package model;

import java.util.Random;

/**
 * Generates NumberPairs with random letters.
 * 
 * @author Tony Jiang
 * 6-25-2015
 *
 */
public class NumberPairGenerator implements PairGenerator{
    
    /**
     * Number of numbers to choose from. 
     */
    static final int NUM_NUMBERS = 99;
    
    /**
     * Random number generator.
     */
    Random randomGenerator = new Random();

    /** The most recent NumberPair produced by NumberPairGenerator. */
    private NumberPair numberPair; 
    
    /** A measure of how many times the same side has been correct. */
    private int sameChoice;
    
    /** True if the last correct choice was left. False otherwise. */
    private boolean lastWasLeft;
    
    /**
     * Constructor. 
     */
//   (6-25-2015) sameChoice is set to 1 because the very first round
//   is not handled by the same instance of NumberPairGenerator as the
//   following rounds. (This should be changed, though)
    public NumberPairGenerator() {
        this.getNewPair();
        this.setSameChoice(1);
        this.setLastWasLeft(false);
    }
    
    /**
     * Gets a new NumberPair with random letters while
     * checking to make sure that the same choice will
     * not be picked more than three times in a row
     * as being correct.
     */
    public void getNewPair() {
        System.out.println(this.getSameChoice());
        int numberOne, numberTwo;
        numberOne = this.randomGenerator.nextInt(NUM_NUMBERS);
        do {
            numberTwo = this.randomGenerator.nextInt(NUM_NUMBERS); 
        } while (numberOne == numberTwo);        
           
        this.checkSameChoice(numberOne, numberTwo);

        if (this.getSameChoice() >= MAX_TIMES_SAME_ANSWER) {
            this.setReversePair(numberOne, numberTwo);
        } else {
            this.setNumberPair(new NumberPair(numberOne, numberTwo));
        }
    }
    
    /**
     * Occurs under the condition that the same side has been correct
     * for MAX_TIMES_SAME_ANSWER times in a row.
     * 
     * Set the NumberPair with the positions of the right and left letters
     * flipped as to what it would have otherwise been.
     * 
     * Toggles the lastWasLeft property because we are toggling the side
     * of which each component of the pair is being shown, so the opposite
     * side will be correct after setting the alpha pair in reverse order.
     * 
     * @param numberOne 
     * @param numberTwo
     */
    public void setReversePair(int numberOne, int numberTwo) {
        this.setNumberPair(new NumberPair(numberTwo, numberOne));
        this.toggleLastWasLeft();
        this.setSameChoice(0);
    }

    /**
     * Check if the same side is correct as the last round.
     * @param numberOne Position of first letter of current round.
     * @param numberTwo Position of right letter of current round.
     */
    public void checkSameChoice(int numberOne, int numberTwo) {
        if (numberOne > numberTwo) {
            if (this.lastWasLeft) {
                this.incrementSameChoice();
            } else {
                this.setSameChoice(0);
            }
            this.lastWasLeft = true;
        } else {
            if (!this.lastWasLeft) {
                this.incrementSameChoice();
            } else {
                this.setSameChoice(0);
            }
            this.lastWasLeft = false;
        }   
    }

    /**
     * Toggles which of the last choices was correct.
     */
    private void toggleLastWasLeft() {
        if (this.lastWasLeft) {
            this.lastWasLeft = false;
        } else {
            this.lastWasLeft = true;
        }
        
    }

    /**
     * Gets a new NumberPair with letters a certain distance apart.
     * @param difference distance between the letters.
     */
    public void getNewPair(int difference) {
        int numberOne, numberTwo;
        numberOne = this.randomGenerator.nextInt(NUM_NUMBERS - difference);
        numberTwo = numberOne + difference;
        
        //Swap the order
        int x = this.randomGenerator.nextInt(2);
        switch (x) {
            case 0: 
                this.setNumberPair(new NumberPair(numberTwo, numberOne));
                break;
            case 1:
                this.setNumberPair(new NumberPair(numberOne, numberTwo));
                break;
            default:
                break;
        }
    }

    public NumberPair getNumberPair() {
        return this.numberPair;
    }

    public void setNumberPair(NumberPair numberPair) {
        this.numberPair = numberPair;
    }

    public int getSameChoice() {
        return this.sameChoice;
    }

    public void setSameChoice(int sameChoice) {
        this.sameChoice = sameChoice;
    }

    public void incrementSameChoice() {
        this.sameChoice++;
    }
    
    public boolean isLastWasLeft() {
        return this.lastWasLeft;
    }

    public void setLastWasLeft(boolean lastWasLeft) {
        this.lastWasLeft = lastWasLeft;
    }
    
}
