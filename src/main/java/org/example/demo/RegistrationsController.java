package org.example.demo;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import org.example.demo.model.DataStore;
import org.example.demo.model.Registration;

import java.time.format.DateTimeFormatter;
import java.util.Locale;

/** W3 My registrations: the attendee's registrations, with cancel and confirmation. */
public class RegistrationsController {
    private static final DateTimeFormatter DATE_FORMAT =
            DateTimeFormatter.ofPattern("EEE d MMM yyyy, HH:mm", Locale.ENGLISH);

    @FXML private ListView<Registration> registrationList;
    @FXML private Label feedbackLabel;

    private final ObservableList<Registration> items = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        items.setAll(DataStore.getInstance().getRegistrations());
        registrationList.setItems(items);
        registrationList.setCellFactory(list -> new RegistrationCell());

        Label empty = new Label("You have no registrations yet.");
        empty.getStyleClass().add("empty-message");
        registrationList.setPlaceholder(empty);
    }

    @FXML
    private void onBack() {
        Navigator.showBrowse();
    }

    private void onCancel(Registration registration) {
        String title = registration.getEvent().getTitle();
        ButtonType yes = new ButtonType("Yes, cancel it", ButtonBar.ButtonData.OK_DONE);
        ButtonType no = new ButtonType("No, keep it", ButtonBar.ButtonData.CANCEL_CLOSE);

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, "", yes, no);
        confirm.setTitle("Cancel registration");
        confirm.setHeaderText("Cancel your registration for " + title + "?");
        confirm.setContentText("Your seat will be given up.");
        if (registrationList.getScene() != null) {
            confirm.initOwner(registrationList.getScene().getWindow());
            confirm.getDialogPane().getStylesheets().addAll(registrationList.getScene().getStylesheets());
        }

        if (confirm.showAndWait().orElse(no) == yes) {
            DataStore.getInstance().cancel(registration);
            items.remove(registration);
            feedbackLabel.setText("Your registration for " + title + " was cancelled.");
            feedbackLabel.setVisible(true);
            feedbackLabel.setManaged(true);
        }
    }

    /** One row: event title, date and location, the name registered with, and a Cancel button. */
    private class RegistrationCell extends ListCell<Registration> {
        @Override
        protected void updateItem(Registration registration, boolean empty) {
            super.updateItem(registration, empty);
            if (empty || registration == null) {
                setGraphic(null);
                return;
            }
            Label title = new Label(registration.getEvent().getTitle());
            title.getStyleClass().add("event-title");

            Label details = new Label(registration.getEvent().getStart().format(DATE_FORMAT)
                    + "    " + registration.getEvent().getLocation()
                    + "    Registered as " + registration.getFullName());
            details.getStyleClass().add("event-details");

            VBox text = new VBox(4, title, details);
            HBox.setHgrow(text, Priority.ALWAYS);

            Button cancel = new Button("Cancel");
            cancel.getStyleClass().add("cancel-button");
            cancel.setOnAction(e -> onCancel(registration));

            HBox row = new HBox(16, text, cancel);
            row.setStyle("-fx-alignment: center-left;");
            setGraphic(row);
        }
    }
}
