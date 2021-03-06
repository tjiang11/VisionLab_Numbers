package model;

import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

/**
 * Utility class for the game logic.
 * 
 * @author Tony Jiang
 * 6-25-2015
 *
 */
public final class GameLogic {
    
    /**
     * Checks whether subject's answer is correct or incorrect.
     * @param e The key event to check which key the user pressed.
     * @param ap The current NumberPair being evaluated.
     * @return correct True if correct, false otherwise.
     */
    public static boolean checkAnswerCorrect(KeyEvent e, NumberPair ap) {
        boolean correct;
        if ((ap.isLeftCorrect() && e.getCode() == KeyCode.F)
                || !ap.isLeftCorrect() && e.getCode() == KeyCode.J) {
            correct = true;
        } else {  
            correct = false;     
        } 
        return correct;
    }
}
