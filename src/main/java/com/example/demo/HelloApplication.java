package com.example.demo;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.URL;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        // טוען את קובץ ה־FXML
        URL fxmlLocation = getClass().getResource("/View/hello-view.fxml");

        // בדיקה חשובה: האם הקובץ באמת נמצא
        if (fxmlLocation == null) {
            System.err.println("❌ FXML file not found at /View/hello-view.fxml");
            System.exit(1); // מפסיק את התוכנית אם הקובץ לא קיים
        } else {
            System.out.println("✅ Loaded FXML from: " + fxmlLocation);
        }

        // טוען את הממשק
        Parent root = FXMLLoader.load(fxmlLocation);
        Scene scene = new Scene(root, 600, 400);
        stage.setTitle("Maze App");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
