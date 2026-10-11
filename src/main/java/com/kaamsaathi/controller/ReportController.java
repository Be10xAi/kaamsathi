package com.kaamsaathi.controller;

import com.kaamsaathi.dto.ReportRequest;
import com.kaamsaathi.exception.ApiException;
import com.kaamsaathi.model.JobReport;
import com.kaamsaathi.repository.ReportRepository;
import com.kaamsaathi.util.Sanitizer;
import com.kaamsaathi.util.Translations;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/reports")
public class ReportController {

    private final ReportRepository repo;

    public ReportController(ReportRepository repo) { this.repo = repo; }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Map<String, String> report(@Valid @RequestBody ReportRequest req) {
        String msg = Sanitizer.clean(req.message(), 1000);
        if (msg == null) throw new ApiException(HttpStatus.BAD_REQUEST, "कृपया शिकायत का विवरण लिखें।");
        JobReport r = new JobReport();
        r.setJobId(req.jobId());
        r.setMessage(msg);
        r.setContactPhone(req.contactPhone());
        repo.save(r);
        return Map.of("message", Translations.tr("आपकी शिकायत दर्ज कर ली गई है। धन्यवाद।"));
    }
}
