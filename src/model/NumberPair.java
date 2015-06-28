package model;

/** 
 * Object to represent a pair of numbers ranging from 1-99.
 * 
 * @author Tony Jiang
 * 6-25-2015
 * 
 */
public class NumberPair {
    
    /** The first number. */
    private int numberOne;
    
    /** The second number */
    private int numberTwo;
    
    /** The distance between the numbers. */
    private int difference;
    
    /** Whether the left answer is correct or not. */
    private boolean leftCorrect;
    
    /** 
     * Constructor for AlphaPair.
     * @param posLetterOne The index of the first letter. A is 0, Z is 25.
     * @param posLetterTwo The index of the second letter.
     */
    public NumberPair(int numOne, int numTwo) {
        this.numberOne = numOne;
        this.numberTwo = numTwo;
        this.difference = numOne - numTwo;
        if (this.difference > 0) {
            this.setLeftCorrect(true);
        } else if (this.difference < 0) {
            this.setLeftCorrect(false);
        }
    }

    public int getNumberOne() {
        return this.numberOne;
    }

    public void setNumberOne(int numberOne) {
        this.numberOne = numberOne;
    }

    public int getNumberTwo() {
        return this.numberTwo;
    }

    public void setNumberTwo(int NumberTwo) {
        this.numberTwo = NumberTwo;
    }


    public int getDifference() {
        return this.difference;
    }


    public void setDifference(int difference) {
        this.difference = difference;
    }

    public boolean isLeftCorrect() {
        return this.leftCorrect;
    }

    public void setLeftCorrect(boolean leftCorrect) {
        this.leftCorrect = leftCorrect;
    }
    
}
