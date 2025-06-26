package com.example.demo.model;

import algorithms.mazeGenerators.Maze;
import algorithms.mazeGenerators.Position;
import java.util.List;

/**
 * Logic-layer contract between the View-Model and any concrete model.
 * No JavaFX classes here — keeps the model GUI-agnostic.
 */
public interface IModel {

    /** define the dimensions the next maze should use */
    void setDimensions(int rows, int cols);

    /** generate a new maze (rows × cols) and keep it internally */
    void generateMaze(int rows, int cols);

    /** @return the last maze that was generated, or null if none */
    Maze getMaze();

    /** @return a list of positions representing a solution path, or null if no maze */
    List<Position> solveMaze();

    /* convenience getters */
    int getRows();
    int getCols();
}
