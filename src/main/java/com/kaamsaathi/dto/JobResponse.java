package com.kaamsaathi.dto;

/** Field names intentionally match the object shape used by the existing frontend (hi, exp, img ...). */
public record JobResponse(Long id, String title, String hi, String company, String city, String location,
                          String salary, String hours, String exp, String food, String stay,
                          String type, String img) {}
