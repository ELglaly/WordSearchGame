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

    private static final int RECTANGLE_SIZE = 20; // Size of each rectangle in the grid for better visibility
    private static final int SPACING = 10; // Space between UI elements (grid, text area, etc.)
    private static final int GRID_SPACING = 5; // Space between individual grid elements (cells)

    private int rows; // Number of rows in the grid
    private int cols; // Number of columns in the grid
    private String[][] grid; // 2D array that represents the word search grid
    private VBox gridContainer; // Container to hold the grid rows and cells in the UI
    public static TextArea textArea; // Text area for user input (where user enters word details)

    /**
     * Constructor to initialize the grid view with given grid data.
     *
     * @param rows Number of rows in the grid.
     * @param cols Number of columns in the grid.
     * @param grid 2D array representing the word search grid.
     */
    public GridView(int rows, int cols, String[][] grid) {
        this.rows = rows; // Set the number of rows
        this.cols = cols + 1; // Set the number of columns (the +1 accounts for 1-indexing in the grid)
        this.grid = grid; // Set the grid data
    }

    /**
     * Draws the main grid and input area as a JavaFX scene.
     *
     * @return The created scene with grid and text area.
     */
    public Scene drawGrid() {
        VBox root = new VBox(SPACING); // Main vertical container for the grid and text area
        root.setAlignment(Pos.CENTER); // Center align the root container

        // Container for the grid (holds all rows)
        gridContainer = new VBox(GRID_SPACING);
        gridContainer.setAlignment(Pos.CENTER); // Center align the grid container

        // Create the grid representation using the current grid data
        createGrid();

        // Text area for user input (e.g., entering word found, coordinates, and direction)
        textArea = new TextArea();
        textArea.setPromptText("Click Enter to Start..."); // Display a prompt message in the text area
        textArea.setFocusTraversable(false); // Prevent the text area from gaining focus by default

        // Add the grid container and text area to the root container
        root.getChildren().addAll(gridContainer, textArea);

        // Return the created scene
        return new Scene(root);
    }

    /**
     * Creates the grid using the current state of the `grid` array.
     * This method populates the `gridContainer` with rows of StackPanes (each containing a rectangle and text).
     */
    public void createGrid() {
        gridContainer.getChildren().clear(); // Clear previous grid content before creating the new grid

        // Loop through the rows of the grid
        for (int i = 0; i < rows + 1; i++) {
            HBox row = new HBox(SPACING); // Create a horizontal box for each row
            row.setAlignment(Pos.CENTER); // Center align the row

            // Loop through the columns in the current row
            for (int j = 0; j < cols; j++) {
                StackPane stackPane = new StackPane(); // StackPane to hold the rectangle and text for each cell

                // Create a rectangle to represent each cell in the grid
                Rectangle rectangle = new Rectangle(RECTANGLE_SIZE, RECTANGLE_SIZE, Color.GREEN);
                // Change color to RED if the cell contains the '*' symbol (word found)
                rectangle.setFill("*".equals(grid[i][j].trim()) ? Color.RED : Color.GREEN);

                // Create a text object to display the letter in the current grid cell
                String letter = grid[i][j].trim(); // Get the letter from the grid
                Text text = new Text(letter); // Create a Text node for the letter

                // Style the text (set color to white and adjust font size)
                text.setFill(Color.WHITE);
                text.setStyle("-fx-font-size: 14;");

                // Add both the rectangle and text to the StackPane (so they stack on top of each other)
                stackPane.getChildren().addAll(rectangle, text);

                // Align the text at the center of the rectangle
                StackPane.setAlignment(text, Pos.CENTER);

                // Add the StackPane to the row (which contains multiple cells)
                row.getChildren().add(stackPane);
            }

            // Add the row to the grid container (which holds all rows)
            gridContainer.getChildren().add(row);
        }
    }

    /**
     * Captures a word or input line from the user via the text area.
     *
     * @return The user's entered text (word or command).
     */
    public String captureWordFromInput() {
        String[] lines = textArea.getText().split("\n"); // Split the text by newlines
        // Return the last entered line (after the colon)
        return lines[lines.length - 1].split(":")[1].trim();
    }

    /**
     * Displays a message in the text area.
     *
     * @param message The message to display in the text area.
     */
    public void displayMessage(String message) {
        textArea.appendText(message); // Append the message to the text area
    }

    /**
     * Updates the grid with a new state.
     * This method is used to update the grid with new data after a word is found and removed.
     *
     * @param updatedGrid The updated grid array (with some words removed).
     */
    public void updateGrid(String[][] updatedGrid) {
        this.grid = updatedGrid; // Update the grid data
        createGrid(); // Re-create the grid UI with the updated data
    }

    // Setter for the grid (allows external classes to modify the grid data)
    public void setGrid(String[][] grid) {
        this.grid = grid;
    }
}
