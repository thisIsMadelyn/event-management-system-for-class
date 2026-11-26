package org.example.demo;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import org.example.demo.model.Category;
import org.example.demo.model.DataStore;
import org.example.demo.model.Event;
import org.example.demo.model.EventSearch;
import org.example.demo.model.EventSearch.Criteria;
import org.example.demo.model.EventSearch.DateFilter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/** W1 Browse events: the upcoming events, with search by keyword, category and date. */
public class BrowseController {
    private static final DateTimeFormatter DATE_FORMAT =
            DateTimeFormatter.ofPattern("EEE d MMM yyyy, HH:mm", Locale.ENGLISH);
    private static final String SEPARATOR = "  ·  ";
    /** At or below this many spaces left, the badge turns red (P1 shows "1 space left" in red). */
    private static final int FEW_SPACES = 3;

    @FXML private TextField searchField;
    @FXML private ComboBox<String> categoryBox;
    @FXML private ComboBox<DateFilter> dateBox;
    @FXML private ScrollPane eventScroll;
    @FXML private VBox eventList;
    @FXML private VBox emptyState;

    @FXML
    private void initialize() {
        categoryBox.getItems().add("All categories");
        for (Category category : Category.values()) {
            categoryBox.getItems().add(category.getLabel());
        }
        dateBox.getItems().setAll(DateFilter.values());
        show(Navigator.getBrowseCriteria());
    }

    @FXML
    private void onSearch() {
        // The first choice is "All categories", so the categories start at 1.
        int index = categoryBox.getSelectionModel().getSelectedIndex();
        Category category = index <= 0 ? null : Category.values()[index - 1];
        show(new Criteria(searchField.getText(), category, dateBox.getValue()));
    }

    @FXML
    private void onClear() {
        show(Criteria.ALL);
    }

    /** Fills in the filters, lists the matching events and remembers the search for the next visit. */
    private void show(Criteria criteria) {
        Navigator.setBrowseCriteria(criteria);
        searchField.setText(criteria.keyword());
        categoryBox.getSelectionModel().select(criteria.category() == null ? 0 : criteria.category().ordinal() + 1);
        dateBox.setValue(criteria.date());

        LocalDateTime now = LocalDateTime.now();
        eventList.getChildren().clear();
        for (Event event : DataStore.getInstance().getEvents()) {
            if (EventSearch.matches(event, criteria, now)) {
                eventList.getChildren().add(createRow(event));
            }
        }
        boolean empty = eventList.getChildren().isEmpty();
        setShown(emptyState, empty);
        setShown(eventScroll, !empty);
    }

    /** One row, as in P1: title over category, date and location, then a spaces-left badge and a button that opens W2. */
    private HBox createRow(Event event) {
        Label title = new Label(event.getTitle());
        title.getStyleClass().add("event-title");
        title.setWrapText(true);

        Label meta = new Label(String.join(SEPARATOR, event.getCategory().getLabel(),
                event.getStart().format(DATE_FORMAT), event.getLocation()));
        meta.getStyleClass().add("event-meta");
        meta.setWrapText(true);

        VBox info = new VBox(4, title, meta);
        info.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(info, Priority.ALWAYS);

        Label badge = new Label(spacesLeft(event));
        badge.getStyleClass().addAll("spaces-badge",
                event.getSeatsLeft() <= FEW_SPACES ? "spaces-few" : "spaces-ok");
        badge.setMinWidth(Region.USE_PREF_SIZE);

        Button details = new Button("View details");
        details.setAccessibleText("View details of " + event.getTitle());
        details.getStyleClass().addAll("primary", "view-details");
        details.setOnAction(e -> Navigator.showDetails(event));

        HBox row = new HBox(20, info, badge, details);
        row.setAlignment(Pos.CENTER_LEFT);
        row.getStyleClass().add("event-row");
        return row;
    }

    private static String spacesLeft(Event event) {
        if (event.isFull()) {
            return "Full";
        }
        return event.getSeatsLeft() == 1 ? "1 space left" : event.getSeatsLeft() + " spaces left";
    }

    private static void setShown(Node node, boolean shown) {
        node.setVisible(shown);
        node.setManaged(shown);
    }
}
