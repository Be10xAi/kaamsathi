package com.kaamsaathi.controller;

import com.kaamsaathi.dto.ApplicationRequest;
import com.kaamsaathi.dto.ApplicationResponse;
import com.kaamsaathi.service.ApplicationService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/applications")
public class ApplicationController {

    private final ApplicationService service;

    public ApplicationController(ApplicationService service) { this.service = service; }

    /** multipart/form-data: part "data" = JSON, optional parts "photo" and "resume" = files.
     *  Works with or without login; if the user is logged in the application is linked to their account. */
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ApplicationResponse apply(@Valid @RequestPart("data") ApplicationRequest data,
                                     @RequestPart(value = "photo", required = false) MultipartFile photo,
                                     @RequestPart(value = "resume", required = false) MultipartFile resume,
                                     Authentication auth) {
        return service.apply(data, photo, resume, userId(auth));
    }

    private static Long userId(Authentication auth) {
        if (auth == null || auth instanceof AnonymousAuthenticationToken || !auth.isAuthenticated()) return null;
        try { return Long.valueOf(auth.getName()); } catch (NumberFormatException e) { return null; }
    }
}
