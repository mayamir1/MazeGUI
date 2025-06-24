package com.example.demo.View;

import com.example.demo.ViewModel.MyViewModel;
import algorithms.mazeGenerators.Maze;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.util.Objects;
import java.util.Properties;

public class MyViewController {

    private final MyViewModel viewModel = new MyViewModel();
    public MenuBar menuBar;

    private Image playerImage;
    private Image wallImage;
    private Image goalImage;

    @FXML private Canvas mazeCanvas;
    @FXML private TextField nameField;
    @FXML private TextField rowsField;
    @FXML private TextField colsField;
    @FXML private Label greetingLabel;


    @FXML
    public void initialize() {
        playerImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/player.png")));
        wallImage   = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/wall.png")));
        goalImage   = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/goal.png")));

        nameField.textProperty().bindBidirectional(viewModel.nameProperty());
        rowsField.textProperty().bindBidirectional(viewModel.rowsProperty());
        colsField.textProperty().bindBidirectional(viewModel.colsProperty());
        greetingLabel.textProperty().bind(viewModel.greetingProperty());

        mazeCanvas.setFocusTraversable(true);
        mazeCanvas.setOnMouseClicked(e -> mazeCanvas.requestFocus());
        mazeCanvas.setOnKeyPressed(this::handleMove);
    }

    @FXML
    private void onGenerateMazeClicked() {
        viewModel.generateMaze();
        drawMaze();
        mazeCanvas.requestFocus();
    }

    private void handleMove(KeyEvent event) {
        switch (event.getCode()) {
            case UP    -> viewModel.moveUp();
            case DOWN  -> viewModel.moveDown();
            case LEFT  -> viewModel.moveLeft();
            case RIGHT -> viewModel.moveRight();
            default -> {
                event.consume();
                return;
            }
        }
        drawMaze();
        if (viewModel.isAtGoal()) {
            new Alert(Alert.AlertType.INFORMATION, "🎉 You reached the goal!").showAndWait();
            mazeCanvas.setOnKeyPressed(null);
        }
        event.consume();
    }

    private void drawMaze() {
        Maze maze = viewModel.getMaze();
        if (maze == null) return;

        int[][] mat = maze.getMat();
        double cellW = mazeCanvas.getWidth()  / mat[0].length;
        double cellH = mazeCanvas.getHeight() / mat.length;

        GraphicsContext gc = mazeCanvas.getGraphicsContext2D();
        gc.clearRect(0, 0, mazeCanvas.getWidth(), mazeCanvas.getHeight());

        /* ---  צובע את שבילי המבוך  --- */
        gc.setFill(Color.web("#ff9800"));          // ← כאן מחליפים מ-WHITE
        for (int r = 0; r < mat.length; r++)
            for (int c = 0; c < mat[0].length; c++)
                if (mat[r][c] == 0)                // תא שביל
                    gc.fillRect(c*cellW, r*cellH, cellW, cellH);

        /* --- ציור קירות (כמו שהיה) --- */
        for (int r = 0; r < mat.length; r++)
            for (int c = 0; c < mat[0].length; c++)
                if (mat[r][c] == 1)
                    gc.drawImage(wallImage, c*cellW, r*cellH, cellW, cellH);

        /* --- ציור מטרה ושחקן --- */
        int goalR = maze.getGoalPosition().getRowIndex();
        int goalC = maze.getGoalPosition().getColumnIndex();
        gc.drawImage(goalImage, goalC*cellW, goalR*cellH, cellW, cellH);

        gc.drawImage(playerImage,
                viewModel.getPlayerCol()*cellW,
                viewModel.getPlayerRow()*cellH,
                cellW, cellH);
    }


    @FXML

    private void onNewMaze(ActionEvent event) {
        nameField.clear();
        rowsField.clear();
        colsField.clear();
        greetingLabel.setText("");
        mazeCanvas.getGraphicsContext2D().clearRect(0, 0, mazeCanvas.getWidth(), mazeCanvas.getHeight());

        javafx.application.Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("New Maze");
            alert.setHeaderText(null);
            alert.setContentText("You are now creating a new maze.");
            alert.showAndWait();
        });
    }


    @FXML
    private void onSaveMaze(ActionEvent event) {
        File f = showSaveDialog("Save maze", "Maze file", "maze");
        if (f == null) return;          // המשתמש לחץ Cancel

        try (ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream(f))) {

            oos.writeObject(viewModel.getMaze());
            // --------------- הודעת הצלחה ---------------
            Alert ok = new Alert(Alert.AlertType.INFORMATION);
            ok.setTitle("Maze saved");
            ok.setHeaderText(null);
            ok.setContentText("✔ Maze saved successfully!");
            ok.showAndWait();
            //------------------------------------------------

        } catch (IOException ex) {
            ex.printStackTrace();
            new Alert(Alert.AlertType.ERROR,
                    "FAIL ON SAVED\n" + ex.getMessage()).showAndWait();
        }
    }

    /* ---------- Helpers ---------- */
// showSaveDialog נשאר – משתמשים בו בכפתור Save בלבד
    private File showSaveDialog(String title,
                                String extDesc, String ext) {

        FileChooser fc = new FileChooser();
        fc.setTitle(title);
        fc.getExtensionFilters().add(
                new FileChooser.ExtensionFilter(extDesc, "*." + ext)
        );
        fc.setInitialDirectory(
                new File(System.getProperty("user.home"))
        );
        return fc.showSaveDialog(
                mazeCanvas.getScene().getWindow()
        );
    }


    @FXML
    private void onExit(ActionEvent event) {
        System.exit(0);
    }

    @FXML
    private void onHelp(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Help");
        alert.setHeaderText("Game Instructions");
        alert.setContentText(
                "\uD83D\uDDFA Mission Overview:\n" +
                        "You are a special forces soldier navigating a maze to locate and destroy a nuclear reactor.\n\n" +
                        "\uD83C\uDFAE Controls:\n" +
                        "- Use the arrow keys to move (↑ ↓ ← →).\n" +
                        "- Avoid dead ends and traps.\n\n" +
                        "\uD83C\uDFAF Objective:\n" +
                        "Reach the hidden goal marked inside the maze. It represents the entrance to the enemy facility.\n" +
                        "When reached, the mission is complete.\n\n" +
                        "\uD83D\uDCA1 Tips:\n" +
                        "- Plan your path strategically.\n" +
                        "- Use 'New', 'Save', and 'Load' from the menu to manage your progress."
        );
        alert.showAndWait();
    }

    @FXML
    private void onAbout(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("About");
        alert.setHeaderText("Maze Mission: Operation Final Strike");
        alert.setContentText(
                "In this maze game, you play as an elite commando tasked with infiltrating a hostile area.\n" +
                        "Your objective: navigate the maze and destroy a nuclear reactor deep inside Iran.\n" +
                        "The goal tile represents the hidden entrance to the facility.\n" +
                        "Plan your path, avoid traps, and reach the destination to complete your mission.\n\n" +
                        "\uD83D\uDCD6 Project Info:\n" +
                        "This project was developed as part of a university GUI course.\n" +
                        "Developer: [Your Name]\nYear: 2025"
        );
        alert.showAndWait();
    }

    @FXML
    private void onBackToMenu() throws IOException {
        Stage stage = (Stage) menuBar.getScene().getWindow();
        Parent root = FXMLLoader.load(
                Objects.requireNonNull(getClass().getResource("/View/main-menu.fxml")));
        Scene scene = new Scene(root, 800, 600);
        scene.getStylesheets().add(
                Objects.requireNonNull(getClass().getResource("/View/style.css")).toExternalForm());
        stage.setScene(scene);
    }


}
