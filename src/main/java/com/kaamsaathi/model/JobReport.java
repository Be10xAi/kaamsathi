package com.kaamsaathi.model;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "job_reports")
public class JobReport {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private Long jobId;

    @Column(length = 1000, nullable = false)
    private String message;

    @Column(length = 10)
    private String contactPhone;

    @Column(nullable = false)
    private boolean reviewed;

    @Column(nullable = false, updatable = false)
    private Instant createdAt = Instant.now();

    public JobReport() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getJobId() { return jobId; }
    public void setJobId(Long jobId) { this.jobId = jobId; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    public String getContactPhone() { return contactPhone; }
    public void setContactPhone(String contactPhone) { this.contactPhone = contactPhone; }
    public boolean isReviewed() { return reviewed; }
    public void setReviewed(boolean reviewed) { this.reviewed = reviewed; }
    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
}
