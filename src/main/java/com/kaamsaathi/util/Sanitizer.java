package com.kaamsaathi.util;

/** Strips HTML angle brackets and control characters from free text; returns null for blank input. */
public final class Sanitizer {
    private Sanitizer() {}

    public static String clean(String s) {
        if (s == null) return null;
        String t = s.replaceAll("[<>\\p{Cntrl}&&[^\\n\\r\\t]]", "").trim();
        return t.isEmpty() ? null : t;
    }

    public static String clean(String s, int maxLen) {
        String t = clean(s);
        return (t != null && t.length() > maxLen) ? t.substring(0, maxLen) : t;
    }
}
