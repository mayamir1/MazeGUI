package com.example.demo.View;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.Map;
import java.util.Objects;

public class MainMenuController {

    /* ─────────────  שדה משורש ה-FXML (fx:id="menuRoot") ───────────── */
    @FXML private StackPane menuRoot;

    /* ─────────────  New Game – מעביר למסך המֵייז  ───────────── */
    @FXML
    private void onNewGame() throws Exception {
        Stage stage = (Stage) menuRoot.getScene().getWindow();

        Parent root = FXMLLoader.load(
                Objects.requireNonNull(getClass().getResource("/View/hello-view.fxml")));
        Scene scene = new Scene(root, 800, 600);
        scene.getStylesheets().add(
                Objects.requireNonNull(getClass().getResource("/View/style.css")).toExternalForm());

        stage.setScene(scene);
    }

    /* ─────────────  Properties – חלון טבלה קטן  ───────────── */
    @FXML
    private void onProperties() throws Exception {

        // דוגמת ערכים (אפשר לקרוא מקובץ properties אמיתי בעתיד)
        Map<String,String> cfg = Map.of(
                "threadPoolSize",    "5",
                "mazeGeneratingAlg", "MyMazeGenerator",
                "mazeSearchingAlg",  "BreadthFirstSearch"
        );

        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/View/properties-dialog.fxml"));
        Parent root = loader.load();

        // מעביר Map לבקר החלון
        PropertiesController pc = loader.getController();
        pc.init(cfg);

        Stage dlg = new Stage();
        dlg.setTitle("Properties");
        dlg.setScene(new Scene(root));
        dlg.initOwner(menuRoot.getScene().getWindow());
        dlg.initModality(Modality.APPLICATION_MODAL);
        dlg.setResizable(false);
        dlg.showAndWait();
    }

    /* ─────────────  Help  ───────────── */
    @FXML
    private void onHelp() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Help");
        alert.setHeaderText("Gameplay Instructions");
        alert.setContentText(
                """
                🗺  Mission
                Navigate the maze, reach the goal tile, and destroy the reactor.
        
                🎮  Controls
                • Arrow keys – move the soldier
                • ESC – return to the main menu
        
                💡  Tips
                • Plan your path carefully.
                • You can save a generated maze and load it later.
                """
        );
        alert.showAndWait();
    }

    /* ----------  About  ---------- */
    @FXML
    private void onAbout() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("About");
        alert.setHeaderText("Maze Mission – Operation Final Strike");
        alert.setContentText(
                """
                A JavaFX maze game created as part of the GUI course at
                Ben-Gurion University of the Negev (2025).
        
                Developers
                • May Amir
                • Hila Sagi
        
                Maze–generation algorithm:
                • MyMazeGenerator  (DFS-based)
        
                Maze–solving algorithm:
                • Breadth-First Search
                """
        );
        alert.showAndWait();
    }


    /* ─────────────  Exit  ───────────── */
    @FXML
    private void onExit() {
        System.exit(0);
    }
}
