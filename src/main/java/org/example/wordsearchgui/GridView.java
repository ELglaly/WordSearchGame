package org.example.wordsearchgui;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

public class GridView {

    private static final int RECTANGLE_SIZE = 20; // Size of each rectangle for better visibility
    private static final int SPACING = 10; // Space between UI elements
    private static final int GRID_SPACING = 5; // Space between grid elements

    private int rows;
    private int cols;
    private String[][] grid;
    private VBox gridContainer;
    public static TextArea textArea;

    public GridView(int rows, int cols, String[][] grid) {
        this.rows = rows;
        this.cols = cols+1;
        this.grid = grid;
    }

    /**
     * Draws the main grid and input area as a JavaFX scene.
     *
     * @return The created scene.
     */
    public Scene drawGrid() {
        VBox root = new VBox(SPACING);
        root.setAlignment(Pos.CENTER);

        // Container for the grid
        gridContainer = new VBox(GRID_SPACING);
        gridContainer.setAlignment(Pos.CENTER);

        // Create the grid representation
        createGrid();

        // Text area for user input
        textArea = new TextArea();
        textArea.setPromptText("Click Enter to Start...");
        textArea.setFocusTraversable(false);
        root.getChildren().addAll(gridContainer, textArea);

        return new Scene(root);
    }

    /**
     * Creates the grid using the current state of the `grid` array.
     */
    public void createGrid() {
        gridContainer.getChildren().clear(); // Clear previous grid content
        for (int i = 0; i < rows+1; i++) {
            HBox row = new HBox(SPACING);
            row.setAlignment(Pos.CENTER);
            for (int j = 0; j < cols; j++) {
                StackPane stackPane = new StackPane();
                // Create a rectangle with specific size and color
                Rectangle rectangle = new Rectangle(RECTANGLE_SIZE, RECTANGLE_SIZE, Color.GREEN);
                rectangle.setFill("*".equals(grid[i][j].trim()) ? Color.RED : Color.GREEN);

                // Create a Text object to display the letter
                String letter = grid[i][j].trim();
                Text text = new Text(letter);

                // Style the text
                text.setFill(Color.WHITE); // Set text color (e.g., white for contrast)
                text.setStyle("-fx-font-size: 14;"); // Set font size and weight

                // Add the rectangle and text to the StackPane
                stackPane.getChildren().addAll(rectangle, text);

                // Optionally, you can center the text on the rectangle
                StackPane.setAlignment(text, Pos.CENTER);

                // Add the StackPane to the rowBox
                row.getChildren().add(stackPane);
            }
            gridContainer.getChildren().add(row);
        }

        }

    /**
     * Captures a word or input line from the user via the text area.
     *

     * @return The user's entered text.
     */
    public String captureWordFromInput() {
        String[] lines = textArea.getText().split("\n");
        // Return the last entered line
        return lines[lines.length - 1].split(":")[1].trim();
    }

    /**
     * Displays a message in the text area.
     *
     * @param message The message to display.
     */
    public void displayMessage(String message) {
        textArea.appendText(message);
    }

    /**
     * Updates the grid with a new state.
     *
     * @param updatedGrid The updated grid array.
     */
    public void updateGrid(String[][] updatedGrid) {
        this.grid = updatedGrid;
        createGrid();
    }

    public String[][] getGrid() {
        return grid;
    }

    public void setGrid(String[][] grid) {
        this.grid = grid;
    }
}
