package view;

import controller.NumberGameController;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * The graphical user interface of the game.
 * 
 * Classes Related to:
 *  -SetUp.java (utility/support)
 *      -SetUp is a utility class that contains functions that support the setup of GameGUI.
 *  -NumberGameController.java
 *      -The controller is created in this class. As the game progresses, the controller 
 *      adopts to the scenes of the GameGUI and sets their event handlers appropriately.
 *  
 * @author Tony Jiang
 * 6-25-2015
 * 
 */
public class GameGUI {
    
    /** Controller for setting event handlers */
    private NumberGameController NGC;

    /** The JavaFX stage for the game. */
    private Stage primaryStage;
    /** The current scene of the game. */
    private Scene scene;
    /** The pane of the game */
    private AnchorPane layout;
    
    /** Login Screen - start button. */
    private Button start;
    /** Login Screen - feedback to tell if user needs to
     * reinput their ID. */
    private Label feedback;
    /** Login Screen - Text field for user to enter their 
     * Subject ID. */
    private TextField enterId;   
    /** Login Screen - Toggle group for gender */
    private ToggleGroup pickGender;
    /** Login Screen - Radio Button "Male" */
    private RadioButton pickMale;
    /** Login Screen - Radio Button "Female" */
    private RadioButton pickFemale; 
    /** Login Screen - Text field for user to enter age. */ 
    private TextField enterAge;
    /** Login Screen - feedback to tell if user needs to correctly input their gender. */
    private Label feedbackGender;
    /** Login Screen - feedback to tell if user needs to correctly input their age. */
    private Label feedbackAge;
    /** Login Box to contain start button, feedback label, and enterId TextField. */
    private VBox loginBox;
    
    /** Instructions Screen - Next button */
    private Button next;
    
    /** Practice Complete screen */
    /** Label informing user has complete the practice trials. */
    private Text practiceComplete;
    /** Button to begin actual assessment. */
    private Button startAssessment;

    /** Game Screen - The left choice. */
    private Label leftOption;
    /** Game Screen - The right choice. */
    private Label rightOption;
    /** Game Screen - Progress Bar. */
    private ProgressBar progressBar;
    /** Game Screen - Get Ready */
    private Label getReady;
    /** Game Screen - Get Ready Bar */
    private ProgressBar getReadyBar;
    private VBox getReadyBox;
    /** Game Screen - Stars */
    private ImageView starNodes[];
    /** Game Screen - Practice label */
    private Label practice;
    
    /** End Screen - message informing the user has finished. */
    private Label congratulations;
    
    private VBox finishMessage;
    
    /** 
     * Constructor for the user interface. Sets the stage
     * and login screen.
     * @param stage The user interface stage.
     * @throws IOException 
     */
    public GameGUI(Stage stage) {
        NGC = new NumberGameController(this);
        this.setPrimaryStage(stage);
        this.layout = new AnchorPane();
        this.scene = new Scene(this.layout, SetUp.SCREEN_WIDTH, SetUp.SCREEN_HEIGHT);
        this.primaryStage.setScene(this.scene);
        this.primaryStage.setTitle("Number Game");  
        this.setLoginScreen();
        this.primaryStage.setResizable(false);
        this.primaryStage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
        this.primaryStage.setFullScreen(true);
        this.primaryStage.sizeToScene();
    }
    
    /**
     * Sets the login screen where user will input their informmation.
     * @param stage The user interface stage.
     * @throws IOException
     */
    private void setLoginScreen() {
        SetUp.setUpLoginScreen(this);
        NGC.setLoginHandlers(); 
    }

    /**
     * Sets the screen where instructions are shown.
     */
    public void setInstructionsScreen() {
        SetUp.setUpInstructionsScreen(this);
        this.NGC.setInstructionsHandlers();
    }
    
    /**
     * Sets the screen where user has finished practice trials and is about to begin assessment.
     */
    public void setPracticeCompleteScreen() {
        SetUp.setUpPracticeCompleteScreen(this);
        this.NGC.setPracticeCompleteHandlers();

    }

    /**
     * Sets the game screen where subject will be presented with two letters.
     * @param stage The user interface stage.
     * @param subjectID The subject's ID number.
     */
    public void setGameScreen() {
        SetUp.setUpGameScreen(this);          
        this.NGC.prepareFirstRound();
        this.NGC.setGameHandlers();
    }
    
    /** 
     * Sets the ending screen informing the subject of their completion.
     * @param stage The user interface stage.
     */
    public void setFinishScreen(int points, int level) {
        SetUp.setUpFinishScreen(this, points, level);
    }
    
    /**
     * Change the background in real time.
     */
    public void changeBackground(int level) { 
        SetUp.setBackground(this.layout, level);
        this.scene.setRoot(this.layout);
    }
    
    public Scene getScene() {
        return this.scene;
    }
    
    public void setScene(Scene s) {
        this.scene = s;
    }
    
    public Label getLeftOption() {
        return this.leftOption;
    }
    
    public void setLeftOption(Label label) {
        this.leftOption = label;
    }
    
    public void setRightOption(Label l) {
        this.rightOption = l;
    }
    
    public Label getRightOption() {
        return this.rightOption;
    }

    public Button getStart() {
        return start;
    }

    public void setStart(Button start) {
        this.start = start;
    }
    
    public void setEnterId(TextField t) {
        this.enterId = t;
    }
    
    public TextField getEnterId() {
        return this.enterId;
    }

    public Label getFeedback() {
        return this.feedback;
    }

    public void setFeedback(Label feedback) {
        this.feedback = feedback;
    }

    public Label getCongratulations() {
        return this.congratulations;
    }

    public void setCongratulations(Label congratulations) {
        this.congratulations = congratulations;
    }

    public Stage getPrimaryStage() {
        return this.primaryStage;
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public ProgressBar getProgressBar() {
        return progressBar;
    }

    public void setProgressBar(ProgressBar progressBar) {
        this.progressBar = progressBar;
    }

    public Label getGetReady() {
        return getReady;
    }

    public void setGetReady(Label getReady) {
        this.getReady = getReady;
    }

    public ProgressBar getGetReadyBar() {
        return getReadyBar;
    }

    public void setGetReadyBar(ProgressBar getReadyBar) {
        this.getReadyBar = getReadyBar;
    }

    public AnchorPane getLayout() {
        return layout;
    }

    public void setLayout(AnchorPane layout) {
        this.layout = layout;
    }

    public ImageView[] getStarNodes() {
        return starNodes;
    }

    public void setStarNodes(ImageView starNodes[]) {
        this.starNodes = starNodes;
    }

    public VBox getLoginBox() {
        return loginBox;
    }

    public void setLoginBox(VBox loginBox) {
        this.loginBox = loginBox;
    }

    public VBox getGetReadyBox() {
        return getReadyBox;
    }

    public void setGetReadyBox(VBox getReadyBox) {
        this.getReadyBox = getReadyBox;
    }

    public VBox getFinishMessage() {
        return finishMessage;
    }

    public void setFinishMessage(VBox finishMessage) {
        this.finishMessage = finishMessage;
    }

    public Button getNext() {
        return next;
    }

    public void setNext(Button next) {
        this.next = next;
    }

    public Label getPractice() {
        return practice;
    }

    public void setPractice(Label practice) {
        this.practice = practice;
    }
    
    public Text getPracticeComplete() {
        return practiceComplete;
    }

    public void setPracticeComplete(Text practiceComplete) {
        this.practiceComplete = practiceComplete;
    }

    public Button getStartAssessment() {
        return startAssessment;
    }

    public void setStartAssessment(Button startAssessment) {
        this.startAssessment = startAssessment;
    }

	public ToggleGroup getPickGender() {
		return pickGender;
	}

	public void setPickGender(ToggleGroup pickGender) {
		this.pickGender = pickGender;
	}

	public RadioButton getPickMale() {
		return pickMale;
	}

	public void setPickMale(RadioButton pickMale) {
		this.pickMale = pickMale;
	}

	public RadioButton getPickFemale() {
		return pickFemale;
	}

	public void setPickFemale(RadioButton pickFemale) {
		this.pickFemale = pickFemale;
	}

	public TextField getEnterAge() {
		return enterAge;
	}

	public void setEnterAge(TextField enterAge) {
		this.enterAge = enterAge;
	}

	public Label getFeedbackGender() {
		return feedbackGender;
	}

	public void setFeedbackGender(Label feedbackGender) {
		this.feedbackGender = feedbackGender;
	}

	public Label getFeedbackAge() {
		return feedbackAge;
	}

	public void setFeedbackAge(Label feedbackAge) {
		this.feedbackAge = feedbackAge;
	}
}
