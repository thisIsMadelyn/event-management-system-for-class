package org.example.demo;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import org.example.demo.model.DataStore;
import org.example.demo.model.Event;
import org.example.demo.model.RegistrationResult;
import org.example.demo.model.RegistrationValidator;

import java.time.format.DateTimeFormatter;
import java.util.Locale;

/** W2 Event details and registration. */
public class DetailsController {
    private static final DateTimeFormatter DATE_FORMAT =
            DateTimeFormatter.ofPattern("EEEE d MMMM yyyy, HH:mm", Locale.ENGLISH);
    private static final DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ofPattern("HH:mm", Locale.ENGLISH);
    private static final int MAX_PLACES = 5;

    @FXML private Label titleLabel;
    @FXML private Label categoryLabel;
    @FXML private Label dateLabel;
    @FXML private Label locationLabel;
    @FXML private Label spacesLabel;
    @FXML private Label descriptionLabel;

    @FXML private VBox form;
    @FXML private TextField fullNameField;
    @FXML private TextField emailField;
    @FXML private ComboBox<Integer> placesBox;
    @FXML private TextArea needsArea;
    @FXML private Label fullNameError;
    @FXML private Label emailError;
    @FXML private Label placesError;
    @FXML private Button confirmButton;
    @FXML private Label feedbackLabel;

    private Event event;

    @FXML
    private void initialize() {
        for (int places = 1; places <= MAX_PLACES; places++) {
            placesBox.getItems().add(places);
        }
        placesBox.setValue(1);

        // In a text area Tab types a tab character. Here it moves to the next or previous control instead.
        needsArea.addEventFilter(KeyEvent.KEY_PRESSED, key -> {
            if (key.getCode() == KeyCode.TAB && !key.isControlDown()) {
                if (key.isShiftDown()) {
                    placesBox.requestFocus();
                } else {
                    confirmButton.requestFocus();
                }
                key.consume();
            }
        });
    }

    /** Shows the given event. Called by Navigator when it opens this screen. */
    public void setEvent(Event event) {
        this.event = event;
        titleLabel.setText(event.getTitle());
        categoryLabel.setText(event.getCategory().getLabel());
        dateLabel.setText("Date and time:  " + event.getStart().format(DATE_FORMAT)
                + " to " + event.getEnd().format(TIME_FORMAT));
        locationLabel.setText("Location:  " + event.getLocation());
        descriptionLabel.setText(event.getDescription());
        updateSpaces();
        if (event.isFull()) {
            closeForm();
        }
    }

    @FXML
    private void onConfirm() {
        showError(placesBox, placesError, null);
        boolean nameOk = showError(fullNameField, fullNameError,
                RegistrationValidator.validateFullName(fullNameField.getText()));
        boolean emailOk = showError(emailField, emailError,
                RegistrationValidator.validateEmail(emailField.getText()));
        if (!(nameOk && emailOk)) {
            return;
        }

        RegistrationResult result = DataStore.getInstance().register(event, fullNameField.getText().trim(),
                emailField.getText().trim(), placesBox.getValue(), needsArea.getText().trim());

        updateSpaces();
        switch (result) {
            case SUCCESS -> Navigator.showRegistrations(
                    "Success: You are registered for " + event.getTitle() + ".");
            case EVENT_FULL -> closeForm();
            case NOT_ENOUGH_PLACES -> showError(placesBox, placesError, notEnoughPlaces(event.getSeatsLeft()));
            case ALREADY_REGISTERED -> showError(emailField, emailError,
                    "This email address is already registered for this event.");
        }
    }

    /** Back and Cancel both return to the event list. */
    @FXML
    private void onBack() {
        Navigator.showBrowse();
    }

    private static String notEnoughPlaces(int left) {
        return left == 1
                ? "Only 1 place is left now. Choose 1 place or fewer."
                : "Only " + left + " places are left now. Choose " + left + " places or fewer.";
    }

    /** Shows or clears one field's error. Returns true when the field is valid. */
    private boolean showError(Node field, Label errorLabel, String message) {
        boolean valid = message == null;
        errorLabel.setText(valid ? "" : message);
        errorLabel.setVisible(!valid);
        errorLabel.setManaged(!valid);
        field.getStyleClass().remove("field-invalid");
        if (!valid) {
            field.getStyleClass().add("field-invalid");
        }
        return valid;
    }

    private void updateSpaces() {
        spacesLabel.setText("Spaces left:  " + event.getSeatsLeft());
    }

    /** The event is full: says so and stops further input. What was typed stays. */
    private void closeForm() {
        form.setDisable(true);
        confirmButton.setDisable(true);
        feedbackLabel.setText("Event full. Registration is closed for this event.");
        feedbackLabel.setVisible(true);
        feedbackLabel.setManaged(true);
    }
}
