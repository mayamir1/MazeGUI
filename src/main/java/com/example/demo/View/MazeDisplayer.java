package com.example.demo.View;



import algorithms.mazeGenerators.Maze;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

public class MazeDisplayer extends Canvas {
    private Maze maze;
    private int playerRow;
    private int playerCol;
    private Image wallImage;
    private Image playerImage;

    public MazeDisplayer() {
        // ניתן להחליף לתמונות אחרות מתיקיית resources
        wallImage = new Image(getClass().getResourceAsStream("/wall.jpg"));
        playerImage = new Image(getClass().getResourceAsStream("/player.png"));
    }

    public void setMaze(Maze maze) {
        this.maze = maze;
        this.playerRow = maze.getStartPosition().getRowIndex();
        this.playerCol = maze.getStartPosition().getColumnIndex();
        redraw();
    }

    public void movePlayer(int dRow, int dCol) {
        int newRow = playerRow + dRow;
        int newCol = playerCol + dCol;

        if (maze != null && newRow >= 0 && newRow < maze.getMat().length &&
                newCol >= 0 && newCol < maze.getMat()[0].length &&
                maze.getCellValue(newRow, newCol) == 0) {

            playerRow = newRow;
            playerCol = newCol;
            redraw();
        }
    }

    public void redraw() {
        if (maze == null) return;

        double cellWidth = getWidth() / maze.getMat()[0].length;
        double cellHeight = getHeight() / maze.getMat().length;
        GraphicsContext gc = getGraphicsContext2D();

        gc.clearRect(0, 0, getWidth(), getHeight());

        int[][] matrix = maze.getMat();
        for (int row = 0; row < matrix.length; row++) {
            for (int col = 0; col < matrix[row].length; col++) {
                if (matrix[row][col] == 1) {
                    gc.drawImage(wallImage, col * cellWidth, row * cellHeight, cellWidth, cellHeight);
                }
            }
        }

        // Draw player
        gc.drawImage(playerImage, playerCol * cellWidth, playerRow * cellHeight, cellWidth, cellHeight);
    }

    public int getPlayerRow() {
        return playerRow;
    }

    public int getPlayerCol() {
        return playerCol;
    }
}

