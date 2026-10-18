package com.shopsphere.util;

import java.util.regex.Pattern;

/**
 * Server-side input validation utility for form sanitization and constraint checking.
 */
public class ValidationUtil {

    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    private static final Pattern MOBILE_PATTERN = Pattern.compile("^[0-9]{10,15}$");

    public static boolean isValidEmail(String email) {
        if (email == null) return false;
        return EMAIL_PATTERN.matcher(email.trim()).matches();
    }

    public static boolean isValidMobile(String mobile) {
        if (mobile == null || mobile.trim().isEmpty()) return true; // Optional field
        return MOBILE_PATTERN.matcher(mobile.trim()).matches();
    }

    public static boolean isNotEmpty(String value) {
        return value != null && !value.trim().isEmpty();
    }

    public static boolean isValidPassword(String password) {
        return password != null && password.length() >= 6;
    }

    public static String sanitize(String input) {
        if (input == null) return "";
        return input.trim();
    }
}
