package org.example.demo.model;

import java.text.Normalizer;
import java.util.regex.Pattern;

/**
 * Validation rules for the W2 registration form.
 * Each method returns an error message to show next to the field, or null when the value is valid.
 */
public final class RegistrationValidator {
    private static final Pattern NAME = Pattern.compile("[\\p{L}][\\p{L} .'-]*[\\p{L}.]");

    /** Parts separated by single dots on both sides of the @, with at least one dot in the domain. */
    private static final Pattern EMAIL =
            Pattern.compile("[^@\\s.]+(\\.[^@\\s.]+)*@[^@\\s.]+(\\.[^@\\s.]+)+");

    private RegistrationValidator() {
    }

    public static String validateFullName(String value) {
        // NFC joins a letter and a separate accent into one letter, so both ways of typing it are accepted.
        String v = value == null ? "" : Normalizer.normalize(value.trim(), Normalizer.Form.NFC);
        if (v.isEmpty()) {
            return "Full name is required.";
        }
        if (!NAME.matcher(v).matches()) {
            return "Use letters only, at least 2 characters.";
        }
        return null;
    }

    public static String validateEmail(String value) {
        String v = value == null ? "" : value.trim();
        if (v.isEmpty()) {
            return "Email address is required.";
        }
        if (!EMAIL.matcher(v).matches()) {
            return "Enter a valid email address, for example name@example.org.";
        }
        return null;
    }
}
