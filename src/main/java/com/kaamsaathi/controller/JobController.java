package com.kaamsaathi.controller;

import com.kaamsaathi.dto.JobResponse;
import com.kaamsaathi.service.JobService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/jobs")
public class JobController {

    private final JobService jobService;

    public JobController(JobService jobService) { this.jobService = jobService; }

    @GetMapping
    public List<JobResponse> list(@RequestParam(required = false) String keyword,
                                  @RequestParam(required = false) String city) {
        return jobService.search(keyword, city);
    }

    @GetMapping("/{id}")
    public JobResponse get(@PathVariable Long id) { return jobService.get(id); }
}
