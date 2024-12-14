package org.example.wordsearchgui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Scanner;

/**
 * WordSearch - A program to generate a word search puzzle grid from a given list of words and make
 * a game to search for the generated words.
 *
 * Instructions:
 * 1. Prepare an input file with the following format:
 *    - The first line should contain two integers separated by a space, representing the number of rows and columns in the grid.
 *    - Each line should contain a word to be placed in the grid.
 *
 * 2. Run the program by entering the file name in the project arguments.
 *
 * 3. Output:
 *    - The program will generate a word search grid in GUI.
 *    - The grid contains the words placed, with remaining spaces filled with random letters in green boxes.
 *
 * Example Workflow:
 *    1. The player is prompted to enter a word, its coordinates (x, y), and the direction (horizontal, vertical, diagonal).
 *    2. The word is removed from the grid (replaced with "*") and marked in red.
 *    3. Once all words are found, the program displays "All words found!" and ends the game.
 *
 * Author: Sherif Shawashen
 * CSC 210, Fall Semester 2024
 * WordSearch Project 9
 */
public class App extends Application {

    static String fileName; // Filename for the input file containing grid data and words.
    String wordEntered; // The word the user has entered.
    char xCoordinate; // The x-coordinate (row) of the word's starting position.
    char yCoordinate; // The y-coordinate (column) of the word's starting position.
    char direction; // The direction in which the word is placed: 'H' for horizontal, 'V' for vertical, 'D' for diagonal.
    int i = 0; // A counter for user input steps (1 to 4, after which the word is removed).
    int flag = 0; // A counter to track how many words remain to be found.

    @Override
    public void start(Stage stage) throws IOException {
        // Set the title of the window
        stage.setTitle("Word Search Game");

        // Open and read the input file specified by the user
        File file = new File(fileName);
        Scanner myReader = new Scanner(file);

        // Read the first line to get grid dimensions (rows and columns)
        String[] coordinates = myReader.nextLine().split(" ");
        int rows = Integer.parseInt(coordinates[0]); // Number of rows in the grid
        int cols = Integer.parseInt(coordinates[1]); // Number of columns in the grid

        // Create a new Grid object with the specified dimensions
        Grid myGridObject = new Grid(rows, cols);

        // List to hold the words that will be placed in the grid
        ArrayList<String> words = new ArrayList<>();

        // Read the rest of the file and extract words
        while (myReader.hasNextLine()) {
            String word = myReader.nextLine();
            words.add(word);
            flag++; // Increment the counter for the number of words to be placed in the grid
        }

        // Set and place the words into the grid
        myGridObject.setWords(words);
        myGridObject.placeWords();

        // Retrieve the grid as a 2D array (used to generate the visual representation)
        String[][] gridArray = myGridObject.getGridAsArray();

        // Create the GridView object for rendering the grid in GUI
        GridView gridView = new GridView(rows, cols, gridArray);

        // Create and set the scene with the grid UI
        Scene scene = gridView.drawGrid();
        stage.setScene(scene);
        stage.show();

        // Set up an event handler for key presses to capture user input
        GridView.textArea.setOnKeyPressed(event -> {
            // When the user presses Enter (ENTER key), handle different stages of input
            handleUserInput(event,gridView,myGridObject,words,gridArray);
        });
    }

    /**
     * Handles the user input when the ENTER key is pressed.
     * This method processes the input flow step by step to capture the word found, coordinates, and direction.
     * If all inputs are correct, it will apply the action to remove the word from the grid.
     *
     * @param event        The KeyEvent triggered by the user pressing a key (ENTER key).
     * @param gridView     The GridView object that represents the grid and the user interface.
     * @param myGridObject The Grid object that contains the logic for word placement and grid management.
     * @param words        The list of words that need to be found in the grid.
     * @param gridArray    The 2D array representation of the word search grid.
     */
    private void handleUserInput(KeyEvent event,GridView gridView, Grid myGridObject, ArrayList<String> words, String[][] gridArray) {
        if (event.getCode() == KeyCode.ENTER) {
            if (i == 0) {
                // Prompt for the word found
                gridView.displayMessage("Enter word found: ");
                i = 1; // Move to the next step
            } else if (i == 1) {
                // Capture the word entered
                wordEntered = gridView.captureWordFromInput();
                gridView.displayMessage("Enter x: ");
                i++; // Move to the next input stage
            } else if (i == 2) {
                // Capture the x-coordinate (row) of the word
                xCoordinate = gridView.captureWordFromInput().charAt(0);
                gridView.displayMessage("Enter y: ");
                i++; // Move to the next input stage
            } else if (i == 3) {
                // Capture the y-coordinate (column) of the word
                yCoordinate = gridView.captureWordFromInput().charAt(0);
                gridView.displayMessage("[H]orizontal [V]ertical [D]iagonal?: ");
                i++; // Move to the next input stage
            } else if (i == 4) {
                // Apply the user input: validate and remove the word from the grid if valid
                applyUserInput(gridView, myGridObject, words, gridArray);
            }
        }
    }

    /**
     * This method processes the user input after the coordinates and direction are entered.
     * It validates the word, updates the grid, and removes the word if found.
     *
     * @param gridView The GridView object for updating the GUI.
     * @param myGridObject The Grid object representing the internal state of the word search.
     * @param words The list of words to be found.
     * @param gridArray The 2D array representation of the grid.
     */
    private void applyUserInput(GridView gridView, Grid myGridObject, ArrayList<String> words, String[][] gridArray) {
        // Get the direction of the word from user input
        direction = gridView.captureWordFromInput().toUpperCase().charAt(0);

        // Convert coordinates from char to index (x-coordinate is already numeric, y-coordinate is converted to index)
        int xIndex = Character.getNumericValue(xCoordinate);
        int yIndex = (yCoordinate - 'a') + 1; // Convert letter to 1-based index (e.g., 'a' -> 1)

        // Validate whether the word is found at the specified location and direction
        Map<String, ArrayList<Integer>> gridMap = myGridObject.getGridMap();
        if (validateWord(wordEntered, xIndex, yIndex, direction, gridMap)) {
            // If the word is valid, remove it from the grid and list of words
            words.remove(wordEntered);
            removeWord(wordEntered, xIndex, yIndex, direction, gridArray, gridMap);
            gridView.setGrid(gridArray); // Update the grid in the GUI
            gridView.displayMessage("\n" + wordEntered + " removed\n\n");
            gridView.createGrid(); // Re-render the grid after removal
            flag--; // Decrease the number of remaining words
        } else {
            // If the word was not found, notify the user
            GridView.textArea.appendText("\n" + wordEntered + " not found\n\n");
        }

        i = 0; // Reset the input step counter

        // Check if all words have been found and display a completion message
        if (flag == 0) {
            gridView.displayMessage("\nAll words found!\n");
        }
    }

    /**
     * Validates whether a word is present in the grid at the specified coordinates and direction.
     *
     * @param word The word to validate.
     * @param x The x-coordinate (row) of the word.
     * @param y The y-coordinate (column) of the word.
     * @param direction The direction of the word ('H' for horizontal, 'V' for vertical, 'D' for diagonal).
     * @param gridMap The map that holds the words and their coordinates.
     * @return true if the word is found and correctly placed, false otherwise.
     */
    public static boolean validateWord(String word, int x, int y, char direction, Map<String, ArrayList<Integer>> gridMap) {
        // Loop through all words in the grid to check if the word is valid at the given coordinates
        for (String wordTest : gridMap.keySet()) {
            if (wordTest.equalsIgnoreCase(word)) {
                ArrayList<Integer> coordinates = gridMap.get(wordTest);

                // Check if the word's starting coordinates match the given ones
                if (coordinates.get(0) == x && coordinates.get(1) == y - 1) {
                    // Validate the direction of the word
                    return switch (direction) {
                        case 'H' -> // Horizontal validation
                                coordinates.get(2) == 0;
                        case 'V' -> // Vertical validation
                                coordinates.get(2) == 1;
                        case 'D' -> // Diagonal validation
                                coordinates.get(2) == 2;
                        default -> false;
                    };
                }
            }
        }
        return false; // Word not found or invalid location
    }

    /**
     * Removes a word from the grid by replacing its letters with "*".
     *
     * @param word The word to remove.
     * @param x The x-coordinate (row) of the word.
     * @param y The y-coordinate (column) of the word.
     * @param direction The direction of the word ('H', 'V', or 'D').
     * @param grid The grid as a 2D array.
     * @param gridMap The map of words and their coordinates.
     */
    public static void removeWord(String word, int x, int y, char direction, String[][] grid, Map<String, ArrayList<Integer>> gridMap) {
        x++; // Adjust for 1-based indexing
        switch (direction) {
            case 'H': // Remove horizontally
                for (int i = 0; i < word.length(); i++) {
                    grid[x][y + i] = "* ";
                }
                break;
            case 'V': // Remove vertically
                for (int i = 0; i < word.length(); i++) {
                    grid[x + i][y] = "* ";
                }
                break;
            case 'D': // Remove diagonally
                for (int i = 0; i < word.length(); i++) {
                    grid[x + i][y + i] = "* ";
                }
                break;
        }
    }

    public static void main(String[] args) throws FileNotFoundException {
        // Get the filename from the command-line arguments
        fileName = args[0];
        launch(); // Launch the JavaFX application
    }
}
