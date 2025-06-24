package com.example.demo.model;

import algorithms.mazeGenerators.Maze;
import algorithms.mazeGenerators.MyMazeGenerator;
import algorithms.mazeGenerators.Position;
import algorithms.search.*;

import java.util.ArrayList;
import java.util.List;

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

    public List<Position> solveMaze() {
        if (maze == null) return null;

        Solution sol = new BestFirstSearch()
                .solve(new SearchableMaze(maze));

        List<Position> path = new ArrayList<>();
        for (AState s : sol.getSolutionPath()) {          // ← מגיע כ-AState
            MazeState ms = (MazeState) s;                 // cast
            path.add(ms.getPosition());                        // MazeState מחזיר Position
        }
        return path;
    }


}
