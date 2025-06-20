package com.example.demo.View;

import com.example.demo.ViewModel.MyViewModel;
import algorithms.mazeGenerators.Maze;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;          // ← חדש
import javafx.scene.paint.Color;

public class MyViewController {

    private final MyViewModel viewModel = new MyViewModel();

    private Image playerImage;
    private Image wallImage;
    private Image goalImage;               // ← חדש (ליעד)

    @FXML private Canvas     mazeCanvas;
    @FXML private TextField  nameField;
    @FXML private TextField  rowsField;
    @FXML private TextField  colsField;
    @FXML private Label      greetingLabel;

    /** ➊  מתודת initialise – כאן מוסיפים/משנים: */
    @FXML
    public void initialize() {

        //-- טעינת תמונות --
        playerImage = new Image(getClass().getResourceAsStream("/player.png"));
        wallImage   = new Image(getClass().getResourceAsStream("/wall.png"));
        goalImage   = new Image(getClass().getResourceAsStream("/goal.png"));   // ← הוסף קובץ goal.png ל-resources

        //-- קישור שדות ל-ViewModel --
        nameField.textProperty().bindBidirectional(viewModel.nameProperty());
        rowsField.textProperty().bindBidirectional(viewModel.rowsProperty());
        colsField.textProperty().bindBidirectional(viewModel.colsProperty());
        greetingLabel.textProperty().bind(viewModel.greetingProperty());

        //--  מפתח: לתת לקנבס פוקוס       --
        mazeCanvas.setFocusTraversable(true);                         // הקנבס “כשיר” לפוקוס
        mazeCanvas.setOnMouseClicked(e -> mazeCanvas.requestFocus()); // קליק = פוקוס
        mazeCanvas.setOnKeyPressed(this::handleMove);                 // מאזין למקשים
    }

    /** ➋ כפתור Generate */
    @FXML
    private void onGenerateMazeClicked() {
        viewModel.generateMaze();
        drawMaze();
        mazeCanvas.requestFocus();   // לאחר יצירת מבוך – פוקוס לקנבס
    }

    /*----------- מאזין למקשי-חצים + בדיקת-ניצחון -----------*/
    private void handleMove(KeyEvent event) {

        /* תרגום מקשי-חצים לפעולות ב-ViewModel */
        switch (event.getCode()) {
            case UP    -> viewModel.moveUp();
            case DOWN  -> viewModel.moveDown();
            case LEFT  -> viewModel.moveLeft();
            case RIGHT -> viewModel.moveRight();
            default    -> {                       // מקשים אחרים – מתעלמים
                event.consume();
                return;
            }
        }

        drawMaze();                               // מציירים אחרי תזוזה

        /* --- אם הגענו ליעד --- */
        if (viewModel.isAtGoal()) {
            new Alert(Alert.AlertType.INFORMATION, "🎉  You reached the goal!")
                    .showAndWait();

            mazeCanvas.setOnKeyPressed(null);     // מבטל תנועה עד Generate חדש
        }

        event.consume();                          // שלא יעבור לשדות-טקסט
    }


    /** ➍ ציור */
    private void drawMaze() {
        Maze maze = viewModel.getMaze();
        if (maze == null) return;

        int[][] mat = maze.getMat();
        double cellW = mazeCanvas.getWidth()  / mat[0].length;
        double cellH = mazeCanvas.getHeight() / mat.length;

        GraphicsContext gc = mazeCanvas.getGraphicsContext2D();
        gc.clearRect(0, 0, mazeCanvas.getWidth(), mazeCanvas.getHeight());

        /* ➊ קודם – רצפה לבנה לכל התאים הפנויים */
        gc.setFill(Color.WHITE);
        for (int r = 0; r < mat.length; r++)
            for (int c = 0; c < mat[0].length; c++)
                if (mat[r][c] == 0)
                    gc.fillRect(c*cellW, r*cellH, cellW, cellH);

        /* ➋ קירות (תמונות) */
        for (int r = 0; r < mat.length; r++)
            for (int c = 0; c < mat[0].length; c++)
                if (mat[r][c] == 1)
                    gc.drawImage(wallImage, c*cellW, r*cellH, cellW, cellH);

        /* ➌ יעד */
        int goalR = maze.getGoalPosition().getRowIndex();
        int goalC = maze.getGoalPosition().getColumnIndex();
        gc.drawImage(goalImage, goalC*cellW, goalR*cellH, cellW, cellH);

        /* ➍ שחקן */
        gc.drawImage(playerImage,
                viewModel.getPlayerCol()*cellW,
                viewModel.getPlayerRow()*cellH,
                cellW, cellH);
    }


}
