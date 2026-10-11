package com.kaamsaathi.controller;

import com.kaamsaathi.dto.JobResponse;
import com.kaamsaathi.dto.StatusRequest;
import com.kaamsaathi.exception.ApiException;
import com.kaamsaathi.model.*;
import com.kaamsaathi.repository.*;
import com.kaamsaathi.service.JobService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;

/** All endpoints here require ROLE_ADMIN (see SecurityConfig). Admin = phone listed in app.admin.phones. */
@RestController
@RequestMapping("/api/admin")
public class AdminController {
    public record JobRequestView(Long id, String companyName, String phone, String jobTitle, String city,
                                 String salary, String experience, String description, String status, Instant createdAt) {}
    public record ReportView(Long id, Long jobId, String message, String contactPhone, boolean reviewed, Instant createdAt) {}
    public record ApplicationView(String applicationId, Long jobId, String jobTitle, String company, String name,
                                  String phone, Integer age, String gender, String homeCity, String workCity,
                                  String experience, String expectedSalary, String note, String status,
                                  boolean hasPhoto, boolean hasResume, Instant createdAt) {}

    private final EmployerRequestRepository requests;
    private final ReportRepository reports;
    private final ApplicationRepository apps;
    private final JobRepository jobs;
    private final String defaultImage;

    public AdminController(EmployerRequestRepository requests, ReportRepository reports,
                           ApplicationRepository apps, JobRepository jobs,
                           @Value("${app.default-job-image}") String defaultImage) {
        this.requests = requests;
        this.reports = reports;
        this.apps = apps;
        this.jobs = jobs;
        this.defaultImage = defaultImage;
    }

    @GetMapping("/job-requests")
    public List<JobRequestView> jobRequests(@RequestParam(defaultValue = "PENDING") RequestStatus status) {
        return requests.findByStatusOrderByCreatedAtDesc(status).stream().map(AdminController::view).toList();
    }

    @PostMapping("/job-requests/{id}/approve")
    @Transactional
    public JobResponse approve(@PathVariable Long id) {
        EmployerJobRequest r = pending(id);
        r.setStatus(RequestStatus.APPROVED);
        Job j = new Job();
        j.setTitle(r.getJobTitle());
        j.setTitleHi(r.getJobTitle());
        j.setCompany(r.getCompanyName());
        j.setCity(r.getCity());
        j.setSalary(r.getSalary());
        j.setExperience(r.getExperience());
        j.setDescription(r.getDescription());
        j.setJobType("Full Time");
        j.setImageUrl(defaultImage);
        j.setVerified(true);
        return JobService.toResponse(jobs.save(j));
    }

    @PostMapping("/job-requests/{id}/reject")
    @Transactional
    public void reject(@PathVariable Long id) {
        pending(id).setStatus(RequestStatus.REJECTED);
    }

    @GetMapping("/reports")
    public List<ReportView> reports() {
        return reports.findAllByOrderByCreatedAtDesc().stream()
                .map(r -> new ReportView(r.getId(), r.getJobId(), r.getMessage(), r.getContactPhone(), r.isReviewed(), r.getCreatedAt()))
                .toList();
    }

    @GetMapping("/applications")
    @Transactional(readOnly = true)
    public List<ApplicationView> applications() {
        return apps.findAllWithJob(PageRequest.of(0, 200)).stream().map(a -> new ApplicationView(
                a.getApplicationId(), a.getJob().getId(), a.getJob().getTitle(), a.getJob().getCompany(),
                a.getName(), a.getPhone(), a.getAge(), a.getGender(), a.getHomeCity(), a.getWorkCity(),
                a.getExperience(), a.getExpectedSalary(), a.getNote(), a.getStatus().name(),
                a.getPhotoFile() != null, a.getResumeFile() != null, a.getCreatedAt())).toList();
    }

    @PatchMapping("/applications/{applicationId}/status")
    @Transactional
    public void updateStatus(@PathVariable String applicationId, @Valid @RequestBody StatusRequest body) {
        JobApplication a = apps.findByApplicationId(applicationId)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "आवेदन नहीं मिला।"));
        a.setStatus(body.status());
    }

    private EmployerJobRequest pending(Long id) {
        EmployerJobRequest r = requests.findById(id)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "अनुरोध नहीं मिला।"));
        if (r.getStatus() != RequestStatus.PENDING) {
            throw new ApiException(HttpStatus.CONFLICT, "यह अनुरोध पहले ही प्रोसेस हो चुका है।");
        }
        return r;
    }

    private static JobRequestView view(EmployerJobRequest r) {
        return new JobRequestView(r.getId(), r.getCompanyName(), r.getPhone(), r.getJobTitle(), r.getCity(),
                r.getSalary(), r.getExperience(), r.getDescription(), r.getStatus().name(), r.getCreatedAt());
    }
}
