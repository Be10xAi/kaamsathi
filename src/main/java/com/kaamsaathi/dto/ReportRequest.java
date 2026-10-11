package com.kaamsaathi.dto;

import com.kaamsaathi.util.Patterns;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record ReportRequest(
        Long jobId,
        @NotBlank(message = "कृपया शिकायत का विवरण लिखें।") @Size(min = 5, max = 1000, message = "विवरण 5 से 1000 अक्षरों के बीच होना चाहिए।") String message,
        @Pattern(regexp = Patterns.PHONE, message = Patterns.PHONE_MSG) String contactPhone) {}
