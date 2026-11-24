package org.example.demo.model;

import java.time.LocalDateTime;

/** An event that attendees can register for. */
public class Event {
    private final int id;
    private final String title;
    private final Category category;
    private final LocalDateTime start;
    private final LocalDateTime end;
    private final String location;
    private final String description;
    private final int capacity;
    private int seatsTaken;

    public Event(int id, String title, Category category, LocalDateTime start, LocalDateTime end,
                 String location, String description, int capacity, int seatsTaken) {
        this.id = id;
        this.title = title;
        this.category = category;
        this.start = start;
        this.end = end;
        this.location = location;
        this.description = description;
        this.capacity = capacity;
        this.seatsTaken = seatsTaken;
    }

    public int getId() { return id; }
    public String getTitle() { return title; }
    public Category getCategory() { return category; }
    public LocalDateTime getStart() { return start; }
    public LocalDateTime getEnd() { return end; }
    public String getLocation() { return location; }
    public String getDescription() { return description; }
    public int getCapacity() { return capacity; }
    public int getSeatsTaken() { return seatsTaken; }

    public int getSeatsLeft() { return capacity - seatsTaken; }
    public boolean isFull() { return seatsTaken >= capacity; }

    /** Takes seats. Only DataStore calls this, after checking that enough are left. */
    void takeSeat(int places) { seatsTaken += places; }

    /** Frees seats. Only DataStore calls this, when a registration is cancelled. */
    void releaseSeat(int places) { seatsTaken -= places; }
}
