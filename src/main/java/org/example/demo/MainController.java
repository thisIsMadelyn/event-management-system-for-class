package org.example.demo;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

/** The main window's navigation bar, the same on every screen. */
public class MainController {
    @FXML private Button browseButton;
    @FXML private Button registrationsButton;
    @FXML private Button createEventButton;

    @FXML
    private void onBrowse() {
        Navigator.showBrowse();
    }

    @FXML
    private void onRegistrations() {
        Navigator.showRegistrations();
    }

    @FXML
    private void onCreateEvent() {
        Navigator.showCreateEvent();
    }

    /** Marks Browse Events as the current section (W1 and W2). */
    void highlightBrowse() {
        highlight(browseButton);
    }

    /** Marks My Registrations as the current section (W3). */
    void highlightRegistrations() {
        highlight(registrationsButton);
    }

    /** Marks Create Event as the current section (W4). */
    void highlightCreateEvent() {
        highlight(createEventButton);
    }

    private void highlight(Button current) {
        for (Button button : new Button[]{browseButton, registrationsButton, createEventButton}) {
            button.getStyleClass().remove("nav-current");
        }
        current.getStyleClass().add("nav-current");
    }
}
