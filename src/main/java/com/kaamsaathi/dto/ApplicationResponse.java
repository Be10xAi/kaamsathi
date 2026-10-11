package com.kaamsaathi.dto;

import java.time.Instant;

public record ApplicationResponse(String applicationId, String jobTitle, String jobTitleEn, String company, String city,
                                  String status, Instant appliedAt) {}
