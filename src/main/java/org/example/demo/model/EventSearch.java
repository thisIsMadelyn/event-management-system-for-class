package org.example.demo.model;

import java.util.Locale;

/** Search rule for W1: an event matches when its title or location contains the query, ignoring case. */
public final class EventSearch {
    private EventSearch() {
    }

    public static boolean matches(Event event, String query) {
        if (query == null || query.isBlank()) {
            return true;
        }
        String q = query.trim().toLowerCase(Locale.ROOT);
        return event.getTitle().toLowerCase(Locale.ROOT).contains(q)
                || event.getLocation().toLowerCase(Locale.ROOT).contains(q);
    }
}
