
/**
 * solves sudoku puzzles with A*
 * 
 * credit to https://lipas.uwasa.fi/~timan/sudoku/
 * for txt files of sudoku puzzles and corresponding solutions
 * 
 * @author Vance Spears
 * @version 2023/12/2
 */

public class SudokuSolver {
   public static void main(String args[]) {
      Board board = Board.random(9);
      board.print();
      SudokuGui sudokuGui = new SudokuGui(board);
      sudokuGui.showGui();
   }
}
