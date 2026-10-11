package com.kaamsaathi.model;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "employer_job_requests")
public class EmployerJobRequest {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 150, nullable = false)
    private String companyName;

    @Column(length = 10, nullable = false)
    private String phone;

    @Column(length = 150, nullable = false)
    private String jobTitle;

    @Column(length = 100, nullable = false)
    private String city;

    @Column(length = 60)
    private String salary;

    @Column(length = 60)
    private String experience;

    @Column(length = 2000)
    private String description;

    @Enumerated(EnumType.STRING) @Column(nullable = false, length = 10)
    private RequestStatus status = RequestStatus.PENDING;

    @Column(nullable = false, updatable = false)
    private Instant createdAt = Instant.now();

    public EmployerJobRequest() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getCompanyName() { return companyName; }
    public void setCompanyName(String companyName) { this.companyName = companyName; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getJobTitle() { return jobTitle; }
    public void setJobTitle(String jobTitle) { this.jobTitle = jobTitle; }
    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }
    public String getSalary() { return salary; }
    public void setSalary(String salary) { this.salary = salary; }
    public String getExperience() { return experience; }
    public void setExperience(String experience) { this.experience = experience; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public RequestStatus getStatus() { return status; }
    public void setStatus(RequestStatus status) { this.status = status; }
    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
}
