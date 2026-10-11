package com.kaamsaathi.dto;

public record AuthResponse(String token, String email, String name, String role, long expiresInSeconds) {}
