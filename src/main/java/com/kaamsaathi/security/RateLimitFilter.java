package com.kaamsaathi.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Simple in-memory fixed-window rate limiter per client IP (protects OTP, apply, report, employer endpoints).
 * If you run behind a reverse proxy, configure server.forward-headers-strategy so getRemoteAddr() is the real client IP.
 * For multiple server instances use Redis (or a gateway) instead.
 */
public class RateLimitFilter extends OncePerRequestFilter {

    private static final long WINDOW_MS = 60_000;
    private final ConcurrentHashMap<String, long[]> windows = new ConcurrentHashMap<>();
    private final int authLimit, writeLimit, readLimit;

    public RateLimitFilter(int authLimit, int writeLimit, int readLimit) {
        this.authLimit = authLimit;
        this.writeLimit = writeLimit;
        this.readLimit = readLimit;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return !request.getRequestURI().startsWith("/api/");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
            throws ServletException, IOException {
        String path = req.getRequestURI();
        String bucket;
        int limit;
        if (path.startsWith("/api/auth/")) { bucket = "auth"; limit = authLimit; }
        else if (!"GET".equals(req.getMethod())) { bucket = "write"; limit = writeLimit; }
        else { bucket = "read"; limit = readLimit; }

        if (!allow(req.getRemoteAddr() + "|" + bucket, limit)) {
            res.setStatus(429);
            res.setCharacterEncoding("UTF-8");
            res.setContentType("application/json;charset=UTF-8");
            res.setHeader("Retry-After", "60");
            res.getWriter().write("{\"status\":429,\"message\":\""
                    + com.kaamsaathi.util.Translations.translate("बहुत ज्यादा अनुरोध। कृपया एक मिनट बाद कोशिश करें।", req) + "\"}");
            return;
        }
        chain.doFilter(req, res);
    }

    private boolean allow(String key, int limit) {
        long now = System.currentTimeMillis();
        if (windows.size() > 50_000) windows.entrySet().removeIf(e -> now - e.getValue()[0] >= WINDOW_MS);
        long[] w = windows.compute(key, (k, v) -> {
            if (v == null || now - v[0] >= WINDOW_MS) return new long[]{now, 1};
            v[1]++;
            return v;
        });
        return w[1] <= limit;
    }
}
