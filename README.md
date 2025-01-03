# WordSearch - A Word Search Puzzle Generator and Game

## Overview
WordSearch is a program designed to create a word search puzzle grid from a given list of words and provide an interactive game to search for the generated words. This project is developed as part of CSC 210 coursework during Fall 2024.

## Instructions

### Input Preparation
1. Create an input file with the following format:
   - The first line should contain two integers separated by a space, representing the number of rows and columns in the grid.
   - Each subsequent line should contain a word to be placed in the grid.

### Running the Program
1. Run the program by providing the file name as an argument in the project settings.
2. The program will generate a word search grid and display it in a graphical user interface (GUI).
3. The grid will contain:
   - Words placed based on the input.
   - Remaining spaces filled with random letters, highlighted in **green boxes**.

### Playing the Game
1. Enter the word you found in the text area below the grid GUI.
2. Provide the following information:
   - **Word Found**: The word you found.
   - **X Coordinate**: The row number of the word's starting position.
   - **Y Coordinate**: The column letter of the word's starting position.
   - **Orientation**: Specify the direction of the word ([H]orizontal, [V]ertical, [D]iagonal).
3. Upon successful entry:
   - The word will be removed from the grid and replaced with `*` in **red boxes**.
4. When all words have been found, the program will display the message: `All words found!` and terminate the game.

### Example Workflow
#### Input File Example:
```
10 10
apple
banana
cherry
date
eggfruit
```
#### Generated Grid Example:
```
   a b c d e f g h i j
00 P A C O A F W I T G
01 A B C D C J B B B Y
02 A G C J I G E B Z L
03 A P C Q R X E B B H
04 A T C L N D E B T P
05 A F C L N I E B I N
06 A B C Q E B E B T V
07 A T C B I K E B I Y
08 A D T P I K E B E T
```
#### Gameplay Example:
- Player enters:
  - **Enter word found**: `apple`
  - **Enter x**: `1`
  - **Enter y**: `d`
  - **Orientation**: `h`
- The word `apple` is removed and replaced with `*` in red boxes.

### Output
- Words placed in the grid.
- Words found by the player are marked and removed.
- Final message: `All words found!`

## Author
- **Name**: Sherif Shawashen
- **Course**: CSC 210
- **Semester**: Fall 2024
- **Project**: WordSearch Project 9

