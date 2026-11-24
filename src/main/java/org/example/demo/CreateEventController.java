package org.example.demo;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import org.example.demo.model.Category;
import org.example.demo.model.DataStore;
import org.example.demo.model.Event;
import org.example.demo.model.EventSearch;
import org.example.demo.model.EventValidator;

import java.time.LocalDate;
import java.time.LocalDateTime;

/** W4 Create event: one form with which an organizer publishes an event. */
public class CreateEventController {
    @FXML private Label feedbackLabel;
    @FXML private TextField titleField;
    @FXML private ComboBox<Category> categoryBox;
    @FXML private TextField locationField;
    @FXML private TextArea descriptionArea;
    @FXML private TextField dateField;
    @FXML private TextField startField;
    @FXML private TextField endField;
    @FXML private TextField capacityField;
    @FXML private Label titleError;
    @FXML private Label categoryError;
    @FXML private Label locationError;
    @FXML private Label dateError;
    @FXML private Label startError;
    @FXML private Label endError;
    @FXML private Label capacityError;

    @FXML
    private void initialize() {
        categoryBox.getItems().setAll(Category.values());

        // In a text area Tab types a tab character. Here it moves to the next or previous control instead.
        descriptionArea.addEventFilter(KeyEvent.KEY_PRESSED, key -> {
            if (key.getCode() == KeyCode.TAB && !key.isControlDown()) {
                if (key.isShiftDown()) {
                    locationField.requestFocus();
                } else {
                    dateField.requestFocus();
                }
                key.consume();
            }
        });
    }

    @FXML
    private void onPublish() {
        feedbackLabel.setVisible(false);
        feedbackLabel.setManaged(false);

        LocalDateTime now = LocalDateTime.now();
        String date = dateField.getText();
        boolean valid = showError(titleField, titleError,
                EventValidator.validateRequired("Event title", titleField.getText()));
        valid &= showError(categoryBox, categoryError, EventValidator.validateCategory(categoryBox.getValue()));
        valid &= showError(locationField, locationError,
                EventValidator.validateRequired("Location", locationField.getText()));
        valid &= showError(dateField, dateError, EventValidator.validateDate(date, now.toLocalDate()));
        valid &= showError(startField, startError,
                EventValidator.validateStartTime(startField.getText(), date, now));
        valid &= showError(endField, endError,
                EventValidator.validateEndTime(endField.getText(), startField.getText()));
        valid &= showError(capacityField, capacityError, EventValidator.validateCapacity(capacityField.getText()));
        if (!valid) {
            return;
        }

        LocalDate day = EventValidator.parseDate(date);
        Event event = DataStore.getInstance().addEvent(titleField.getText().trim(), categoryBox.getValue(),
                day.atTime(EventValidator.parseTime(startField.getText())),
                day.atTime(EventValidator.parseTime(endField.getText())),
                locationField.getText().trim(), descriptionArea.getText().trim(),
                Integer.parseInt(capacityField.getText().trim()));

        // An earlier search in W1 must not hide the new event.
        Navigator.setBrowseCriteria(EventSearch.Criteria.ALL);
        Navigator.showCreateEvent("Success: " + event.getTitle() + " is now published and visible on Browse Events.");
    }

    /** Throws the draft away and returns to the event list. */
    @FXML
    private void onDiscard() {
        Navigator.showBrowse();
    }

    /** Shows a message in the banner above the form. */
    void showMessage(String message) {
        feedbackLabel.setText(message);
        feedbackLabel.setVisible(true);
        feedbackLabel.setManaged(true);
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
}
