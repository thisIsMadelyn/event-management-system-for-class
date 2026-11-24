package org.example.demo;

import javafx.collections.FXCollections;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import org.example.demo.model.DataStore;
import org.example.demo.model.Event;
import org.example.demo.model.EventSearch;

import java.time.format.DateTimeFormatter;
import java.util.Locale;

/** W1 Browse events: the event list with search. */
public class BrowseController {
    private static final DateTimeFormatter DATE_FORMAT =
            DateTimeFormatter.ofPattern("EEE d MMM yyyy, HH:mm", Locale.ENGLISH);

    @FXML
    private TextField searchField;

    @FXML
    private ListView<Event> eventList;

    @FXML
    private void initialize() {
        FilteredList<Event> filtered =
                new FilteredList<>(FXCollections.observableArrayList(DataStore.getInstance().getEvents()));
        eventList.setItems(filtered);
        eventList.setCellFactory(list -> new EventCell());

        Label empty = new Label("No events found");
        empty.getStyleClass().add("empty-message");
        eventList.setPlaceholder(empty);

        searchField.textProperty().addListener((obs, oldText, newText) ->
                filtered.setPredicate(event -> EventSearch.matches(event, newText)));

        // Keyboard: Enter on the selected event opens it.
        eventList.setOnKeyPressed(key -> {
            if (key.getCode() == KeyCode.ENTER && eventList.getSelectionModel().getSelectedItem() != null) {
                Navigator.showDetails(eventList.getSelectionModel().getSelectedItem());
            }
        });
    }

    /** One row: title on top, then date and time, location and seats. Clicking it opens W2. */
    private static class EventCell extends ListCell<Event> {
        EventCell() {
            setOnMouseClicked(click -> {
                if (!isEmpty() && getItem() != null) {
                    Navigator.showDetails(getItem());
                }
            });
        }

        @Override
        protected void updateItem(Event event, boolean empty) {
            super.updateItem(event, empty);
            if (empty || event == null) {
                setGraphic(null);
                return;
            }
            Label title = new Label(event.getTitle());
            title.getStyleClass().add("event-title");

            Label date = new Label(event.getStart().format(DATE_FORMAT));
            Label location = new Label(event.getLocation());
            Region spacer = new Region();
            HBox.setHgrow(spacer, Priority.ALWAYS);
            Label seats = new Label(event.isFull() ? "Full" : event.getSeatsLeft() + " seats left");
            seats.getStyleClass().add(event.isFull() ? "seats-full" : "seats-left");

            HBox details = new HBox(16, date, location, spacer, seats);
            details.getStyleClass().add("event-details");

            setGraphic(new VBox(4, title, details));
        }
    }
}
