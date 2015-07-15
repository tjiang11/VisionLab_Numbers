package model;

/** 
 * Object to represent a pair of numbers.
 * 
 * @author Tony Jiang
 * 6-25-2015
 * 
 */
public class NumberPair {
    
    /** The first number. */
    private int numberOne;
    
    /** The second number. */
    private int numberTwo;
    
    /** Font size of the first number */
    private int fontSizeOne;
    
    /** Font size of the second number */
    private int fontSizeTwo;
    
    /** The distance between the numbers. */
    private int difference;

    /** Whether the left answer is correct or not. */
    private boolean leftCorrect;
    
    /** 
     * Constructor for NumberPair.
     * @param posLetterOne The index of the first number. A is 0, Z is 25.
     * @param posLetterTwo The index of the second number.
     * @param fontSizeOne font size of the first number.
     * @param fontSizeTwo font size of the second number..
     */
    public NumberPair(int posLetterOne, int posLetterTwo, int fontSizeOne, int fontSizeTwo) {
    	this.numberOne = posLetterOne;
    	this.numberTwo = posLetterTwo;
        this.setFontSizeOne(fontSizeOne);
        this.setFontSizeTwo(fontSizeTwo);
        this.difference = posLetterOne - posLetterTwo;
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

	public int getFontSizeOne() {
		return fontSizeOne;
	}

	public void setFontSizeOne(int fontSizeOne) {
		this.fontSizeOne = fontSizeOne;
	}

	public int getFontSizeTwo() {
		return fontSizeTwo;
	}

	public void setFontSizeTwo(int fontSizeTwo) {
		this.fontSizeTwo = fontSizeTwo;
	}
}
