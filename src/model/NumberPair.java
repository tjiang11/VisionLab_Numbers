package model;

import java.util.Random;

/** 
 * Object to represent a pair of numbers.
 * 
 * @author Tony Jiang
 * 6-25-2015
 * 
 */
public class NumberPair {
    
//    static final int SMALL_LETTER = 0;
//    static final int MEDIUM_LETTER = 1;
//    static final int BIG_LETTER = 2;
//    
//    static final int SMALL_LETTER_FONT_SIZE = 100;
//    static final int MEDIUM_LETTER_FONT_SIZE = 200;
//    static final int BIG_LETTER_FONT_SIZE = 300;
    
    /** The first number. */
    private int numberOne;
    
    /** The second number. */
    private int numberTwo;
    
    private int numberSizeOne;
    private int numberSizeTwo;
    
    /** The distance between the numbers. */
    private int difference;
    
    /** Whether the left answer is correct or not. */
    private boolean leftCorrect;
    
    private Random randomGenerator = new Random();
    
    /** 
     * Constructor for NumberPair.
     * @param valNumberOne The value of the first number. 
     * @param valNumberTwo The value of the second number.
     */
    public NumberPair(int valNumberOne, int valNumberTwo) {
        this.numberOne = valNumberOne;
        this.numberTwo = valNumberTwo;
        this.difference = valNumberOne - valNumberTwo;
        if (this.difference > 0) {
            this.setLeftCorrect(true);
        } else if (this.difference < 0) {
            this.setLeftCorrect(false);
        }
        
        //SIZE VARIATION
        
        this.numberSizeOne = 100 * (randomGenerator.nextInt(3) + 1);
        this.numberSizeTwo = 100 * (randomGenerator.nextInt(3) + 1);
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

    public void setNumberTwo(int numberTwo) {
        this.numberTwo = numberTwo;
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

    public int getNumberSizeOne() {
        return numberSizeOne;
    }

    public void setNumberSizeOne(int numberSizeOne) {
        this.numberSizeOne = numberSizeOne;
    }

    public int getNumberSizeTwo() {
        return numberSizeTwo;
    }

    public void setNumberSizeTwo(int numberSizeTwo) {
        this.numberSizeTwo = numberSizeTwo;
    }
    
}
