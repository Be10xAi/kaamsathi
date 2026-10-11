package com.kaamsaathi.util;

public final class Patterns {
    private Patterns() {}
    /** Indian mobile number: 10 digits starting with 6-9 */
    public static final String PHONE = "^[6-9][0-9]{9}$";
    public static final String PHONE_MSG = "कृपया सही 10 अंकों का मोबाइल नंबर डालें।";
}
