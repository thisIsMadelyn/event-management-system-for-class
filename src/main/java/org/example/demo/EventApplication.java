package org.example.demo;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;

public class EventApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader(EventApplication.class.getResource("main-view.fxml"));
        BorderPane root = loader.load();
        Navigator.init(root, loader.getController());
        Navigator.showBrowse();

        Scene scene = new Scene(root, 1280, 800);
        scene.getStylesheets().add(EventApplication.class.getResource("app.css").toExternalForm());
        stage.setTitle("Event Management System");
        stage.setScene(scene);
        stage.show();
    }
}
