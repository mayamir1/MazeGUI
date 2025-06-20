package com.example.demo.ViewModel;

import algorithms.mazeGenerators.Maze;
import com.example.demo.model.MyModel;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;

public class MyViewModel {
    private final StringProperty name = new SimpleStringProperty();
    private final StringProperty greeting = new SimpleStringProperty("Hello!");
    private final MyModel model = new MyModel();
    private final StringProperty rowsProperty = new SimpleStringProperty();
    private final StringProperty colsProperty = new SimpleStringProperty();

    private Maze maze;

    private int playerRow;
    private int playerCol;


    public MyViewModel() {
        // Whenever name changes, update greeting
        name.addListener((obs, oldVal, newVal) -> {
            greeting.set("Hello, " + newVal + "!");
        });
    }

    public Property<String> nameProperty() {
        return name;
    }

    public ObservableValue<String> greetingProperty() {
        return greeting;
    }

    public StringProperty rowsProperty() {
        return rowsProperty;
    }

    public StringProperty colsProperty() {
        return colsProperty;
    }

    public void generateMaze() {
        try {
            int rows = Integer.parseInt(rowsProperty.get());
            int cols = Integer.parseInt(colsProperty.get());
            model.generateMaze(rows, cols);

            maze = model.getMaze();
            resetPlayerPosition();
        } catch (NumberFormatException e) {
            System.out.println("Invalid input for rows or columns.");
        }
    }



    public MyModel getModel() {
        return model;
    }

    public Maze getMaze() {
        return model.getMaze();
    }

    public void resetPlayerPosition() {
        if (maze != null) {
            playerRow = maze.getStartPosition().getRowIndex();
            playerCol = maze.getStartPosition().getColumnIndex();
        }
    }


    public int getPlayerRow() {
        return playerRow;
    }

    public int getPlayerCol() {
        return playerCol;
    }

    public void moveUp()    { move(-1,  0); }
    public void moveDown()  { move( 1,  0); }
    public void moveLeft()  { move( 0, -1); }
    public void moveRight() { move( 0,  1); }

    private void move(int dRow, int dCol) {
        int newR = playerRow + dRow;
        int newC = playerCol + dCol;
        if (canMove(newR, newC)) {
            playerRow = newR;
            playerCol = newC;
        }
    }
    /** ➍  בדיקה האם תא (r,c) חוקי ואינו קיר */
    private boolean canMove(int r, int c) {
        if (maze == null) return false;
        int[][] m = maze.getMat();
        return r >= 0 && r < m.length &&
                c >= 0 && c < m[0].length &&
                m[r][c] == 0;              // ‎0 = דרך, ‎1 = קיר
    }


    public boolean isAtGoal() {
        if (maze == null) return false;
        return playerRow == maze.getGoalPosition().getRowIndex() &&
                playerCol == maze.getGoalPosition().getColumnIndex();
    }

}
