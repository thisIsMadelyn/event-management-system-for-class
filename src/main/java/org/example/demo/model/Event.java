package org.example.demo.model;

import java.time.LocalDateTime;

/** An event that attendees can register for. */
public class Event {
    private final int id;
    private final String title;
    private final LocalDateTime dateTime;
    private final String location;
    private final String description;
    private final int capacity;
    private int seatsTaken;

    public Event(int id, String title, LocalDateTime dateTime, String location,
                 String description, int capacity, int seatsTaken) {
        this.id = id;
        this.title = title;
        this.dateTime = dateTime;
        this.location = location;
        this.description = description;
        this.capacity = capacity;
        this.seatsTaken = seatsTaken;
    }

    public int getId() { return id; }
    public String getTitle() { return title; }
    public LocalDateTime getDateTime() { return dateTime; }
    public String getLocation() { return location; }
    public String getDescription() { return description; }
    public int getCapacity() { return capacity; }
    public int getSeatsTaken() { return seatsTaken; }

    public int getSeatsLeft() { return capacity - seatsTaken; }
    public boolean isFull() { return seatsTaken >= capacity; }

    /** Takes one seat. Only DataStore calls this, after checking isFull(). */
    void takeSeat() { seatsTaken++; }

    /** Frees one seat. Only DataStore calls this, when a registration is cancelled. */
    void releaseSeat() { seatsTaken--; }
}
