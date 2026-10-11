package com.kaamsaathi.dto;

import java.util.List;

public record ProfileResponse(String email, String name, String phone, String homeCity, String workCity,
                              String experience, int completion, List<ApplicationResponse> applications) {}
