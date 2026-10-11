package com.kaamsaathi.service;

import com.kaamsaathi.dto.ApplicationRequest;
import com.kaamsaathi.dto.ApplicationResponse;
import com.kaamsaathi.exception.ApiException;
import com.kaamsaathi.model.Job;
import com.kaamsaathi.model.JobApplication;
import com.kaamsaathi.repository.ApplicationRepository;
import com.kaamsaathi.repository.JobRepository;
import com.kaamsaathi.repository.UserRepository;
import com.kaamsaathi.util.Sanitizer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.security.SecureRandom;
import java.util.Set;

@Service
public class ApplicationService {

    private static final String ALPHABET = "ABCDEFGHJKLMNPQRSTUVWXYZ23456789";
    private static final SecureRandom RNG = new SecureRandom();
    private static final Set<String> PHOTO_EXT = Set.of("jpg", "jpeg", "png", "webp");
    private static final Set<String> DOC_EXT = Set.of("pdf", "jpg", "jpeg", "png");

    private final ApplicationRepository apps;
    private final JobRepository jobs;
    private final UserRepository users;
    private final FileStorageService storage;
    private final long photoMax;
    private final long resumeMax;

    public ApplicationService(ApplicationRepository apps, JobRepository jobs, UserRepository users,
                              FileStorageService storage,
                              @Value("${app.upload.photo-max-bytes}") long photoMax,
                              @Value("${app.upload.resume-max-bytes}") long resumeMax) {
        this.apps = apps;
        this.jobs = jobs;
        this.users = users;
        this.storage = storage;
        this.photoMax = photoMax;
        this.resumeMax = resumeMax;
    }

    @Transactional
    public ApplicationResponse apply(ApplicationRequest r, MultipartFile photo, MultipartFile resume, Long userId) {
        Job job = jobs.findById(r.jobId()).filter(Job::isActive)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "यह नौकरी उपलब्ध नहीं है।"));
        String name = Sanitizer.clean(r.name(), 100);
        if (name == null) throw new ApiException(HttpStatus.BAD_REQUEST, "कृपया सही नाम लिखें।");
        if (apps.existsByJobIdAndPhone(job.getId(), r.phone())) {
            throw new ApiException(HttpStatus.CONFLICT, "आप इस नौकरी के लिए पहले ही आवेदन कर चुके हैं।");
        }

        JobApplication a = new JobApplication();
        a.setJob(job);
        a.setName(name);
        a.setPhone(r.phone());
        a.setAge(r.age());
        a.setGender(optional(r.gender(), 20));
        a.setHomeCity(Sanitizer.clean(r.homeCity(), 100));
        a.setWorkCity(optional(r.workCity(), 100));
        a.setExperience(Sanitizer.clean(r.experience(), 50));
        a.setExpectedSalary(Sanitizer.clean(r.salary(), 60));
        a.setNote(Sanitizer.clean(r.note(), 1000));
        a.setPhotoFile(storage.store(photo, PHOTO_EXT, photoMax));
        a.setResumeFile(storage.store(resume, DOC_EXT, resumeMax));
        a.setApplicationId(newApplicationId());
        a.setUserId(userId);          // null when the applicant is not logged in
        apps.save(a);

        // If the applicant is logged in, fill the blanks of their profile
        if (userId != null) users.findById(userId).ifPresent(u -> {
            if (isBlank(u.getPhone())) u.setPhone(a.getPhone());
            if (isBlank(u.getName())) u.setName(a.getName());
            if (isBlank(u.getHomeCity())) u.setHomeCity(a.getHomeCity());
            if (isBlank(u.getWorkCity())) u.setWorkCity(a.getWorkCity());
            if (isBlank(u.getExperience())) u.setExperience(a.getExperience());
            users.save(u);
        });
        return toResponse(a);
    }

    private String newApplicationId() {
        String id;
        do {
            StringBuilder sb = new StringBuilder("KS");
            for (int i = 0; i < 8; i++) sb.append(ALPHABET.charAt(RNG.nextInt(ALPHABET.length())));
            id = sb.toString();
        } while (apps.existsByApplicationId(id));
        return id;
    }

    /** The frontend sends "चुनें" ("select") for untouched dropdowns - treat it as empty. */
    private static String optional(String s, int max) {
        String c = Sanitizer.clean(s, max);
        return (c == null || c.equals("चुनें")) ? null : c;
    }

    private static boolean isBlank(String s) { return s == null || s.isBlank(); }

    public static ApplicationResponse toResponse(JobApplication a) {
        Job j = a.getJob();
        return new ApplicationResponse(a.getApplicationId(),
                j.getTitleHi() != null ? j.getTitleHi() : j.getTitle(), j.getTitle(),
                j.getCompany(), j.getCity(), a.getStatus().name(), a.getCreatedAt());
    }
}
