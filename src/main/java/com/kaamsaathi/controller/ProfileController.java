package com.kaamsaathi.controller;

import com.kaamsaathi.dto.ApplicationResponse;
import com.kaamsaathi.dto.ProfileResponse;
import com.kaamsaathi.exception.ApiException;
import com.kaamsaathi.model.User;
import com.kaamsaathi.repository.ApplicationRepository;
import com.kaamsaathi.repository.UserRepository;
import com.kaamsaathi.service.ApplicationService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/profile")
public class ProfileController {

    private final UserRepository users;
    private final ApplicationRepository apps;

    public ProfileController(UserRepository users, ApplicationRepository apps) {
        this.users = users;
        this.apps = apps;
    }

    @GetMapping("/me")
    @Transactional(readOnly = true)
    public ProfileResponse me(Authentication auth) {
        Long id;
        try { id = Long.valueOf(auth.getName()); }
        catch (NumberFormatException e) { throw new ApiException(HttpStatus.UNAUTHORIZED, "कृपया दोबारा लॉगिन करें।"); }
        User u = users.findById(id)
                .orElseThrow(() -> new ApiException(HttpStatus.UNAUTHORIZED, "कृपया दोबारा लॉगिन करें।"));
        List<ApplicationResponse> list = apps.findByUserIdWithJob(id).stream()
                .map(ApplicationService::toResponse).toList();
        int completion = 20 + pts(u.getName()) + pts(u.getPhone()) + pts(u.getHomeCity()) + pts(u.getWorkCity());
        return new ProfileResponse(u.getEmail(), u.getName(), u.getPhone(), u.getHomeCity(), u.getWorkCity(),
                u.getExperience(), completion, list);
    }

    private static int pts(String s) { return (s == null || s.isBlank()) ? 0 : 20; }
}
