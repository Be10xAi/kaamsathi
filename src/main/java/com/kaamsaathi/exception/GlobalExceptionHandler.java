package com.kaamsaathi.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.*;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.kaamsaathi.util.Translations;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;

/** One consistent JSON error shape: { timestamp, status, message, errors? }. Never leaks stack traces. */
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    private static Map<String, Object> error(int status, String message, Map<String, String> errors) {
        Map<String, Object> m = new LinkedHashMap<>();
        m.put("timestamp", Instant.now().toString());
        m.put("status", status);
        m.put("message", Translations.tr(message));
        if (errors != null && !errors.isEmpty()) {
            Map<String, String> translated = new LinkedHashMap<>();
            errors.forEach((k, v) -> translated.put(k, Translations.tr(v)));
            m.put("errors", translated);
        }
        return m;
    }

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<Object> handleApi(ApiException ex) {
        return ResponseEntity.status(ex.getStatus()).body(error(ex.getStatus().value(), ex.getMessage(), null));
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Object> handleIntegrity(DataIntegrityViolationException ex) {
        log.warn("Data integrity violation: {}", ex.getMostSpecificCause().getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(error(409, "यह जानकारी पहले से मौजूद है।", null));
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Object> handleDenied(AccessDeniedException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(error(403, "आपको इसकी अनुमति नहीं है।", null));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleAny(Exception ex) {
        log.error("Unhandled error", ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(error(500, "सर्वर में समस्या है, कृपया बाद में कोशिश करें।", null));
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers,
                                                                  HttpStatusCode status, WebRequest request) {
        Map<String, String> errors = new LinkedHashMap<>();
        for (FieldError fe : ex.getBindingResult().getFieldErrors()) {
            errors.putIfAbsent(fe.getField(), fe.getDefaultMessage());
        }
        String first = errors.values().stream().findFirst().orElse("दी गई जानकारी सही नहीं है।");
        return ResponseEntity.badRequest().body(error(400, first, errors));
    }

    @Override
    protected ResponseEntity<Object> handleHandlerMethodValidationException(HandlerMethodValidationException ex, HttpHeaders headers,
                                                                            HttpStatusCode status, WebRequest request) {
        return ResponseEntity.badRequest().body(error(400, "दी गई जानकारी सही नहीं है।", null));
    }

    /** All other standard Spring MVC errors (405, 404, 413, malformed JSON ...) get the same safe shape. */
    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers,
                                                             HttpStatusCode status, WebRequest request) {
        String msg;
        if (status.value() == 413) msg = "फाइल बहुत बड़ी है (अधिकतम 5MB)।";
        else if (status.is4xxClientError()) msg = "अनुरोध सही नहीं है।";
        else msg = "सर्वर में समस्या है, कृपया बाद में कोशिश करें।";
        return ResponseEntity.status(status).headers(headers).body(error(status.value(), msg, null));
    }
}
