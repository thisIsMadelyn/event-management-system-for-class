package org.example.demo.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.Locale;

/**
 * Validation rules for the W4 create event form.
 * Each validate method returns an error message to show next to the field, or null when the value is valid.
 */
public final class EventValidator {
    /** Strict, so that a date such as 31/02/2027 is refused and not moved to another day. */
    private static final DateTimeFormatter DATE =
            DateTimeFormatter.ofPattern("d/M/uuuu", Locale.ENGLISH).withResolverStyle(ResolverStyle.STRICT);
    private static final DateTimeFormatter TIME =
            DateTimeFormatter.ofPattern("H:mm", Locale.ENGLISH).withResolverStyle(ResolverStyle.STRICT);
    private static final int MAX_CAPACITY = 100_000;

    private EventValidator() {
    }

    /** For the fields that only have to be filled in: title and location. */
    public static String validateRequired(String fieldName, String value) {
        return value == null || value.isBlank() ? fieldName + " is required." : null;
    }

    public static String validateCategory(Category value) {
        return value == null ? "Category is required." : null;
    }

    public static String validateDate(String value, LocalDate today) {
        if (value == null || value.isBlank()) {
            return "Date is required.";
        }
        LocalDate date = parseDate(value);
        if (date == null) {
            return "Enter a valid date as dd/mm/yyyy.";
        }
        return date.isBefore(today) ? "Date must be today or later." : null;
    }

    /** The date may be empty or invalid here, because it has its own message. */
    public static String validateStartTime(String value, String date, LocalDateTime now) {
        if (value == null || value.isBlank()) {
            return "Start time is required.";
        }
        LocalTime start = parseTime(value);
        if (start == null) {
            return "Enter a valid time as hh:mm.";
        }
        LocalDate day = parseDate(date);
        boolean past = day != null && day.equals(now.toLocalDate()) && !day.atTime(start).isAfter(now);
        return past ? "Start time must be later than now." : null;
    }

    /** The start time may be empty or invalid here, because it has its own message. */
    public static String validateEndTime(String value, String startTime) {
        if (value == null || value.isBlank()) {
            return "End time is required.";
        }
        LocalTime end = parseTime(value);
        if (end == null) {
            return "Enter a valid time as hh:mm.";
        }
        LocalTime start = parseTime(startTime);
        return start != null && !end.isAfter(start) ? "End time must be after the start time." : null;
    }

    public static String validateCapacity(String value) {
        if (value == null || value.isBlank()) {
            return "Capacity is required.";
        }
        String v = value.trim();
        if (!v.matches("[0-9]+") || v.matches("0+")) {
            return "Capacity must be a whole number greater than zero.";
        }
        if (v.length() > 9 || Integer.parseInt(v) > MAX_CAPACITY) {
            return "Capacity can be at most " + MAX_CAPACITY + ".";
        }
        return null;
    }

    /** Returns null when the text is not a date. */
    public static LocalDate parseDate(String value) {
        try {
            return value == null ? null : LocalDate.parse(value.trim(), DATE);
        } catch (DateTimeParseException e) {
            return null;
        }
    }

    /** Returns null when the text is not a time. */
    public static LocalTime parseTime(String value) {
        try {
            return value == null ? null : LocalTime.parse(value.trim(), TIME);
        } catch (DateTimeParseException e) {
            return null;
        }
    }
}
