import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;

public class Board {
   public int size;
   public int boxSize;
   public ArrayList<ArrayList<Character>> cells;
   public List<Character> validValues;

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

      this.validValues = possibleValues.subList(0, boardSize - 1);
   }

   public Board(ArrayList<ArrayList<Character>> cells) {
      this(cells.get(0).size());
      this.cells = cells;
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

   public void updateCells(ArrayList<ArrayList<Character>> cells) {
      if (cells.size() == this.size && cells.get(0).size() == this.size) {
         this.cells = cells;
      } else {
         System.err.println("Cannot update board cells: invalid input dimensions");
      }
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
}
