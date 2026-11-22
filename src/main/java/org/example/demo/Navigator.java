package org.example.demo;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.BorderPane;
import org.example.demo.model.Event;

import java.io.IOException;
import java.io.UncheckedIOException;

/**
 * Switches between the three screens inside the one main window.
 * Each screen is loaded fresh, so it always shows the current data (seats, registrations).
 */
public final class Navigator {
    private static BorderPane root;

    private Navigator() {
    }

    public static void init(BorderPane mainRoot) {
        root = mainRoot;
    }

    /** W1 Browse events. */
    public static void showBrowse() {
        root.setCenter(load("browse-view.fxml").getRoot());
    }

    /** W2 Event details and registration, for one event. */
    public static void showDetails(Event event) {
        FXMLLoader loader = load("details-view.fxml");
        DetailsController controller = loader.getController();
        controller.setEvent(event);
        root.setCenter(loader.getRoot());
    }

    /** W3 My registrations. */
    public static void showRegistrations() {
        root.setCenter(load("registrations-view.fxml").getRoot());
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
