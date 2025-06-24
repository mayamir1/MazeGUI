package com.example.demo.View;

import algorithms.mazeGenerators.Position;
import com.example.demo.ViewModel.MyViewModel;
import algorithms.mazeGenerators.Maze;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.util.List;
import java.util.Objects;

public class MyViewController {

    private final MyViewModel viewModel = new MyViewModel();
    public MenuBar menuBar;

    private Image playerImage;
    private Image wallImage;
    private Image goalImage;
    private boolean showSolution = false;


    @FXML private Canvas mazeCanvas;
    @FXML private TextField nameField;
    @FXML private TextField rowsField;
    @FXML private TextField colsField;
    @FXML private Label greetingLabel;
    @FXML private Button solveBtn;
    @FXML private BorderPane root;


    private MediaPlayer bgPlayer;
    private boolean victoryShown = false;   // דגל כדי שלא נפעיל פעמיים


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
        startBackgroundMusic();

    }

    @FXML
    private void onGenerateMazeClicked() {
        viewModel.generateMaze();
        solveBtn.setDisable(false);
        showSolution = false;
        drawMaze();
        mazeCanvas.requestFocus();
    }

    @FXML
    public void onSolveMazeClicked() {
        viewModel.solveMaze();          // מחשב פתרון במודל
        showSolution = true;            // עכשיו מותר לצייר
        drawMaze();
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
            new Alert(Alert.AlertType.INFORMATION, "You reached the goal!").showAndWait();
            mazeCanvas.setOnKeyPressed(null);
        }
        event.consume();
    }

    private void drawMaze() {
        Maze maze = viewModel.getMaze();
        if (maze == null) return;

        int[][] grid = maze.getMat();
        GraphicsContext gc = mazeCanvas.getGraphicsContext2D();

        // ========= החישוב צריך להיות לפני כל שימוש =========
        double cellWidth  = mazeCanvas.getWidth()  / grid[0].length;
        double cellHeight = mazeCanvas.getHeight() / grid.length;
        // =====================================================

        gc.clearRect(0, 0, mazeCanvas.getWidth(), mazeCanvas.getHeight());

        /* --- ציור קירות --- */
        for (int r = 0; r < grid.length; r++) {
            for (int c = 0; c < grid[0].length; c++) {
                if (grid[r][c] == 1) {
                    gc.drawImage(wallImage,
                            c * cellWidth, r * cellHeight,
                            cellWidth, cellHeight);
                }
            }
        }

        /* --- ציור פתרון (אם ביקשו) --- */
        if (showSolution) {
            List<Position> path = viewModel.getSolutionPath();
            if (path != null) {
                gc.setFill(javafx.scene.paint.Color.rgb(255, 0, 0, 0.5));
                for (Position p : path) {
                    gc.fillRect(p.getColumnIndex() * cellWidth,
                            p.getRowIndex()    * cellHeight,
                            cellWidth, cellHeight);
                }
            }
        }

        /* --- ציור שחקן --- */
        int pRow = viewModel.getPlayerRow();
        int pCol = viewModel.getPlayerCol();
        gc.drawImage(playerImage,
                pCol * cellWidth, pRow * cellHeight,
                cellWidth, cellHeight);

        int gRow = maze.getGoalPosition().getRowIndex();
        int gCol = maze.getGoalPosition().getColumnIndex();
        gc.drawImage(goalImage, gCol*cellWidth, gRow*cellHeight,
                cellWidth, cellHeight);

        if (!victoryShown &&
                viewModel.getPlayerRow() == viewModel.getGoalRow() &&
                viewModel.getPlayerCol() == viewModel.getGoalCol()) {

            victoryShown = true;
            playVictory();          // הפעלת מוזיקה + וידאו
        }
    }
    @FXML
    private void onJarMazeClicked() {
        algorithms.mazeGenerators.IMazeGenerator jarGen =
                new algorithms.mazeGenerators.SimpleMazeGenerator(); // מה־JAR

        algorithms.mazeGenerators.Maze mazeFromJar = jarGen.generate(10, 10);

        viewModel.setMaze(mazeFromJar);   // תוודאי שיש את המתודה הזו
        viewModel.solveMaze();

        showSolution = true;
        drawMaze();
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

    private void startBackgroundMusic() {
        Media bg = new Media(getClass().getResource("/View/sounds/bg.mp3").toExternalForm());
        bgPlayer = new MediaPlayer(bg);
        bgPlayer.setCycleCount(MediaPlayer.INDEFINITE); // לולאה
        bgPlayer.setVolume(0.6);
        bgPlayer.play();               // לא צריך MediaView אם לא מציגים וידאו
    }

    private void playVictory() {

        // עוצר מוזיקה קיימת
        if (bgPlayer != null) bgPlayer.stop();

        // קובץ ה-MP4 (עם סאונד) – יפעל פעם אחת
        Media media = new Media(getClass().getResource("/View/sounds/victory.mp4").toExternalForm());
        MediaPlayer mediaPlayer = new MediaPlayer(media);
        MediaView mediaView = new MediaView(mediaPlayer);

        mediaView.setPreserveRatio(true);
        mediaView.setFitWidth(root.getWidth());
        mediaView.setFitHeight(root.getHeight());

        Label victoryLabel = new Label("🎉 Mission Complete!");
        victoryLabel.setStyle("-fx-font-size: 48px; -fx-text-fill: yellow; -fx-font-weight: bold;");
        victoryLabel.setTranslateY(-50); // מעלה קצת את הכיתוב

        StackPane overlay = new StackPane(mediaView, victoryLabel);
        overlay.setStyle("-fx-background-color: black;"); // רקע אחיד
        overlay.setPrefSize(root.getWidth(), root.getHeight());

        root.setCenter(overlay);

        mediaPlayer.setOnEndOfMedia(() -> root.setCenter(mazeCanvas));
        mediaPlayer.play();
    }

    private void checkVictory(int playerRow, int playerCol, int goalRow, int goalCol) {
        if (playerRow == goalRow && playerCol == goalCol) {
            playVictory();
            showSolution = false; // or any logic
        }
    }



}
