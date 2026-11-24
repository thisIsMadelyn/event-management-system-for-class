package org.example.demo.model;

import java.time.LocalDateTime;

/** One attendee's registration for one event, with the details entered in the W2 form. */
public class Registration {
    private final Event event;
    private final String fullName;
    private final String email;
    private final int places;
    private final String needs;
    private final LocalDateTime registeredAt;

    public Registration(Event event, String fullName, String email, int places, String needs) {
        this.event = event;
        this.fullName = fullName;
        this.email = email;
        this.places = places;
        this.needs = needs;
        this.registeredAt = LocalDateTime.now();
    }

    public Event getEvent() { return event; }
    public String getFullName() { return fullName; }
    public String getEmail() { return email; }
    public int getPlaces() { return places; }

    /** Dietary or access needs. Optional, so it may be empty. */
    public String getNeeds() { return needs; }

    public LocalDateTime getRegisteredAt() { return registeredAt; }
}
