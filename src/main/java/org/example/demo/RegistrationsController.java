package org.example.demo;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;
import org.example.demo.model.DataStore;
import org.example.demo.model.Registration;

import java.time.format.DateTimeFormatter;
import java.util.Locale;

/** W3 My registrations: the attendee's registrations, with cancel and confirmation. */
public class RegistrationsController {
    private static final DateTimeFormatter DATE_FORMAT =
            DateTimeFormatter.ofPattern("EEE d MMM yyyy, HH:mm", Locale.ENGLISH);
    private static final double ROW_HEIGHT = 40;
    /** The header row and the table's border. */
    private static final double HEADER_HEIGHT = 42;

    @FXML private Label feedbackLabel;
    @FXML private TableView<Registration> registrationTable;
    @FXML private TableColumn<Registration, String> eventColumn;
    @FXML private TableColumn<Registration, String> dateColumn;
    @FXML private TableColumn<Registration, String> locationColumn;
    @FXML private TableColumn<Registration, String> placesColumn;
    @FXML private TableColumn<Registration, Registration> actionColumn;
    @FXML private VBox emptyState;

    private final ObservableList<Registration> items = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        eventColumn.setCellValueFactory(row -> new ReadOnlyStringWrapper(row.getValue().getEvent().getTitle()));
        dateColumn.setCellValueFactory(row ->
                new ReadOnlyStringWrapper(row.getValue().getEvent().getStart().format(DATE_FORMAT)));
        locationColumn.setCellValueFactory(row -> new ReadOnlyStringWrapper(row.getValue().getEvent().getLocation()));
        placesColumn.setCellValueFactory(row ->
                new ReadOnlyStringWrapper(String.valueOf(row.getValue().getPlaces())));
        actionColumn.setCellValueFactory(row -> new ReadOnlyObjectWrapper<>(row.getValue()));
        actionColumn.setCellFactory(column -> new CancelCell());

        registrationTable.setFixedCellSize(ROW_HEIGHT);
        registrationTable.setItems(items);
        refresh();
    }

    @FXML
    private void onBrowse() {
        Navigator.showBrowse();
    }

    /** Shows a message in the banner above the list. */
    void showMessage(String message) {
        feedbackLabel.setText(message);
        feedbackLabel.setVisible(true);
        feedbackLabel.setManaged(true);
    }

    /** Lists the current registrations, or the empty state. The table is only as tall as its rows. */
    private void refresh() {
        items.setAll(DataStore.getInstance().getRegistrations());
        registrationTable.setMaxHeight(HEADER_HEIGHT + items.size() * ROW_HEIGHT);
        setShown(registrationTable, !items.isEmpty());
        setShown(emptyState, items.isEmpty());
    }

    private void onCancel(Registration registration) {
        String title = registration.getEvent().getTitle();
        if (confirmCancel(registration)) {
            DataStore.getInstance().cancel(registration);
            refresh();
            showMessage("Your registration for " + title + " was cancelled.");
        }
    }

    /** Asks once. Closing the dialog, Esc and Enter all keep the registration. */
    private boolean confirmCancel(Registration registration) {
        ButtonType keep = new ButtonType("Keep registration", ButtonBar.ButtonData.CANCEL_CLOSE);
        ButtonType cancel = new ButtonType("Cancel registration", ButtonBar.ButtonData.OTHER);
        String released = registration.getPlaces() == 1
                ? "Your place will be released for other people."
                : "Your " + registration.getPlaces() + " places will be released for other people.";

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, "Cancel your registration for "
                + registration.getEvent().getTitle() + "?\n" + released, keep, cancel);
        confirm.setTitle("Cancel registration");
        confirm.setHeaderText(null);
        confirm.setGraphic(null);
        confirm.initOwner(registrationTable.getScene().getWindow());

        DialogPane pane = confirm.getDialogPane();
        pane.getStylesheets().addAll(registrationTable.getScene().getStylesheets());
        pane.getStyleClass().add("confirm-dialog");
        // Keeps the wireframe's order: Keep registration first.
        ((ButtonBar) pane.lookup(".button-bar")).setButtonOrder(ButtonBar.BUTTON_ORDER_NONE);
        Button keepButton = (Button) pane.lookupButton(keep);
        keepButton.setDefaultButton(true);
        // P7 styling: Keep is outlined, Cancel registration is filled. Keep stays the default (Enter).
        keepButton.getStyleClass().add("secondary");
        pane.lookupButton(cancel).getStyleClass().add("primary");

        return confirm.showAndWait().orElse(keep) == cancel;
    }

    private static void setShown(Node node, boolean shown) {
        node.setVisible(shown);
        node.setManaged(shown);
    }

    /** The Action column: a button that cancels the row's registration. */
    private class CancelCell extends TableCell<Registration, Registration> {
        @Override
        protected void updateItem(Registration registration, boolean empty) {
            super.updateItem(registration, empty);
            if (empty || registration == null) {
                setGraphic(null);
                return;
            }
            Button cancel = new Button("Cancel registration");
            cancel.setAccessibleText("Cancel registration for " + registration.getEvent().getTitle());
            cancel.getStyleClass().add("secondary");
            cancel.setOnAction(e -> onCancel(registration));
            setGraphic(cancel);
        }
    }
}
