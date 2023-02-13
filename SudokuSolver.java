
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
 * @author Vance Spears
 * @version 2023/12/2
 */

public class SudokuSolver {
   public static void main(String args[]) {
      Board board = Board.random(9);
      SudokuGui sudokuGui = new SudokuGui(board);
      sudokuGui.showGui();
   }
}

/*
 * A-star ideas
 * --------------
 * - Weigh each number based on how many times it has already been used --> sort
 * cells highest weights first
 * - Do a lot of pruning (eliminate branches before continuing search)
 * --- row, col, box incorrect
 */