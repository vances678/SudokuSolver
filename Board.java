import java.awt.geom.Point2D;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Board {
   public int size;
   public int boxSize;
   public ArrayList<ArrayList<Character>> cells;
   public List<Character> validValues;
   public int cost;
   public ArrayList<Point2D> constraints;

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

   public Board(ArrayList<ArrayList<Character>> cells) {
      this(cells.get(0).size());
      this.updateCells(cells);
   }

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
                  // Replaces Q-Y with 1-9 and '.' with '0' for 16x16
                  if (boardSize == 25) {
                     if (value == '.') {
                        value = '0';
                     } else if (value == 'Q') {
                        value = '1';
                     } else if (value == 'R') {
                        value = '2';
                     } else if (value == 'S') {
                        value = '3';
                     } else if (value == 'T') {
                        value = '4';
                     } else if (value == 'U') {
                        value = '5';
                     } else if (value == 'V') {
                        value = '6';
                     } else if (value == 'W') {
                        value = '7';
                     } else if (value == 'X') {
                        value = '8';
                     } else if (value == 'Y') {
                        value = '9';
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

   public boolean areRowsValid() {
      for (int i = 0; i < this.size; i++) {
         if (!this.isRowValid(i)) {
            return false;
         }
      }
      return true;
   }

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

   public boolean areColumnsValid() {
      for (int i = 0; i < this.size; i++) {
         if (!this.isColumnValid(i)) {
            return false;
         }
      }
      return true;
   }

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
