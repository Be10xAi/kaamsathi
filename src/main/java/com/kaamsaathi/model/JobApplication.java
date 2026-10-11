package com.kaamsaathi.model;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "job_applications",
       uniqueConstraints = @UniqueConstraint(columnNames = {"job_id", "phone"}),
       indexes = {@Index(columnList = "phone"), @Index(columnList = "user_id")})
public class JobApplication {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 16, nullable = false, unique = true)
    private String applicationId;

    @Column
    private Long userId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false) @JoinColumn(name = "job_id")
    private Job job;

    @Column(length = 100, nullable = false)
    private String name;

    @Column(length = 10, nullable = false)
    private String phone;

    @Column
    private Integer age;

    @Column(length = 20)
    private String gender;

    @Column(length = 100)
    private String homeCity;

    @Column(length = 100)
    private String workCity;

    @Column(length = 50)
    private String experience;

    @Column(length = 60)
    private String expectedSalary;

    @Column(length = 1000)
    private String note;

    @Column(length = 100)
    private String photoFile;

    @Column(length = 100)
    private String resumeFile;

    @Enumerated(EnumType.STRING) @Column(nullable = false, length = 15)
    private ApplicationStatus status = ApplicationStatus.SUBMITTED;

    @Column(nullable = false, updatable = false)
    private Instant createdAt = Instant.now();

    public JobApplication() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getApplicationId() { return applicationId; }
    public void setApplicationId(String applicationId) { this.applicationId = applicationId; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public Job getJob() { return job; }
    public void setJob(Job job) { this.job = job; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public Integer getAge() { return age; }
    public void setAge(Integer age) { this.age = age; }
    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }
    public String getHomeCity() { return homeCity; }
    public void setHomeCity(String homeCity) { this.homeCity = homeCity; }
    public String getWorkCity() { return workCity; }
    public void setWorkCity(String workCity) { this.workCity = workCity; }
    public String getExperience() { return experience; }
    public void setExperience(String experience) { this.experience = experience; }
    public String getExpectedSalary() { return expectedSalary; }
    public void setExpectedSalary(String expectedSalary) { this.expectedSalary = expectedSalary; }
    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }
    public String getPhotoFile() { return photoFile; }
    public void setPhotoFile(String photoFile) { this.photoFile = photoFile; }
    public String getResumeFile() { return resumeFile; }
    public void setResumeFile(String resumeFile) { this.resumeFile = resumeFile; }
    public ApplicationStatus getStatus() { return status; }
    public void setStatus(ApplicationStatus status) { this.status = status; }
    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
}
