package org.example.demo.model;

import java.time.LocalDateTime;

/** One attendee's registration for one event, with the details entered in the W2 form. */
public class Registration {
    private final Event event;
    private final String fullName;
    private final String email;
    private final String phone;
    private final LocalDateTime registeredAt;

    public Registration(Event event, String fullName, String email, String phone) {
        this.event = event;
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
        this.registeredAt = LocalDateTime.now();
    }

    public Event getEvent() { return event; }
    public String getFullName() { return fullName; }
    public String getEmail() { return email; }
    public String getPhone() { return phone; }
    public LocalDateTime getRegisteredAt() { return registeredAt; }
}
