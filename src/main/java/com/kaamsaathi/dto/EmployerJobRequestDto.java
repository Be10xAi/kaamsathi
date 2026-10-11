package com.kaamsaathi.dto;

import com.kaamsaathi.util.Patterns;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record EmployerJobRequestDto(
        @NotBlank(message = "कंपनी का नाम जरूरी है।") @Size(max = 150) String companyName,
        @NotBlank(message = Patterns.PHONE_MSG) @Pattern(regexp = Patterns.PHONE, message = Patterns.PHONE_MSG) String phone,
        @NotBlank(message = "नौकरी का नाम जरूरी है।") @Size(max = 150) String jobTitle,
        @NotBlank(message = "शहर जरूरी है।") @Size(max = 100) String city,
        @Size(max = 60) String salary,
        @Size(max = 60) String experience,
        @Size(max = 2000) String description) {}
