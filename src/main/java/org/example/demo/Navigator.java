package org.example.demo;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.BorderPane;
import org.example.demo.model.Event;
import org.example.demo.model.EventSearch;

import java.io.IOException;
import java.io.UncheckedIOException;

/**
 * Switches between the three screens inside the one main window and marks the current section in the bar.
 * Each screen is loaded fresh, so it always shows the current data (seats, registrations).
 */
public final class Navigator {
    private static BorderPane root;
    private static MainController bar;

    /** The last search in W1, kept so that coming back to W1 does not lose it. */
    private static EventSearch.Criteria browseCriteria = EventSearch.Criteria.ALL;

    private Navigator() {
    }

    static EventSearch.Criteria getBrowseCriteria() {
        return browseCriteria;
    }

    static void setBrowseCriteria(EventSearch.Criteria criteria) {
        browseCriteria = criteria;
    }

    public static void init(BorderPane mainRoot, MainController mainController) {
        root = mainRoot;
        bar = mainController;
    }

    /** W1 Browse events. */
    public static void showBrowse() {
        root.setCenter(load("browse-view.fxml").getRoot());
        bar.highlightBrowse();
    }

    /** W2 Event details and registration, for one event. Part of the Browse Events section. */
    public static void showDetails(Event event) {
        FXMLLoader loader = load("details-view.fxml");
        DetailsController controller = loader.getController();
        controller.setEvent(event);
        root.setCenter(loader.getRoot());
        bar.highlightBrowse();
    }

    /** W3 My registrations. */
    public static void showRegistrations() {
        root.setCenter(load("registrations-view.fxml").getRoot());
        bar.highlightRegistrations();
    }

    private static FXMLLoader load(String fxml) {
        FXMLLoader loader = new FXMLLoader(Navigator.class.getResource(fxml));
        try {
            loader.<Parent>load();
        } catch (IOException e) {
            throw new UncheckedIOException("Could not load " + fxml, e);
        }
        return loader;
    }
}
