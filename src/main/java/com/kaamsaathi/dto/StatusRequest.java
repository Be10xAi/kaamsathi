package com.kaamsaathi.dto;

import com.kaamsaathi.model.ApplicationStatus;
import jakarta.validation.constraints.NotNull;

public record StatusRequest(@NotNull ApplicationStatus status) {}
