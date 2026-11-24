package org.example.demo.model;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.Locale;

/** Search rule for W1: upcoming events that match the keyword, the category and the date. */
public final class EventSearch {

    /** The choices in the Date filter. */
    public enum DateFilter {
        ANY_DATE("Any date"),
        THIS_WEEKEND("This weekend");

        private final String label;

        DateFilter(String label) {
            this.label = label;
        }

        @Override
        public String toString() { return label; }
    }

    /** What the attendee searched for. A null category means all categories. */
    public record Criteria(String keyword, Category category, DateFilter date) {
        /** No filters: every upcoming event. */
        public static final Criteria ALL = new Criteria("", null, DateFilter.ANY_DATE);
    }

    private EventSearch() {
    }

    public static boolean matches(Event event, Criteria criteria, LocalDateTime now) {
        return event.getStart().isAfter(now)
                && matchesKeyword(event, criteria.keyword())
                && (criteria.category() == null || criteria.category() == event.getCategory())
                && (criteria.date() == DateFilter.ANY_DATE || isThisWeekend(event, now.toLocalDate()));
    }

    /** The keyword is looked up in the title and the description, ignoring case. */
    private static boolean matchesKeyword(Event event, String keyword) {
        String k = keyword.trim().toLowerCase(Locale.ROOT);
        return event.getTitle().toLowerCase(Locale.ROOT).contains(k)
                || event.getDescription().toLowerCase(Locale.ROOT).contains(k);
    }

    /** The weekend that today is in, or the coming one. */
    private static boolean isThisWeekend(Event event, LocalDate today) {
        LocalDate sunday = today.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));
        LocalDate day = event.getStart().toLocalDate();
        return day.equals(sunday) || day.equals(sunday.minusDays(1));
    }
}
