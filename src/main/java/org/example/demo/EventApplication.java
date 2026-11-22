package org.example.demo;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class EventApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader(EventApplication.class.getResource("main-view.fxml"));
        Scene scene = new Scene(loader.load(), 1000, 700);
        stage.setTitle("Event Management System");
        stage.setScene(scene);
        stage.show();
    }
}
