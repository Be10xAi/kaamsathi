package com.kaamsaathi.service;

import com.kaamsaathi.dto.AuthResponse;
import com.kaamsaathi.dto.LoginRequest;
import com.kaamsaathi.dto.RegisterRequest;
import com.kaamsaathi.exception.ApiException;
import com.kaamsaathi.model.Role;
import com.kaamsaathi.model.User;
import com.kaamsaathi.repository.UserRepository;
import com.kaamsaathi.security.JwtService;
import com.kaamsaathi.util.Sanitizer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AuthService {

    private static final String BAD_LOGIN = "ईमेल या पासवर्ड गलत है।";

    private final UserRepository users;
    private final PasswordEncoder encoder;
    private final JwtService jwt;
    private final Set<String> adminEmails;
    private final int maxFailed;
    private final long lockMinutes;
    private final String dummyHash;   // used so unknown emails take the same time as wrong passwords

    public AuthService(UserRepository users, PasswordEncoder encoder, JwtService jwt,
                       @Value("${app.admin.emails:}") String admins,
                       @Value("${app.login.max-failed-attempts}") int maxFailed,
                       @Value("${app.login.lock-minutes}") long lockMinutes) {
        this.users = users;
        this.encoder = encoder;
        this.jwt = jwt;
        this.maxFailed = maxFailed;
        this.lockMinutes = lockMinutes;
        this.dummyHash = encoder.encode("dummy-password-for-timing-only");
        this.adminEmails = Arrays.stream(admins.split(",")).map(s -> s.trim().toLowerCase(Locale.ROOT))
                .filter(s -> !s.isEmpty()).collect(Collectors.toSet());
    }

    @Transactional
    public AuthResponse register(RegisterRequest r) {
        String email = r.email().trim().toLowerCase(Locale.ROOT);
        String name = Sanitizer.clean(r.name(), 100);
        if (name == null) throw new ApiException(HttpStatus.BAD_REQUEST, "कृपया सही नाम लिखें।");
        if (users.existsByEmail(email)) {
            throw new ApiException(HttpStatus.CONFLICT, "इस ईमेल से खाता पहले से मौजूद है। कृपया लॉगिन करें।");
        }
        User u = new User();
        u.setEmail(email);
        u.setName(name);
        u.setPhone(r.phone());
        u.setPasswordHash(encoder.encode(r.password()));      // BCrypt hash, the password itself is never stored
        u.setRole(roleFor(email));
        try {
            users.save(u);
        } catch (DataIntegrityViolationException e) {         // two registrations at the same moment
            throw new ApiException(HttpStatus.CONFLICT, "इस ईमेल से खाता पहले से मौजूद है। कृपया लॉगिन करें।");
        }
        return token(u);
    }

    /** noRollbackFor: the failed-attempt counter and lock must be saved even though we throw. */
    @Transactional(noRollbackFor = ApiException.class)
    public AuthResponse login(LoginRequest r) {
        String email = r.email().trim().toLowerCase(Locale.ROOT);
        User u = users.findByEmail(email).orElse(null);
        if (u == null) {
            encoder.matches(r.password(), dummyHash);
            throw new ApiException(HttpStatus.UNAUTHORIZED, BAD_LOGIN);
        }
        if (u.getLockedUntil() != null && u.getLockedUntil().isAfter(Instant.now())) {
            throw new ApiException(HttpStatus.TOO_MANY_REQUESTS,
                    "बहुत ज्यादा गलत प्रयास हुए। कृपया कुछ देर बाद कोशिश करें।");
        }
        if (u.getPasswordHash() == null || !encoder.matches(r.password(), u.getPasswordHash())) {
            u.setFailedAttempts(u.getFailedAttempts() + 1);
            if (u.getFailedAttempts() >= maxFailed) {
                u.setLockedUntil(Instant.now().plus(lockMinutes, ChronoUnit.MINUTES));
                u.setFailedAttempts(0);
            }
            users.save(u);
            throw new ApiException(HttpStatus.UNAUTHORIZED, BAD_LOGIN);
        }
        u.setFailedAttempts(0);
        u.setLockedUntil(null);
        u.setRole(roleFor(email));
        users.save(u);
        return token(u);
    }

    private Role roleFor(String email) { return adminEmails.contains(email) ? Role.ADMIN : Role.USER; }

    private AuthResponse token(User u) {
        return new AuthResponse(jwt.generate(String.valueOf(u.getId()), u.getRole()),
                u.getEmail(), u.getName(), u.getRole().name(), jwt.ttlSeconds());
    }
}
