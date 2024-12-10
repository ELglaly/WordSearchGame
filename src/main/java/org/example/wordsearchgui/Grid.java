package org.example.wordsearchgui;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Grid {
    private int rows;
    private int cols;
    private char[][] grid;
    private ArrayList<String> words;
    private Boolean DigonalPlace=false;
    private final int[][] directions = {
            {0, 1}, {1, 0}, {1, 1},  // Horizontal, Vertical, Diagonal
    };
    Map<String,ArrayList<Integer>> gridMap=new HashMap<String,ArrayList<Integer>>();

    public Map<String, ArrayList<Integer>> getGridMap() {
        return gridMap;
    }

    /**
     * Constructor to initialize the grid with specified dimensions.
     * The grid is initially filled with spaces.
     *
     * @param rows Number of rows in the grid
     * @param cols Number of columns in the grid
     */
    public Grid(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        this.grid = new char[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                grid[i][j] = ' '; // fill the grid with empty spaces
            }
        }
    }

    /**
     * Fills the remaining empty spaces in the grid with random letters.
     */
    public void fillGridWithLetters() {
        Random random = new Random();
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if(grid[i][j]==' ') // Only replace empty space
                    grid[i][j] = (char) ('A' + random.nextInt(26));  // Random uppercase letter
            }
        }
    }

    /**
     * Places all words in the word list into the grid.
     * Words are placed in random directions and positions.
     * Ensures at least one word is placed diagonally.
     */
    public void placeWords() {
        for (String word : words) {
            boolean placed = false;
            // Convert the word to uppercase
            word = word.toUpperCase();
            // Keep trying until the word is placed
            while (!placed) {
                Random random = new Random();
                int startRow = random.nextInt(rows);
                int startCol = random.nextInt(cols);
                // random direction
                int[] direction = directions[random.nextInt(directions.length)];
                // ensure at least one diagonal is placed
                if(!DigonalPlace)
                {
                    DigonalPlace=true;
                    direction[1] =1; // force diagonal direction
                    direction[0] =1;
                }
                // Attempt to place the word
                placed = checkPlaceWord(word,direction,startRow,startCol);
            }
        }
    }

    /**
     * Attempts to place a word into the grid at the specified position and direction.
     * @param word      The word to place
     * @param direction The direction to place the word ({rowIncrement, colIncrement})
     * @param startRow  The starting row
     * @param startCol  The starting column
     * @return True if the word was placed successfully; false otherwise
     */
    public boolean checkPlaceWord(String word, int[] direction, int startRow, int startCol) {
        // calculate the end position of the word
        int endRow = startRow + direction[0] * (word.length() - 1);
        int endCol = startCol + direction[1] * (word.length() - 1);
        // check if the word will go out of boundraies
        if (endRow >= rows || endCol >= cols) {
            return false; // Out of bounds
        }
        // check for conflicts with existing letters
        for (int i = 0; i < word.length(); i++) {
            int row = startRow + i * direction[0];
            int col = startCol + i * direction[1];
            if (grid[row][col] !=' ') {
                return false; // conflict
            }
        }
        // place the word in the grid
        ArrayList<Integer> letters = new ArrayList<Integer>();
        letters.add(startRow);
        letters.add(startCol);
        if(direction[0]==0)
        {
            letters.add(0); //Horizintal
        }
        else {
            if(direction[1]==0)
            {
                letters.add(1); // Vertical
            }
            else{
                letters.add(2); // Digonal
            }
        }
        gridMap.put(word,letters);
        for (int i = 0; i < word.length(); i++) {
            int row = startRow + i * direction[0];
            int col = startCol + i * direction[1];
            grid[row][col] = word.charAt(i);
        }
        return true;
    }

    /**
     * Sets the list of words to place in the grid.
     *
     * @param words A list of words
     */
    public void setWords(ArrayList<String> words) {
        this.words = words;
    }

    /**
     * Returns the list of words in the grid.
     *
     * @return The list of words
     */
    public ArrayList<String> getWords() {
        return words;
    }

    /**
     * Prints the grid to a file and fills empty spaces with random letters.
     *
     * @return The FileWriter object used to write the grid
     * @throws IOException If an error occurs during file writing
     */
    public String [][] printGrid()  {
        String grid2[][]=new String[rows+1][cols+1];
        fillGridWithLetters();
        for (int i = 0; i < rows+1; i++) {
            if(i==0)
            {
                grid2[i][0]="   ";
                for (int j = 1; j < cols+1; j++) {
                    grid2[i][j] = (char) ('a' + j - 1)+" ";
                }
                continue;
            }
            if(i-1<10)
            {
                grid2[i][0] = "0"+(i-1)+" ";}
            else {
                grid2[i][0] = (i-1)+" ";
            }

            for (int j = 1; j < cols+1; j++) {
                grid2[i][j] = String.valueOf(grid[i-1][j-1]+" ");
            }
        }
        return grid2;
    }




}