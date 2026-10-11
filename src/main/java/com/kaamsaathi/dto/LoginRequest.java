package com.kaamsaathi.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record LoginRequest(
        @NotBlank(message = "ईमेल जरूरी है।") @Email(message = "कृपया सही ईमेल डालें।") @Size(max = 150) String email,
        @NotBlank(message = "पासवर्ड जरूरी है।") @Size(max = 72) String password) {}
