import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class SudokuGui {
   private Board board;
   private Board solutionBoard;
   private JTextField[] textFields;
   private JLabel[] labels;

   public SudokuGui(Board board) {
      this.board = board;
      this.textFields = new JTextField[board.size * board.size];
      this.labels = new JLabel[board.size * board.size];
   }

   public void showGui() {
      JFrame frame = createMainFrame();

      JPanel puzzlePanel = createCenteredPanel(createBoardPanel(false));
      frame.add(puzzlePanel);

      JPanel buttonPanel = createButtonPanel();
      frame.add(buttonPanel);

      JPanel solutionPanel = createCenteredPanel(createBoardPanel(true));
      frame.add(solutionPanel);

      frame.pack();
      frame.setVisible(true);
   }

   private void updateBoardData() {

      // initialize empty board to store rearranged textField values
      ArrayList<ArrayList<Character>> values = new ArrayList<ArrayList<Character>>();
      for (int i = 0; i < this.board.size; i++) {
         ArrayList<Character> row = new ArrayList<Character>();
         for (int j = 0; j < this.board.size; j++) {
            row.add('0');
         }
         values.add(row);
      }

      // rearrange textField values to board creation format
      for (int i = 0; i < textFields.length; i++) {
         int row = (int) Math.floor(i / this.board.boxSize) % this.board.boxSize
               + (int) Math.floor(i / (this.board.boxSize * this.board.size)) * this.board.boxSize;
         int col = (int) (Math.floor(i / this.board.size) * this.board.boxSize) % this.board.size
               + (i % this.board.boxSize);
         String text = textFields[i].getText();
         if (text.length() == 1) {
            values.get(row).set(col, text.charAt(0));
         }

      }

      this.board.updateCells(values);
   }

   private void updateLabels() {
      for (int row = 0; row < this.solutionBoard.boxSize; row++) {
         for (int col = 0; col < this.solutionBoard.boxSize; col++) {
            for (int i = 0; i < this.solutionBoard.size; i++) {
               int boardIndex = i
                     + col * this.solutionBoard.size
                     + row * this.solutionBoard.size * this.solutionBoard.boxSize;
               int smallRow = (int) Math.floor(boardIndex / this.solutionBoard.boxSize) % this.solutionBoard.boxSize
                     + (int) Math.floor(boardIndex / (this.solutionBoard.boxSize * this.solutionBoard.size))
                           * this.solutionBoard.boxSize;
               int smallCol = (int) (Math.floor(boardIndex / this.solutionBoard.size) * this.solutionBoard.boxSize)
                     % this.solutionBoard.size
                     + (boardIndex % this.solutionBoard.boxSize);
               labels[boardIndex].setText(String
                     .valueOf(this.solutionBoard.cells.get(smallRow).get(smallCol)));
            }
         }
      }
   }

   private JFrame createMainFrame() {
      JFrame frame = new JFrame("Sudoku Solver");
      frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
      frame.setMinimumSize(new Dimension(900, 300));
      frame.setPreferredSize(new Dimension(1350, 450));
      frame.setLayout(new GridLayout(1, 4, 20, 20));
      return frame;
   }

   private JPanel createButtonPanel() {
      JPanel mainButtonPanel = new JPanel(new GridLayout(1, 2, 20, 20));

      JPanel leftButtonPanel = new JPanel(new GridLayout(4, 1, 20, 20));
      JButton solveDFSButton = createButton("Solve (DFS)", new Color(195, 177, 225));
      solveDFSButton.addActionListener(e -> {
         if (board.size > 9) {
            JOptionPane.showMessageDialog(null, "Board too large to solve :(");
         } else {
            updateBoardData();
            System.out.println("----------------------");
            System.out.println("SOLVING WITH DFS");
            System.out.println("----------------------");
            solutionBoard = board.solveDFS();
            if (solutionBoard.equals(board)) {
               System.err.println("Board already solved or error solving board");
            } else {
               System.out.println("Input:");
               board.print();
               System.out.println();
               System.out.println("Solution:");
               solutionBoard.print();
               System.out.println("----------------------");
               updateLabels();
            }
         }
      });
      JButton checkButton = createButton("Check", new Color(233, 236, 107));
      checkButton.addActionListener(e -> {
         updateBoardData();
         board.checkValidity();
      });
      JButton clearButton = createButton("Clear", new Color(255, 105, 98));
      clearButton.addActionListener(e -> {
         for (JTextField textField : textFields) {
            textField.setText("");
         }
         updateBoardData();
      });
      leftButtonPanel.add(solveDFSButton);
      leftButtonPanel.add(checkButton);
      leftButtonPanel.add(clearButton);
      mainButtonPanel.add(leftButtonPanel);

      JPanel rightButtonPanel = new JPanel(new GridLayout(4, 1, 20, 20));
      JButton fourButton = createNewBoardButton(4);
      JButton nineButton = createNewBoardButton(9);
      JButton sixteenButton = createNewBoardButton(16);
      JButton twentyFiveButton = createNewBoardButton(25);
      rightButtonPanel.add(fourButton);
      rightButtonPanel.add(nineButton);
      rightButtonPanel.add(sixteenButton);
      rightButtonPanel.add(twentyFiveButton);
      mainButtonPanel.add(rightButtonPanel);

      return mainButtonPanel;
   }

   private JButton createNewBoardButton(int boardSize) {
      JButton button = createButton("New " + boardSize + "x" + boardSize, new Color(137, 207, 240));
      button.addActionListener(e -> {
         Board newBoard = Board.random(boardSize);
         SudokuGui newSudokuGui = new SudokuGui(newBoard);
         newSudokuGui.showGui();
      });
      return button;
   }

   private JButton createButton(String title, Color color) {
      JButton button = new JButton(title);
      button.setFocusPainted(false);
      button.setBackground(color);
      return button;
   }

   private JPanel createBoardPanel(boolean isSolution) {
      JPanel boardPanel = new JPanel(new GridLayout(this.board.boxSize, this.board.boxSize, 0, 0));
      for (int row = 0; row < this.board.boxSize; row++) {
         for (int col = 0; col < this.board.boxSize; col++) {
            JPanel boxPanel = new JPanel();
            boxPanel.setLayout(new GridLayout(this.board.boxSize, this.board.boxSize, 0, 0));
            boxPanel.setBorder(BorderFactory.createLineBorder(Color.black));
            for (int i = 0; i < this.board.size; i++) {
               int boardIndex = i
                     + col * this.board.size
                     + row * this.board.size * this.board.boxSize;
               if (isSolution) {
                  JLabel label = new JLabel();
                  label.setText("");
                  label.setHorizontalAlignment(SwingConstants.CENTER);
                  label.setBorder(BorderFactory.createLineBorder(Color.black));
                  labels[boardIndex] = label;
                  boxPanel.add(label);
               } else {
                  SudokuTextField textField = new SudokuTextField();
                  int smallRow = (int) Math.floor(boardIndex / this.board.boxSize) % this.board.boxSize
                        + (int) Math.floor(boardIndex / (this.board.boxSize * this.board.size)) * this.board.boxSize;
                  int smallCol = (int) (Math.floor(boardIndex / this.board.size) * this.board.boxSize) % this.board.size
                        + (boardIndex % this.board.boxSize);
                  textField.setText(String
                        .valueOf(this.board.cells.get(smallRow).get(smallCol)));
                  textField.setBorder(BorderFactory.createLineBorder(Color.BLACK));
                  textFields[boardIndex] = textField;
                  boxPanel.add(textField);
               }
            }
            boardPanel.add(boxPanel);
         }
      }
      return boardPanel;
   }

   private JPanel createCenteredPanel(JPanel childPanel) {
      JPanel gridPanel = new JPanel(new GridBagLayout());
      JPanel centerPanel = new JPanel(new GridLayout(1, 1)) {
         @Override
         public Dimension getPreferredSize() {
            Dimension size = this.getParent().getSize();
            int newSize = size.width > size.height ? size.height : size.width;
            return new Dimension(newSize, newSize);
         }
      };
      centerPanel.add(childPanel);
      gridPanel.add(centerPanel);
      return gridPanel;
   }
}