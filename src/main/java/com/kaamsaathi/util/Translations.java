package com.kaamsaathi.util;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Locale;
import java.util.Map;

/**
 * Messages in the code are written in Hindi. If the browser asks for English (header "Accept-Language: en"),
 * they are translated here. Unknown messages are returned unchanged.
 */
public final class Translations {
    private Translations() {}

    private static final Map<String, String> EN = Map.ofEntries(
            Map.entry("कंपनी का नाम जरूरी है।", "Company name is required."),
            Map.entry("नौकरी का नाम जरूरी है।", "Job title is required."),
            Map.entry("शहर जरूरी है।", "City is required."),
            Map.entry("कृपया नौकरी चुनें।", "Please select a job."),
            Map.entry("नाम जरूरी है।", "Name is required."),
            Map.entry("नाम बहुत लंबा है।", "Name is too long."),
            Map.entry("उम्र कम से कम 18 होनी चाहिए।", "Age must be at least 18."),
            Map.entry("उम्र अधिकतम 70 हो सकती है।", "Age can be at most 70."),
            Map.entry("अतिरिक्त जानकारी 1000 अक्षरों तक ही हो सकती है।", "Additional information can be up to 1000 characters."),
            Map.entry("ईमेल जरूरी है।", "Email is required."),
            Map.entry("कृपया सही ईमेल डालें।", "Please enter a valid email."),
            Map.entry("पासवर्ड जरूरी है।", "Password is required."),
            Map.entry("पासवर्ड 8 से 72 अक्षरों का होना चाहिए।", "Password must be 8 to 72 characters long."),
            Map.entry("पासवर्ड में कम से कम एक अक्षर और एक अंक होना चाहिए।", "Password must contain at least one letter and one digit."),
            Map.entry("कृपया शिकायत का विवरण लिखें।", "Please describe your complaint."),
            Map.entry("विवरण 5 से 1000 अक्षरों के बीच होना चाहिए।", "Description must be between 5 and 1000 characters."),
            Map.entry("कृपया सही 10 अंकों का मोबाइल नंबर डालें।", "Please enter a valid 10-digit mobile number."),
            Map.entry("कृपया पहले लॉगिन करें।", "Please log in first."),
            Map.entry("आपको इसकी अनुमति नहीं है।", "You are not allowed to do this."),
            Map.entry("यह जानकारी पहले से मौजूद है।", "This information already exists."),
            Map.entry("सर्वर में समस्या है, कृपया बाद में कोशिश करें।", "Something went wrong on the server. Please try again later."),
            Map.entry("दी गई जानकारी सही नहीं है।", "The information provided is not valid."),
            Map.entry("फाइल बहुत बड़ी है (अधिकतम 5MB)।", "File is too large (maximum 5 MB)."),
            Map.entry("अनुरोध सही नहीं है।", "Invalid request."),
            Map.entry("ईमेल या पासवर्ड गलत है।", "Incorrect email or password."),
            Map.entry("कृपया सही नाम लिखें।", "Please enter a valid name."),
            Map.entry("इस ईमेल से खाता पहले से मौजूद है। कृपया लॉगिन करें।", "An account with this email already exists. Please log in."),
            Map.entry("बहुत ज्यादा गलत प्रयास हुए। कृपया कुछ देर बाद कोशिश करें।", "Too many wrong attempts. Please try again after some time."),
            Map.entry("यह नौकरी उपलब्ध नहीं है।", "This job is not available."),
            Map.entry("आप इस नौकरी के लिए पहले ही आवेदन कर चुके हैं।", "You have already applied for this job."),
            Map.entry("फाइल बहुत बड़ी है।", "File is too large."),
            Map.entry("यह फाइल प्रकार स्वीकार नहीं है।", "This file type is not accepted."),
            Map.entry("फाइल मान्य नहीं है।", "The file is not valid."),
            Map.entry("फाइल सेव नहीं हो सकी।", "The file could not be saved."),
            Map.entry("आपकी शिकायत दर्ज कर ली गई है। धन्यवाद।", "Your complaint has been recorded. Thank you."),
            Map.entry("आवेदन नहीं मिला।", "Application not found."),
            Map.entry("अनुरोध नहीं मिला।", "Request not found."),
            Map.entry("यह अनुरोध पहले ही प्रोसेस हो चुका है।", "This request has already been processed."),
            Map.entry("कृपया दोबारा लॉगिन करें।", "Please log in again."),
            Map.entry("कृपया सभी जरूरी जानकारी सही भरें।", "Please fill in all required information correctly."),
            Map.entry("आपकी job posting request प्राप्त हो गई है। जांच के बाद नौकरी प्रकाशित होगी।", "Your job posting request has been received. The job will be published after review."),
            Map.entry("बहुत ज्यादा अनुरोध। कृपया एक मिनट बाद कोशिश करें।", "Too many requests. Please try again after a minute."));

    /** "en" if the header asks for English, otherwise "hi" (default). */
    public static String langOf(String acceptLanguage) {
        return acceptLanguage != null && acceptLanguage.trim().toLowerCase(Locale.ROOT).startsWith("en") ? "en" : "hi";
    }

    public static String translate(String hindi, String lang) {
        if (hindi == null || !"en".equals(lang)) return hindi;
        return EN.getOrDefault(hindi, hindi);
    }

    public static String translate(String hindi, HttpServletRequest request) {
        return translate(hindi, langOf(request.getHeader("Accept-Language")));
    }

    /** Uses the request that is currently being handled (controllers, services, exception handler). */
    public static String tr(String hindi) {
        if (RequestContextHolder.getRequestAttributes() instanceof ServletRequestAttributes sra) {
            return translate(hindi, sra.getRequest());
        }
        return hindi;
    }
}
