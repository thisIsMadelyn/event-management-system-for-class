package org.example.demo.model;

/** Outcome of DataStore.register, so the W2 screen can show the right feedback message. */
public enum RegistrationResult {
    SUCCESS,
    EVENT_FULL,
    NOT_ENOUGH_PLACES,
    ALREADY_REGISTERED
}
