package com.kaamsaathi.config;

import com.kaamsaathi.security.JwtAuthFilter;
import com.kaamsaathi.security.JwtService;
import com.kaamsaathi.security.RateLimitFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.header.writers.ReferrerPolicyHeaderWriter;
import org.springframework.security.web.header.writers.StaticHeadersWriter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
public class SecurityConfig {

    // The page uses inline <script>/<style> and Unsplash images, hence 'unsafe-inline' + that image host.
    private static final String CSP = "default-src 'self'; "
            + "img-src 'self' https://images.unsplash.com data:; "
            + "style-src 'self' 'unsafe-inline'; "
            + "script-src 'self' 'unsafe-inline'; "
            + "connect-src 'self'; "
            + "object-src 'none'; base-uri 'self'; form-action 'self'; frame-ancestors 'none'";

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http, JwtService jwtService,
                                    @Value("${app.rate-limit.auth-per-minute}") int authLimit,
                                    @Value("${app.rate-limit.write-per-minute}") int writeLimit,
                                    @Value("${app.rate-limit.read-per-minute}") int readLimit) throws Exception {
        http
            .csrf(AbstractHttpConfigurer::disable)           // stateless JWT in Authorization header, no cookies
            .cors(Customizer.withDefaults())
            .httpBasic(AbstractHttpConfigurer::disable)
            .formLogin(AbstractHttpConfigurer::disable)
            .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .headers(h -> h
                .contentSecurityPolicy(c -> c.policyDirectives(CSP))
                .frameOptions(HeadersConfigurer.FrameOptionsConfig::deny)
                .referrerPolicy(r -> r.policy(ReferrerPolicyHeaderWriter.ReferrerPolicy.STRICT_ORIGIN_WHEN_CROSS_ORIGIN))
                .addHeaderWriter(new StaticHeadersWriter("Permissions-Policy", "microphone=(self), camera=(), geolocation=()")))
            .authorizeHttpRequests(a -> a
                .requestMatchers("/", "/index.html", "/js/**", "/css/**", "/favicon.ico", "/error").permitAll()
                .requestMatchers(HttpMethod.OPTIONS, "/api/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/jobs", "/api/jobs/*").permitAll()
                .requestMatchers("/api/auth/**").permitAll()
                .requestMatchers(HttpMethod.POST, "/api/applications", "/api/reports", "/api/employer/job-requests").permitAll()
                .requestMatchers("/api/admin/**").hasRole("ADMIN")
                .requestMatchers("/api/**").authenticated()
                .anyRequest().denyAll())
            .exceptionHandling(e -> e
                .authenticationEntryPoint((req, res, ex) -> writeJson(req, res, 401, "कृपया पहले लॉगिन करें।"))
                .accessDeniedHandler((req, res, ex) -> writeJson(req, res, 403, "आपको इसकी अनुमति नहीं है।")))
            .addFilterBefore(new RateLimitFilter(authLimit, writeLimit, readLimit), UsernamePasswordAuthenticationFilter.class)
            .addFilterBefore(new JwtAuthFilter(jwtService), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    private static void writeJson(jakarta.servlet.http.HttpServletRequest req, jakarta.servlet.http.HttpServletResponse res, int status, String message)
            throws java.io.IOException {
        res.setStatus(status);
        res.setCharacterEncoding("UTF-8");
        res.setContentType("application/json;charset=UTF-8");
        res.getWriter().write("{\"status\":" + status + ",\"message\":\"" + com.kaamsaathi.util.Translations.translate(message, req) + "\"}");
    }

    @Bean
    PasswordEncoder passwordEncoder() { return new BCryptPasswordEncoder(10); }

    @Bean
    CorsConfigurationSource corsConfigurationSource(@Value("${app.cors.allowed-origins}") List<String> origins) {
        CorsConfiguration c = new CorsConfiguration();
        c.setAllowedOrigins(origins);
        c.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        c.setAllowedHeaders(List.of("Authorization", "Content-Type", "Accept-Language"));
        c.setAllowCredentials(false);
        c.setMaxAge(3600L);
        UrlBasedCorsConfigurationSource src = new UrlBasedCorsConfigurationSource();
        src.registerCorsConfiguration("/api/**", c);
        return src;
    }
}
