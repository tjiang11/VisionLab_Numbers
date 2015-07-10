package model;

import java.util.Random;
/** 
 * Object to represent a pair of capital alphabetical letters.
 * 
 * @author Tony Jiang
 * 6-25-2015
 * 
 */
public class NumberPair {

    static final int SMALL_CHOICE = 0;
    static final int MEDIUM_CHOICE = 1;
    static final int BIG_CHOICE = 2;
    
    static final int SMALL_CHOICE_FONT_SIZE = 150;
    static final int MEDIUM_CHOICE_FONT_SIZE = 200;
    static final int BIG_CHOICE_FONT_SIZE = 300;
    
    static final double EASY_MODE_FONT_RATIO = .4;
    static final double MEDIUM_MODE_FONT_RATIO = .7;
    static final double HARD_MODE_FONT_RATIO = .85;
    
    /** The first letter. */
    private int numberOne;
    
    /** The second letter. */
    private int numberTwo;
    
    private int numberSizeOne;
    private int numberSizeTwo;
    
    /** The distance between the letters. */
    private int difference;
    
    /** Difficulty of this pair, determined by distance */
    private int difficulty;
    
    public int getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }

    /** Whether the left answer is correct or not. */
    private boolean leftCorrect;
    
    /** Random number generator */
    private Random randomGenerator = new Random();
    
    /** 
     * Constructor for NumberPair.
     * @param posNumberOne The index of the first letter. A is 0, Z is 25.
     * @param posNumberTwo The index of the second letter.
     * @param difficultyMode the difficulty of this pair, used to determine font sizes.
     */
    public NumberPair(int posNumberOne, int posNumberTwo, int difficultyMode) {
        this.difficulty = difficultyMode;
        this.numberOne = posNumberOne;
        this.numberTwo = posNumberTwo;
        this.setChoiceSizes(this.difficulty);
        this.difference = posNumberOne - posNumberTwo;
        if (this.difference > 0) {
            this.setLeftCorrect(true);
        } else if (this.difference < 0) {
            this.setLeftCorrect(false);
        }
    }
    
    /**
     * Set the font size for each letter based on the difficulty (which
     * is determined by the distance between the choices). Higher distances
     *  --> higher font ratio. Smaller distances --> smaller font ratio.
     * @param difficultyMode
     */
    public void setChoiceSizes(int difficultyMode) {
        int baseSize = this.chooseBaseSize();
        this.numberSizeOne = baseSize;
        switch (difficultyMode) {
        case NumberPairGenerator.EASY_MODE:
            this.numberSizeTwo = (int) (EASY_MODE_FONT_RATIO * baseSize);
            break;
        case NumberPairGenerator.MEDIUM_MODE:
            this.numberSizeTwo = (int) (MEDIUM_MODE_FONT_RATIO * baseSize);
            break;
        case NumberPairGenerator.HARD_MODE:
            this.numberSizeTwo = (int) (HARD_MODE_FONT_RATIO * baseSize);
            break;
        }
        
        int switchSizes = randomGenerator.nextInt(2);
        if (switchSizes == 0) {
            int tempSize = this.numberSizeOne;
            this.numberSizeOne = this.numberSizeTwo;
            this.numberSizeTwo = tempSize;
        }
    }
    
    /**
     * Determine the base font size to build the font ratio off of. The other
     * font size choice will be scaled down from this font size.
     * @return int The base font size.
     */
    public int chooseBaseSize() {
        int choiceOfSize = randomGenerator.nextInt(3);
        switch (choiceOfSize) {
        case SMALL_CHOICE:
            return SMALL_CHOICE_FONT_SIZE;
        case MEDIUM_CHOICE:
            return MEDIUM_CHOICE_FONT_SIZE;
        case BIG_CHOICE:
            return BIG_CHOICE_FONT_SIZE;
        }
        return 0;
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
