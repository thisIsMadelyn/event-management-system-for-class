package org.example.demo.model;

/** The kinds of event in the report's wireframes. */
public enum Category {
    WORKSHOP("Workshop"),
    TALK("Talk"),
    SOCIAL("Social");

    private final String label;

    Category(String label) {
        this.label = label;
    }

    /** The name shown on screen. */
    public String getLabel() { return label; }

    @Override
    public String toString() { return label; }
}
