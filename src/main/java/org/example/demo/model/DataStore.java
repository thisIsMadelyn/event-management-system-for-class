package org.example.demo.model;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * In-memory data for the app: the sample events and the current attendee's registrations.
 * Nothing is saved, so every run starts from the same sample events, dated relative to today.
 */
public class DataStore {
    private static final DataStore INSTANCE = new DataStore();

    private final List<Event> events = new ArrayList<>();
    private final List<Registration> registrations = new ArrayList<>();

    private DataStore() {
        loadSampleEvents(LocalDate.now());
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

    /** Publishes a new event with no places taken, in date order. Form validation happens in the W4 screen. */
    public Event addEvent(String title, Category category, LocalDateTime start, LocalDateTime end,
                          String location, String description, int capacity) {
        Event event = new Event(events.size() + 1, title, category, start, end, location, description, capacity, 0);
        events.add(event);
        events.sort(Comparator.comparing(Event::getStart));
        return event;
    }

    /**
     * Registers for an event. Refused, changing nothing, if this email is already registered for it,
     * if it is full, or if fewer places are left than asked for. Form validation happens in the W2 screen.
     */
    public RegistrationResult register(Event event, String fullName, String email, int places, String needs) {
        if (places < 1) {
            throw new IllegalArgumentException("places must be at least 1");
        }
        if (isRegistered(event, email)) {
            return RegistrationResult.ALREADY_REGISTERED;
        }
        if (event.isFull()) {
            return RegistrationResult.EVENT_FULL;
        }
        if (places > event.getSeatsLeft()) {
            return RegistrationResult.NOT_ENOUGH_PLACES;
        }
        event.takeSeat(places);
        registrations.add(new Registration(event, fullName, email, places, needs));
        return RegistrationResult.SUCCESS;
    }

    /** Cancels a registration and frees its places. Returns false if it was not registered. */
    public boolean cancel(Registration registration) {
        if (!registrations.remove(registration)) {
            return false;
        }
        registration.getEvent().releaseSeat(registration.getPlaces());
        return true;
    }

    private boolean isRegistered(Event event, String email) {
        for (Registration registration : registrations) {
            if (registration.getEvent() == event
                    && registration.getEmail().trim().equalsIgnoreCase(email.trim())) {
                return true;
            }
        }
        return false;
    }

    /** The day of the sample weekend events: the coming Saturday, or Sunday when today is in the weekend. */
    static LocalDate weekendDay(LocalDate today) {
        boolean weekend = today.getDayOfWeek() == DayOfWeek.SATURDAY || today.getDayOfWeek() == DayOfWeek.SUNDAY;
        return today.with(TemporalAdjusters.nextOrSame(weekend ? DayOfWeek.SUNDAY : DayOfWeek.SATURDAY));
    }

    private void loadSampleEvents(LocalDate today) {
        // The weekend events are in the evening, so they are usually still ahead when the app runs on a Sunday.
        LocalDate weekend = weekendDay(today);

        events.add(new Event(1, "Introduction to Screen Printing", Category.WORKSHOP,
                weekend.atTime(18, 0), weekend.atTime(20, 0), "Print Studio",
                "Design a simple stencil, mix your inks and print your own tote bag. No experience needed.",
                12, 7));
        events.add(new Event(2, "Pottery for Beginners", Category.WORKSHOP,
                weekend.atTime(18, 30), weekend.atTime(20, 30), "Ceramics Room",
                "Shape and decorate a small bowl by hand. Clay and tools are provided.",
                10, 4));
        events.add(new Event(3, "Neighbourhood Board Games Evening", Category.SOCIAL,
                weekend.atTime(19, 0), weekend.atTime(22, 0), "Main Hall",
                "Meet your neighbours over board games. Bring a favourite or try one of ours.",
                40, 39));
        events.add(new Event(4, "Local Food Systems Talk", Category.TALK,
                weekend.plusDays(4).atTime(18, 30), weekend.plusDays(4).atTime(20, 0), "Lecture Room",
                "How food reaches our tables, with growers and shop owners from the area.",
                60, 22));
        events.add(new Event(5, "Bike Repair Workshop", Category.WORKSHOP,
                weekend.plusDays(11).atTime(17, 30), weekend.plusDays(11).atTime(19, 30), "Courtyard",
                "Learn to fix a puncture, adjust brakes and look after a chain.",
                15, 15));
        events.add(new Event(6, "Photographing the City Talk", Category.TALK,
                weekend.plusDays(18).atTime(19, 0), weekend.plusDays(18).atTime(20, 30), "Lecture Room",
                "A photographer shares tips on light and composition, followed by questions.",
                50, 12));
    }
}
