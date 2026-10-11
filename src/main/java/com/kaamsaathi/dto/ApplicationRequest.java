package com.kaamsaathi.dto;

import com.kaamsaathi.util.Patterns;
import jakarta.validation.constraints.*;

public record ApplicationRequest(
        @NotNull(message = "कृपया नौकरी चुनें।") Long jobId,
        @NotBlank(message = "नाम जरूरी है।") @Size(max = 100, message = "नाम बहुत लंबा है।") String name,
        @NotBlank(message = Patterns.PHONE_MSG) @Pattern(regexp = Patterns.PHONE, message = Patterns.PHONE_MSG) String phone,
        @Min(value = 18, message = "उम्र कम से कम 18 होनी चाहिए।") @Max(value = 70, message = "उम्र अधिकतम 70 हो सकती है।") Integer age,
        @Size(max = 20) String gender,
        @Size(max = 100) String homeCity,
        @Size(max = 100) String workCity,
        @Size(max = 50) String experience,
        @Size(max = 60) String salary,
        @Size(max = 1000, message = "अतिरिक्त जानकारी 1000 अक्षरों तक ही हो सकती है।") String note) {}
