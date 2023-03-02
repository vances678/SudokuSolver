
/**
 * A text field extension for custom board input functionality
 * 
 * @author Vance Spears
 * @version 2023/01/3
 */

import java.awt.Component;
import java.awt.Container;
import java.awt.KeyboardFocusManager;
import java.awt.event.FocusEvent;
import java.awt.event.FocusEvent.Cause;
import java.awt.event.FocusListener;

import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

public class SudokuTextField extends JTextField {
   /**
    * Creates a new empty Sudoku text field
    */
   public SudokuTextField() {
      this.setHorizontalAlignment(SwingConstants.CENTER);
      AbstractDocument doc = (AbstractDocument) this.getDocument();
      doc.setDocumentFilter(new DocumentLengthFilter());
      this.addFocusListener(new FocusListener() {
         @Override
         public void focusGained(FocusEvent e) {
            if (e.getCause() == Cause.ACTIVATION) {
               // remove the focus gained when starting application
               KeyboardFocusManager
                     .getCurrentKeyboardFocusManager()
                     .clearFocusOwner();
            } else {
               // selects text when focus is gained
               selectAll();
            }
         }

         @Override
         public void focusLost(FocusEvent e) {
            // here because FocusListener requires this to be implemented
         }
      });
   }

   public class DocumentLengthFilter extends DocumentFilter {
      @Override
      public void insertString(
            DocumentFilter.FilterBypass fb,
            int offset,
            String text,
            AttributeSet attrs)
            throws BadLocationException {
         super.insertString(fb, offset, text, attrs);
      }

      @Override
      public void replace(
            DocumentFilter.FilterBypass fb,
            int offset,
            int length,
            String text,
            AttributeSet attrs)
            throws BadLocationException {
         // converts all input text to uppercase (ex. A instead of a)
         text = text.toUpperCase();

         // limits the length of text entry to 1 digit (1-9) or uppercase letter
         if (fb.getDocument().getLength() + text.length() - length == 1
               && text.matches("[1-9A-Z]")) {
            super.replace(fb, offset, length, text, attrs);

            // focuses next empty field after text entry
            focusNextEmptyField();

         } else if (text.equals("")) {
            // program is clearing text field, so focus should not switch.
            super.replace(fb, offset, length, text, attrs);
         }
      }

      /**
       * finds and switches focus to the next empty SudokuTextField
       */
      private void focusNextEmptyField() {
         boolean shouldStopSwitchingFocus = false;
         int counter = 0;
         Component focusOwner = KeyboardFocusManager
               .getCurrentKeyboardFocusManager()
               .getFocusOwner();
         if (focusOwner != null) {
            Container root = focusOwner.getFocusCycleRootAncestor();
            if (root != null) {
               while (!shouldStopSwitchingFocus) {
                  Component nextFocusOwner = root
                        .getFocusTraversalPolicy()
                        .getComponentAfter(root, focusOwner);
                  if (nextFocusOwner instanceof SudokuTextField) {
                     SudokuTextField textField = (SudokuTextField) nextFocusOwner;
                     if (textField.getText().equals("")) {
                        textField.requestFocus();
                        shouldStopSwitchingFocus = true;
                     } else {
                        focusOwner = nextFocusOwner;
                     }
                  } else {
                     focusOwner = nextFocusOwner;
                  }
                  if (counter > focusSwitchLimit) {
                     shouldStopSwitchingFocus = true;
                  }
                  counter++;
               }
            }
         }
      }
   }

   /**
    * the maximum number of focus switches allowed when searching for the next
    * empty text field
    */
   private int focusSwitchLimit = 999;
}