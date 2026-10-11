package com.kaamsaathi.model;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "jobs")
public class Job {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 150, nullable = false)
    private String title;

    @Column(length = 150)
    private String titleHi;

    @Column(length = 150, nullable = false)
    private String company;

    @Column(length = 100, nullable = false)
    private String city;

    @Column(length = 150)
    private String location;

    @Column(length = 60)
    private String salary;

    @Column(length = 40)
    private String hours;

    @Column(length = 60)
    private String experience;

    @Column(length = 30)
    private String food;

    @Column(length = 30)
    private String stay;

    @Column(length = 30)
    private String jobType;

    @Column(length = 500)
    private String imageUrl;

    @Column(length = 60)
    private String category;

    @Column(length = 2000)
    private String description;

    @Column(nullable = false)
    private boolean verified;

    @Column(nullable = false)
    private boolean active = true;

    @Column(nullable = false, updatable = false)
    private Instant createdAt = Instant.now();

    public Job() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getTitleHi() { return titleHi; }
    public void setTitleHi(String titleHi) { this.titleHi = titleHi; }
    public String getCompany() { return company; }
    public void setCompany(String company) { this.company = company; }
    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
    public String getSalary() { return salary; }
    public void setSalary(String salary) { this.salary = salary; }
    public String getHours() { return hours; }
    public void setHours(String hours) { this.hours = hours; }
    public String getExperience() { return experience; }
    public void setExperience(String experience) { this.experience = experience; }
    public String getFood() { return food; }
    public void setFood(String food) { this.food = food; }
    public String getStay() { return stay; }
    public void setStay(String stay) { this.stay = stay; }
    public String getJobType() { return jobType; }
    public void setJobType(String jobType) { this.jobType = jobType; }
    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public boolean isVerified() { return verified; }
    public void setVerified(boolean verified) { this.verified = verified; }
    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
}
