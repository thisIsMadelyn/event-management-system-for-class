package org.example.demo;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

/** The main window's navigation bar, the same on every screen. */
public class MainController {
    @FXML private Button browseButton;
    @FXML private Button registrationsButton;

    @FXML
    private void onBrowse() {
        Navigator.showBrowse();
    }

    @FXML
    private void onRegistrations() {
        Navigator.showRegistrations();
    }

    /** Marks Browse Events as the current section (W1 and W2). */
    void highlightBrowse() {
        highlight(browseButton, registrationsButton);
    }

    /** Marks My Registrations as the current section (W3). */
    void highlightRegistrations() {
        highlight(registrationsButton, browseButton);
    }

    private void highlight(Button current, Button other) {
        other.getStyleClass().remove("nav-current");
        if (!current.getStyleClass().contains("nav-current")) {
            current.getStyleClass().add("nav-current");
        }
    }
}
