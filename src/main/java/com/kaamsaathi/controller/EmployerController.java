package com.kaamsaathi.controller;

import com.kaamsaathi.dto.EmployerJobRequestDto;
import com.kaamsaathi.exception.ApiException;
import com.kaamsaathi.model.EmployerJobRequest;
import com.kaamsaathi.repository.EmployerRequestRepository;
import com.kaamsaathi.util.Sanitizer;
import com.kaamsaathi.util.Translations;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/employer")
public class EmployerController {

    private final EmployerRequestRepository repo;

    public EmployerController(EmployerRequestRepository repo) { this.repo = repo; }

    /** Saved as PENDING; an admin must approve it before it becomes a public job (fraud protection). */
    @PostMapping("/job-requests")
    @ResponseStatus(HttpStatus.CREATED)
    public Map<String, Object> create(@Valid @RequestBody EmployerJobRequestDto d) {
        String company = Sanitizer.clean(d.companyName(), 150);
        String title = Sanitizer.clean(d.jobTitle(), 150);
        String city = Sanitizer.clean(d.city(), 100);
        if (company == null || title == null || city == null) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "कृपया सभी जरूरी जानकारी सही भरें।");
        }
        EmployerJobRequest r = new EmployerJobRequest();
        r.setCompanyName(company);
        r.setPhone(d.phone());
        r.setJobTitle(title);
        r.setCity(city);
        r.setSalary(Sanitizer.clean(d.salary(), 60));
        r.setExperience(Sanitizer.clean(d.experience(), 60));
        r.setDescription(Sanitizer.clean(d.description(), 2000));
        repo.save(r);
        return Map.of("id", r.getId(), "message", Translations.tr("आपकी job posting request प्राप्त हो गई है। जांच के बाद नौकरी प्रकाशित होगी।"));
    }
}
