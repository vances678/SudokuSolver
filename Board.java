
/**
 * A class that represents a Sudoku board
 * 
 * @author Vance Spears
 * @version 2023/01/3
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Board {
   /** The size of the board's rows and columns */
   public int size;
   /** The size of the board's boxes */
   public int boxSize;
   /** The values of the board cells */
   public ArrayList<ArrayList<Character>> cells;
   /** All legal move values */
   public List<Character> validValues;

   /**
    * Creates a new empty board
    * 
    * @param boardSize The size of the board's rows and columns
    */
   public Board(int boardSize) {
      this.size = boardSize;
      this.boxSize = (int) Math.sqrt(boardSize);
      this.cells = new ArrayList<ArrayList<Character>>();
      for (int i = 0; i < boardSize; i++) {
         ArrayList<Character> row = new ArrayList<Character>();
         for (int j = 0; j < boardSize; j++) {
            row.add('0');
         }
         this.cells.add(row);
      }

      ArrayList<Character> possibleValues = new ArrayList<Character>();

      // Adds digits 1-9
      for (int i = 49; i < 58; i++) {
         possibleValues.add((char) i);
      }

      // Adds letters A-Z
      for (int i = 65; i < 91; i++) {
         possibleValues.add((char) i);
      }

      this.validValues = possibleValues.subList(0, boardSize);
   }

   /**
    * Creates a new board with specified cells
    * 
    * @param cells The board's cells
    */
   public Board(ArrayList<ArrayList<Character>> cells) {
      this(cells.get(0).size());
      this.updateCells(cells);
   }

   /**
    * Creates a new board from the values of a randomly chosen puzzle text file
    * 
    * @param boardSize The size of the board's rows and columns
    * @return
    */
   public static Board random(int boardSize) {
      ArrayList<ArrayList<Character>> cells = new ArrayList<ArrayList<Character>>();
      File folder = new File("puzzles/" + boardSize + "x" + boardSize);
      File[] puzzleFiles = folder.listFiles();
      try {
         File file = puzzleFiles[(int) (Math.random() * puzzleFiles.length)];
         Scanner reader = new Scanner(file);
         while (reader.hasNextLine()) {
            String line = reader.nextLine().replaceAll("[^*.0-9A-Z]*", "");
            if (!line.equals("")) {
               ArrayList<Character> row = new ArrayList<Character>();
               for (char value : line.toCharArray()) {
                  // Replaces '0' with 'G' and '*' with '0' for 16x16
                  if (boardSize == 16) {
                     if (value == '0') {
                        value = 'G';
                     } else if (value == '*') {
                        value = '0';
                     }
                  }
                  // Replaces Q-Y with 1-9 and '.' with '0' for 25x25
                  if (boardSize == 25) {
                     Map<Character, Character> characterReplacements = Map.of(
                           '.', '0',
                           'Q', '1',
                           'R', '2',
                           'S', '3',
                           'T', '4',
                           'U', '5',
                           'V', '6',
                           'W', '7',
                           'X', '8',
                           'Y', '9');
                     if (characterReplacements.containsKey(value)) {
                        value = characterReplacements.get(value);
                     }
                  }
                  row.add(value);
               }
               cells.add(row);
            }
         }
         reader.close();
      } catch (FileNotFoundException e) {
         System.out.println("Error accessing file.");
         e.printStackTrace();
      }

      return new Board(cells);
   }

   /**
    * Outputs the board to the console
    */
   public void print() {
      for (int i = 0; i < this.size; i++) {
         if (i % this.boxSize == 0 && i != 0) {
            System.out.println("-" + "--".repeat(this.boxSize - 1) + "--".repeat(this.size - 1));
         }
         for (int j = 0; j < this.size; j++) {
            if (j % this.boxSize == 0 && j != 0) {
               System.out.print("| ");
            }
            System.out.print(this.cells.get(i).get(j) + " ");
         }
         System.out.println();
      }
   }

   /**
    * Updates the board's cells
    * 
    * @param newCells The new board cells
    */
   public void updateCells(ArrayList<ArrayList<Character>> newCells) {
      if (newCells.size() == this.size && newCells.get(0).size() == this.size) {
         for (int r = 0; r < newCells.size(); r++) {
            ArrayList<Character> row = newCells.get(r);
            for (int c = 0; c < row.size(); c++) {
               this.cells.get(r).set(c, row.get(c));
            }
         }
      } else {
         System.err.println("Cannot update board cells: invalid input dimensions");
      }
   }

   /**
    * Checks if the board is valid
    * 
    * @return true if the board is valid (otherwise, false)
    */
   public boolean isValid() {
      ArrayList<Boolean> validity = new ArrayList<Boolean>();
      validity.add(this.areRowsValid());
      validity.add(this.areColumnsValid());
      validity.add(this.areBoxesValid());
      if (validity.get(0) && validity.get(1) && validity.get(2)) {
         return true;
      }
      return false;
   }

   /**
    * Checks and outputs the validity of the rows, columns, boxes, and entire board
    * to the console
    */
   public void checkValidity() {
      System.out.println();
      this.print();
      System.out.println();

      ArrayList<Boolean> validity = new ArrayList<Boolean>();
      validity.add(this.areRowsValid());
      validity.add(this.areColumnsValid());
      validity.add(this.areBoxesValid());
      System.out.println("Rows valid: " + validity.get(0));
      System.out.println("Columns valid: " + validity.get(1));
      System.out.println("Boxes valid: " + validity.get(2));
      System.out.println("----------------------");
      System.out.print("Board valid: ");
      if (validity.get(0) && validity.get(1) && validity.get(2)) {
         System.out.println(true);
      } else {
         System.out.println(false);
      }
   }

   /**
    * Checks if all boxes in the board are valid
    * 
    * @return true if all boxes are valid (otherwise, false)
    */
   public boolean areBoxesValid() {
      for (int i = 0; i < this.size; i += this.boxSize) {
         for (int j = 0; j < this.size; j += this.boxSize) {
            if (!this.isBoxValid(i, j)) {
               return false;
            }
         }
      }
      return true;
   }

   /**
    * Checks if the specified box is valid
    * 
    * @param startRow The index of the row where the box starts
    * @param startCol The index of the column where the box starts
    * @return true if the specified box is valid (otherwise, false)
    */
   public boolean isBoxValid(int startRow, int startCol) {
      ArrayList<Character> values = new ArrayList<Character>();
      for (int i = 0; i < this.boxSize; i++) {
         for (int j = 0; j < this.boxSize; j++) {
            int row = startRow + i;
            int col = startCol + j;
            char value = cells.get(row).get(col);
            if (this.validValues.contains(value) && !values.contains(value)) {
               values.add(value);
            } else {
               return false;
            }
         }
      }
      return true;
   }

   /**
    * Checks if all rows in the board are valid
    * 
    * @return true if all rows are valid (otherwise, false)
    */
   public boolean areRowsValid() {
      for (int i = 0; i < this.size; i++) {
         if (!this.isRowValid(i)) {
            return false;
         }
      }
      return true;
   }

   /**
    * Checks if the specified row is valid
    * 
    * @param rowIndex The index of the row to check
    * @return true if the specified row is valid (otherwise, false)
    */
   public boolean isRowValid(int rowIndex) {
      ArrayList<Character> values = new ArrayList<Character>();
      for (char value : cells.get(rowIndex)) {
         if (this.validValues.contains(value) && !values.contains(value)) {
            values.add(value);
         } else {
            return false;
         }
      }
      return true;
   }

   /**
    * Checks if all columns in the board are valid
    * 
    * @return true if all columns are valid (otherwise, false)
    */
   public boolean areColumnsValid() {
      for (int i = 0; i < this.size; i++) {
         if (!this.isColumnValid(i)) {
            return false;
         }
      }
      return true;
   }

   /**
    * Checks if the specified column is valid
    * 
    * @param columnIndex The index of the column to check
    * @return true if the specified column is valid (otherwise, false)
    */
   public boolean isColumnValid(int columnIndex) {
      ArrayList<Character> values = new ArrayList<Character>();
      for (ArrayList<Character> row : cells) {
         char value = row.get(columnIndex);
         if (this.validValues.contains(value) && !values.contains(value)) {
            values.add(value);
         } else {
            return false;
         }
      }
      return true;
   }

   /**
    * Checks if the board is full
    * 
    * @return true if all cells in the board have a value (otherwise, false)
    */
   public boolean isFull() {
      boolean isFull = true;
      for (ArrayList<Character> row : this.cells) {
         for (char c : row) {
            if (!validValues.contains(c)) {
               isFull = false;
            }
         }
      }
      return isFull;
   }

   /**
    * Checks if a move that has already been made is valid
    * 
    * @param moveRow The row of the move
    * @param moveCol The column of the move
    * @return true if the move is valid (otherwise, false)
    */
   public boolean isValidMove(int moveRow, int moveCol) {
      boolean isValidMove = true;
      char move = this.cells.get(moveRow).get(moveCol);

      // check row
      for (int c = 0; c < this.size; c++) {
         if (c != moveCol && move == this.cells.get(moveRow).get(c)) {
            isValidMove = false;
         }
      }

      // check column
      for (int r = 0; r < this.size; r++) {
         if (r != moveRow && move == this.cells.get(r).get(moveCol)) {
            isValidMove = false;
         }
      }

      // check box
      int startRow = this.boxSize * (moveRow / boxSize);
      int startCol = this.boxSize * (moveCol / boxSize);
      for (int i = 0; i < this.boxSize; i++) {
         for (int j = 0; j < this.boxSize; j++) {
            int row = startRow + i;
            int col = startCol + j;
            if (row != moveRow && col != moveCol && move == this.cells.get(row).get(col)) {
               isValidMove = false;
            }
         }
      }

      return isValidMove;
   }

   /**
    * Gets the neighboring boards
    * 
    * @return A list of neighbor boards
    */
   public ArrayList<Board> getNeighbors() {
      // fill first empty cell with all possible VALID values
      ArrayList<Board> neighbors = new ArrayList<Board>();
      for (int r = 0; r < this.cells.size(); r++) {
         ArrayList<Character> row = this.cells.get(r);
         for (int c = 0; c < row.size(); c++) {
            char value = row.get(c);
            if (value == '0') {
               for (char validValue : this.validValues) {
                  Board neighbor = new Board(this.cells);
                  neighbor.cells.get(r).set(c, validValue);
                  if (neighbor.isValidMove(r, c)) {
                     neighbors.add(neighbor);
                  }
               }
               return neighbors;
            }
         }
      }

      return new ArrayList<Board>();
   }

   /**
    * Solves the board with DFS
    * 
    * @return The solved board (or the starting board if no solution is found)
    */
   public Board solveDFS() {
      ArrayList<Board> queue = new ArrayList<Board>();
      queue.add(this);
      int boardsVisited = 0;

      while (!queue.isEmpty()) {
         Board currentBoard = queue.remove(0);
         boardsVisited++;

         if (currentBoard.isFull()) {
            if (currentBoard.isValid()) {
               System.out.println("Boards visited: " + boardsVisited);
               return currentBoard;
            }
         }

         ArrayList<Board> neighbors = currentBoard.getNeighbors();
         for (Board neighbor : neighbors) {
            queue.add(neighbor);
         }

      }

      return this;
   }
}
