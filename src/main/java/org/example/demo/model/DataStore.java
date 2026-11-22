package org.example.demo.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * In-memory data for the app: the sample events and the current attendee's registrations.
 * Nothing is saved, so every run starts from the same sample data.
 */
public class DataStore {
    private static final DataStore INSTANCE = new DataStore();

    private final List<Event> events = new ArrayList<>();
    private final List<Registration> registrations = new ArrayList<>();

    private DataStore() {
        loadSampleEvents();
    }

    public static DataStore getInstance() {
        return INSTANCE;
    }

    public List<Event> getEvents() {
        return Collections.unmodifiableList(events);
    }

    public List<Registration> getRegistrations() {
        return Collections.unmodifiableList(registrations);
    }

    /** Registers for an event. Refused if the event is full. Form validation happens in the W2 screen. */
    public RegistrationResult register(Event event, String fullName, String email, String phone) {
        if (event.isFull()) {
            return RegistrationResult.EVENT_FULL;
        }
        event.takeSeat();
        registrations.add(new Registration(event, fullName, email, phone));
        return RegistrationResult.SUCCESS;
    }

    /** Cancels a registration and frees its seat. Returns false if it was not registered. */
    public boolean cancel(Registration registration) {
        if (!registrations.remove(registration)) {
            return false;
        }
        registration.getEvent().releaseSeat();
        return true;
    }

    private void loadSampleEvents() {
        events.add(new Event(1, "UX Design Workshop",
                LocalDateTime.of(2026, 12, 10, 18, 0), "Athens",
                "A hands-on workshop on user research, wireframing and usability testing.",
                30, 12));
        events.add(new Event(2, "Tech Careers Fair",
                LocalDateTime.of(2026, 12, 12, 10, 0), "Thessaloniki",
                "Meet companies hiring graduates in software, data and design.",
                200, 143));
        events.add(new Event(3, "Intro to JavaFX",
                LocalDateTime.of(2026, 12, 15, 17, 30), "Online",
                "A beginner session on building desktop interfaces with JavaFX.",
                50, 50));
        events.add(new Event(4, "Accessibility in Practice",
                LocalDateTime.of(2026, 12, 18, 16, 0), "Patras",
                "How to design and test interfaces that everyone can use.",
                40, 8));
        events.add(new Event(5, "Winter Hackathon",
                LocalDateTime.of(2027, 1, 16, 9, 0), "Athens",
                "A 24-hour team hackathon. Bring a laptop and an idea.",
                80, 61));
        events.add(new Event(6, "Photography Walk",
                LocalDateTime.of(2027, 1, 23, 11, 0), "Heraklion",
                "A guided walk through the old town, with tips on composition and light.",
                15, 3));
    }
}
