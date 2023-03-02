
/**
 * Solves Sudoku puzzles with DFS
 * 
 * Credit to:
 * https://lipas.uwasa.fi/~timan/sudoku/
 * for txt files of 9x9 sudoku puzzles and corresponding solutions
 * 
 * https://www.sudoku-puzzles-online.com/hexadoku/enter-a-solution-hexadoku.php
 * for txt files of 16x16 sudoku puzzles
 * 
 * https://cboard.cprogramming.com/cplusplus-programming/117005-25x25-sudoku.html
 * for the 25x25 sudoku puzzle
 * 
 * @author Vance Spears
 * @version 2023/01/3
 */

public class SudokuSolver {
   public static void main(String args[]) {
      Board board = Board.random(INITIAL_BOARD_SIZE);
      SudokuGui sudokuGui = new SudokuGui(board);
      sudokuGui.showGui();
   }

   /** The size of the Sudoku board on the starting window */
   private static final int INITIAL_BOARD_SIZE = 9;
}