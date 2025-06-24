import javafx.event.ActionEvent;

//package View;
//
//import com.example.demo.ViewModel.MyViewModel;
//import javafx.fxml.FXML;
//import javafx.scene.canvas.Canvas;
//import javafx.scene.canvas.GraphicsContext;
//import javafx.scene.control.Label;
//import javafx.scene.control.TextField;
//import algorithms.mazeGenerators.Maze;
//import javafx.scene.image.Image;
//import javafx.scene.paint.Color;
//
//
//
//
//public class MyViewController {
//    private final MyViewModel viewModel = new MyViewModel();
//    Image playerImage = new Image(getClass().getResourceAsStream("/View/player.png"));
//    Image wallImage = new Image(getClass().getResourceAsStream("/View/wall.png"));
//
//    @FXML
//    private TextField rowsField;
//
//    @FXML
//    private TextField colsField;
//
//    @FXML
//    private TextField nameField;
//
//    @FXML
//    private Label greetingLabel;
//
//    @FXML
//    private Canvas mazeCanvas;
//
//    @FXML
//    public void initialize() {
//        nameField.textProperty().bindBidirectional(viewModel.nameProperty());
//        rowsField.textProperty().bindBidirectional(viewModel.rowsProperty());
//        colsField.textProperty().bindBidirectional(viewModel.colsProperty());
//        greetingLabel.textProperty().bind(viewModel.greetingProperty());
//        mazeCanvas.setFocusTraversable(true);
//        mazeCanvas.setOnKeyPressed(event -> {
//            int keyCode = event.getCode().getCode();
//            viewModel.movePlayer(keyCode);
//            drawMaze(); // לצייר מחדש עם המיקום החדש
//        });
//    }
//
//    @FXML
//    public void onGenerateMazeClicked() {
//        viewModel.generateMaze();
//        drawMaze();
//    }
//
//    private void drawMaze() {
//        Maze maze = viewModel.getMaze();
//        if (maze == null) return;
//
//        int[][] mazeMatrix = maze.getMat();
//        GraphicsContext gc = mazeCanvas.getGraphicsContext2D();
//        double cellWidth = mazeCanvas.getWidth() / mazeMatrix[0].length;
//        double cellHeight = mazeCanvas.getHeight() / mazeMatrix.length;
//
//        gc.clearRect(0, 0, mazeCanvas.getWidth(), mazeCanvas.getHeight());
//
//        // ציור קירות
//        for (int row = 0; row < mazeMatrix.length; row++) {
//            for (int col = 0; col < mazeMatrix[0].length; col++) {
//                if (mazeMatrix[row][col] == 1) {
//                    gc.drawImage(wallImage, col * cellWidth, row * cellHeight, cellWidth, cellHeight);
//                }
//            }
//        }
//
//        // ציור השחקן
//        int playerRow = viewModel.getPlayerRow();
//        int playerCol = viewModel.getPlayerCol();
//        gc.drawImage(playerImage, playerCol * cellWidth, playerRow * cellHeight, cellWidth, cellHeight);
//    }
//
//}


