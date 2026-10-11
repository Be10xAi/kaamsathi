package com.kaamsaathi.service;

import com.kaamsaathi.dto.JobResponse;
import com.kaamsaathi.exception.ApiException;
import com.kaamsaathi.model.Job;
import com.kaamsaathi.repository.JobRepository;
import com.kaamsaathi.util.Sanitizer;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Locale;

@Service
public class JobService {

    private final JobRepository jobs;

    public JobService(JobRepository jobs) { this.jobs = jobs; }

    @Transactional(readOnly = true)
    public List<JobResponse> search(String keyword, String city) {
        String c = Sanitizer.clean(city, 100);
        String k = Sanitizer.clean(keyword, 100);
        String pattern = (k == null) ? "%" : "%" + k.replaceAll("[%_\\\\]", "").toLowerCase(Locale.ROOT) + "%";
        return jobs.search(c == null ? "" : c, pattern, PageRequest.of(0, 100))
                .stream().map(JobService::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public JobResponse get(Long id) {
        return jobs.findById(id).filter(Job::isActive).map(JobService::toResponse)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "यह नौकरी उपलब्ध नहीं है।"));
    }

    public static JobResponse toResponse(Job j) {
        return new JobResponse(j.getId(), j.getTitle(),
                j.getTitleHi() != null ? j.getTitleHi() : j.getTitle(),
                j.getCompany(), j.getCity(), nz(j.getLocation()), nz(j.getSalary()), nz(j.getHours()),
                nz(j.getExperience()), nz(j.getFood()), nz(j.getStay()),
                j.getJobType() != null ? j.getJobType() : "Full Time", nz(j.getImageUrl()));
    }

    private static String nz(String s) { return s == null ? "" : s; }
}
