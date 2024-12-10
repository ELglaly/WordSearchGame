package org.example.wordsearchgui;

import javafx.application.Application;
import javafx.event.EventHandler;
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
 * a game to search for the generated words
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
 *    - The grid contains the words placed, with remaining spaces filled with random letters in Green .
 *     box.
 *
 *  Example Workflow:
 *  *    a b c d e f g h i j
 *  * 00 P A C O A F W I T G
 *  * 01 A B C D C J B B B Y
 *  * 02 A G C J I G E B Z L
 *  * 03 A P C Q R X E B B H
 *  * 04 A T C L N D E B T P
 *  * 05 A F C L N I E B I N
 *  * 06 A B C Q E B E B T V
 *  * 07 A T C B I K E B I Y
 *  * 08 A D T P I K E B E T
 *  *
 *  *then the the player enter in the text area below the grid GUI
 *  * Enter word found: AAAAAAA
 *  * Enter x: 1
 *  * Enter y: d
 *  * [H]orizontal [V]ertical [D]iagonal? h
 *  *
 *  *  With each successful word information (word, location, and orientation) entered by the player,
 *  *  the word will be removed from the grid and replaced with * in a red box
 *  *  When all words have been found, print out All words found! and quit the game.
 *  *
 *  *  *
 *  *   auther: Sherif Shawashen
 *  *   CSC 210
 *  *   fall Semeter 2024
 *  *   WordSearch Project 9
 *  */

public class App extends Application {
    static String fileName;
    String wordEntered;
    char xCoordinate;
    char yCoordinate;
    char direction;
    int i=0;
    int flag = 0;
    @Override
    public void start(Stage stage) throws IOException {

        //Scene scene = new Scene( 320, 240);
        stage.setTitle("Hello!");
        File file = new File(fileName);
        Scanner myReader = new Scanner(file);

        // Read the first line to get grid dimensions (rows and columns)
        String[] coordinates = myReader.nextLine().split(" ");
        int rows = Integer.valueOf(coordinates[0]); // Number of rows in the grid
        int cols = Integer.valueOf(coordinates[1]); // Number of columns in the grid

        // Create a new grid with the specified dimensions
        Grid myGrid = new Grid(rows, cols);

        // List to hold the words to be placed in the grid
        ArrayList<String> words = new ArrayList<>();
         // Tracks the number of words to be found

        // Read the rest of the file to extract words
        while (myReader.hasNextLine()) {
            String word = myReader.nextLine();
            words.add(word);
            flag++;
        }

        // Set and place the words in the grid
        myGrid.setWords(words);
        myGrid.placeWords();

        // Print the grid to an output file
        String[][] grid2 = myGrid.printGrid();
        GridView gridView = new GridView(rows, cols, grid2);
        Scene scene = gridView.drawGrid();
        stage.setScene(scene);
        stage.show();
        //printAfterRemove(grid2, rows, cols);

        // Game loop to allow the user to find words
        // Flag controls the game loop

            // Event handler to capture user input

        GridView.textArea.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                if (i == 0) {
                    gridView.displayMessage("Enter word found: ");
                    i = 1;
                } else if (i == 1) {
                    wordEntered = gridView.captureWordFromInput();
                    gridView.displayMessage("Enter x: ");
                    i++;
                } else if (i == 2) {
                    xCoordinate = gridView.captureWordFromInput().charAt(0);
                    gridView.displayMessage("Enter y: ");
                    i++;
                } else if (i == 3) {
                    yCoordinate = gridView.captureWordFromInput().charAt(0);
                    gridView.displayMessage("[H]orizontal [V]ertical [D]iagonal?: ");
                    i++;
                } else if (i == 4) {
                    direction = gridView.captureWordFromInput().toUpperCase().charAt(0);
                    int xIndex = Character.getNumericValue(xCoordinate);  // Ensure xCoordinate is valid for this
                    int yIndex = (yCoordinate - 'a') + 1; // Convert to one-based index

                    // Validate the word's presence and orientation
                    Map<String, ArrayList<Integer>> gridMap = myGrid.getGridMap();
                    if (validateWord(wordEntered, xIndex, yIndex, direction, gridMap)) {
                        // Remove the word from the list and grid if valid
                        words.remove(wordEntered);
                        removeWord(wordEntered, xIndex, yIndex, direction, grid2, gridMap);
                        gridView.setGrid(grid2);
                        gridView.displayMessage("\n" + wordEntered + " removed\n\n");
                        gridView.createGrid();
                        flag--;  // Decrease the number of remaining words
                    } else {
                        // Notify the user if the word is not found
                        gridView.textArea.appendText("\n" + wordEntered + " not found\n\n");

                    }
                        i=0;

                    // Reset input counter if all words are found
                    if (flag == 0) {
                        gridView.displayMessage("\nAll words found!\n");
                    }
                }
            }
        });


        // Wait for the user to finish entering all four pieces of data (word, x, y, direction)
        }




//    /**
//     * Prints the current state of the grid.
//     */
//    public static void printAfterRemove(String[][] grid, int rows, int cols) {
//        for (int i = 0; i < rows + 1; i++) {
//            for (int j = 0; j < cols + 1; j++) {
//                System.out.print(grid[i][j]);
//            }
//            System.out.println();
//        }
//        System.out.println();
//    }

    /**
     * Validates if a word exists in the grid at the specified location and direction.
     */
    public static boolean validateWord(String word, int x, int y, char direction, Map<String, ArrayList<Integer>> gridMap) {
        for (String wordTest : gridMap.keySet()) {
            if (wordTest.equalsIgnoreCase(word)) {
                ArrayList<Integer> coordinates = gridMap.get(wordTest);

                if (coordinates.get(0) == x && coordinates.get(1) == y - 1) {
                    switch (direction) {
                        case 'H': // Horizontal check
                            return coordinates.get(2) == 0;
                        case 'V': // Vertical check
                            System.out.println(coordinates.get(2) );
                            return coordinates.get(2) == 1;
                        case 'D': // Diagonal check
                            return coordinates.get(2) == 2;
                        default:
                            return false;
                    }
                }
            }
        }
        return false;
    }

    /**
     * Removes a word from the grid by replacing its characters with '*'.
     */
    public static void removeWord(String word, int x, int y, char direction, String[][] grid, Map<String, ArrayList<Integer>> gridMap) {
        x++; // Adjust for internal indexing
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
        fileName = args[0];
        launch();
    }
}