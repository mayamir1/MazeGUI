package com.example.demo.View;

import algorithms.mazeGenerators.Maze;
import algorithms.mazeGenerators.Position;
import javafx.scene.input.KeyEvent;

import java.util.List;

/**
 * UI-contract for the View-Model layer.
 * No JavaFX imports here, so the model remains GUI-agnostic.
 */
public interface IView {

   void drawMaze();

   void initialize();

   void handleMove(KeyEvent event);

   void playVictory();
}
