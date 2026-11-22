package org.example.demo.model;

import java.util.regex.Pattern;

/**
 * Validation rules for the W2 registration form.
 * Each method returns an error message to show next to the field, or null when the value is valid.
 */
public final class RegistrationValidator {
    private static final Pattern NAME = Pattern.compile("[\\p{L}][\\p{L} .'-]*[\\p{L}.]");
    private static final Pattern EMAIL = Pattern.compile("[^@\\s]+@[^@\\s]+\\.[^@\\s]+");
    private static final Pattern PHONE = Pattern.compile("\\+?[0-9]{10,15}");

    private RegistrationValidator() {
    }

    public static String validateFullName(String value) {
        String v = value == null ? "" : value.trim();
        if (v.isEmpty()) {
            return "Enter your full name.";
        }
        if (!NAME.matcher(v).matches()) {
            return "Use letters only, at least 2 characters.";
        }
        return null;
    }

    public static String validateEmail(String value) {
        String v = value == null ? "" : value.trim();
        if (v.isEmpty()) {
            return "Enter your email address.";
        }
        if (!EMAIL.matcher(v).matches()) {
            return "Enter a valid email, for example name@example.com.";
        }
        return null;
    }

    public static String validatePhone(String value) {
        String v = value == null ? "" : value.replace(" ", "");
        if (v.isEmpty()) {
            return "Enter your phone number.";
        }
        if (!PHONE.matcher(v).matches()) {
            return "Enter 10 to 15 digits, optionally starting with +.";
        }
        return null;
    }
}
