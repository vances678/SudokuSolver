
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
 * 
 * - Pick cells with least amount of choices (ex. (1 or 2) vs (2, 5, 7 or 8))
 * --- maybe (row options + column options + box options)?
 * 
 * - Do a lot of pruning (eliminate branches before continuing search)
 * --- row, col, box incorrect
 * 
 * 
 * If none of this works, can resort to backtracking with DFS
 */