
/**
 * solves sudoku puzzles with A*
 * 
 * credit to:
 * 
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
 * @version 2023/12/2
 */

public class SudokuSolver {
   public static void main(String args[]) {
      Board board = Board.random(4);
      SudokuGui sudokuGui = new SudokuGui(board);
      sudokuGui.showGui();
   }
}

/*
 * A-star ideas
 * --------------
 * - Weigh each number based on how many times it has already been used --> sort
 * cells highest weights first (ex. pick cell with 2 possible choices over 9)
 * 
 * - Do a lot of pruning (eliminate branches before continuing search)
 * --- row, col, box incorrect
 * 
 */