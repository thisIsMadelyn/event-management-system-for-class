package org.example.demo;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
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

    @FXML private Label titleLabel;
    @FXML private Label dateLabel;
    @FXML private Label locationLabel;
    @FXML private Label seatsLabel;
    @FXML private Label descriptionLabel;

    @FXML private VBox form;
    @FXML private TextField fullNameField;
    @FXML private TextField emailField;
    @FXML private Label fullNameError;
    @FXML private Label emailError;
    @FXML private Button registerButton;
    @FXML private Label feedbackLabel;

    private Event event;

    /** Shows the given event. Called by whoever opens this screen. */
    public void setEvent(Event event) {
        this.event = event;
        titleLabel.setText(event.getTitle());
        dateLabel.setText(event.getStart().format(DATE_FORMAT));
        locationLabel.setText(event.getLocation());
        descriptionLabel.setText(event.getDescription());
        updateSeats();

        if (event.isFull()) {
            form.setDisable(true);
            showFeedback("Event full. Registration is closed for this event.", "feedback-error");
        }
    }

    @FXML
    private void onRegister() {
        hideFeedback();
        boolean nameOk = showError(fullNameField, fullNameError,
                RegistrationValidator.validateFullName(fullNameField.getText()));
        boolean emailOk = showError(emailField, emailError,
                RegistrationValidator.validateEmail(emailField.getText()));

        if (!(nameOk && emailOk)) {
            showFeedback("Please correct the highlighted fields.", "feedback-error");
            return;
        }

        // One place and no needs for now: the W2 form does not ask for them yet.
        RegistrationResult result = DataStore.getInstance().register(event,
                fullNameField.getText().trim(), emailField.getText().trim(), 1, "");

        updateSeats();
        switch (result) {
            case SUCCESS -> {
                form.setDisable(true);
                showFeedback("You are registered for " + event.getTitle() + ".", "feedback-success");
            }
            case EVENT_FULL -> {
                form.setDisable(true);
                showFeedback("Event full. Registration is closed for this event.", "feedback-error");
            }
            case NOT_ENOUGH_PLACES -> showFeedback("Not enough places left.", "feedback-error");
            case ALREADY_REGISTERED -> showFeedback(
                    "This email address is already registered for this event.", "feedback-error");
        }
    }

    @FXML
    private void onBack() {
        Navigator.showBrowse();
    }

    @FXML
    private void onMyRegistrations() {
        Navigator.showRegistrations();
    }

    /** Shows or clears one field's error. Returns true when the field is valid. */
    private boolean showError(TextField field, Label errorLabel, String message) {
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

    private void updateSeats() {
        seatsLabel.setText(event.isFull()
                ? "Full (" + event.getCapacity() + " of " + event.getCapacity() + " seats taken)"
                : event.getSeatsLeft() + " of " + event.getCapacity() + " seats left");
    }

    private void showFeedback(String text, String styleClass) {
        feedbackLabel.setText(text);
        feedbackLabel.getStyleClass().removeAll("feedback-success", "feedback-error");
        feedbackLabel.getStyleClass().add(styleClass);
        feedbackLabel.setVisible(true);
        feedbackLabel.setManaged(true);
    }

    private void hideFeedback() {
        feedbackLabel.setVisible(false);
        feedbackLabel.setManaged(false);
    }
}
