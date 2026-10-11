package com.kaamsaathi.model;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "users")
public class User {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 150, unique = true)
    private String email;

    @Column(length = 100)
    private String passwordHash;

    @Column(length = 10)
    private String phone;

    @Column(length = 100)
    private String name;

    @Column(length = 100)
    private String homeCity;

    @Column(length = 100)
    private String workCity;

    @Column(length = 50)
    private String experience;

    @Enumerated(EnumType.STRING) @Column(nullable = false, length = 10)
    private Role role = Role.USER;

    @Column(nullable = false)
    private int failedAttempts;

    @Column
    private Instant lockedUntil;

    @Column(nullable = false, updatable = false)
    private Instant createdAt = Instant.now();

    public User() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPasswordHash() { return passwordHash; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getHomeCity() { return homeCity; }
    public void setHomeCity(String homeCity) { this.homeCity = homeCity; }
    public String getWorkCity() { return workCity; }
    public void setWorkCity(String workCity) { this.workCity = workCity; }
    public String getExperience() { return experience; }
    public void setExperience(String experience) { this.experience = experience; }
    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }
    public int getFailedAttempts() { return failedAttempts; }
    public void setFailedAttempts(int failedAttempts) { this.failedAttempts = failedAttempts; }
    public Instant getLockedUntil() { return lockedUntil; }
    public void setLockedUntil(Instant lockedUntil) { this.lockedUntil = lockedUntil; }
    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
}
