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


        URL fxmlLocation = getClass().getResource("/com/example/demo/View/main-menu.fxml");

        if (fxmlLocation == null) {
            System.err.println("FXML file not found at /com/example/demo/View/main-menu.fxml");
            System.exit(1);
        } else {
            System.out.println("Loaded FXML from: " + fxmlLocation);
        }

        Parent root = FXMLLoader.load(fxmlLocation);
        Scene scene = new Scene(root, 800, 600);

        /* CSS – נשאר אותו נתיב */
        URL cssLocation = getClass().getResource("/com/example/demo/View/style.css");
        System.out.println("CSS => " + cssLocation);

        if (cssLocation != null) {
            scene.getStylesheets().add(cssLocation.toExternalForm());
            System.out.println("Loaded CSS from: " + cssLocation);
        } else {
            System.err.println("style.css not found in /View/");
        }

        stage.setTitle("Maze App");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
