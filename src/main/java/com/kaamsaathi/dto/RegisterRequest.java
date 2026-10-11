package com.kaamsaathi.dto;

import com.kaamsaathi.util.Patterns;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record RegisterRequest(
        @NotBlank(message = "नाम जरूरी है।") @Size(max = 100, message = "नाम बहुत लंबा है।") String name,
        @NotBlank(message = "ईमेल जरूरी है।") @Email(message = "कृपया सही ईमेल डालें।") @Size(max = 150) String email,
        @Pattern(regexp = Patterns.PHONE, message = Patterns.PHONE_MSG) String phone,
        @NotBlank(message = "पासवर्ड जरूरी है।")
        @Size(min = 8, max = 72, message = "पासवर्ड 8 से 72 अक्षरों का होना चाहिए।")
        @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*[0-9]).+$", message = "पासवर्ड में कम से कम एक अक्षर और एक अंक होना चाहिए।") String password) {}
