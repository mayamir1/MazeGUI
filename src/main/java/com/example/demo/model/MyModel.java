package com.example.demo.model;

import algorithms.mazeGenerators.Maze;
import algorithms.mazeGenerators.MyMazeGenerator;

public class MyModel {
    private Maze maze;
    private int rows;
    private int cols;

    public void generateMaze(int rows, int cols) {
        MyMazeGenerator generator = new MyMazeGenerator();
        maze = generator.generate(rows, cols);
        System.out.println("Maze generated:");
        maze.print(); // הדפסה לקונסול — בשלב מאוחר נחליף בהצגה ב־UI
    }

    public Maze getMaze() {
        return maze;
    }

    public void setDimensions(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
    }

    public int getRows() {
        return rows;
    }

    public int getCols() {
        return cols;
    }
}
