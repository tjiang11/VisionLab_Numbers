package controller;

import java.net.URL;
import java.util.logging.Logger;

import config.Config;
import model.NumberPair;
import model.NumberPairGenerator;
import model.GameLogic;
import model.Player;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.media.AudioClip;
import javafx.scene.text.Font;
import javafx.util.Duration;
import view.GameGUI;

/**
 * 
 * The center of the program; interface between the
 * models and the view. 
 * 
 * Classes Related to:
 *  -GameGUI.java (view)
 *      -Updates elements of the GUI as the game progresses and responds.
 *  -NumberPairGenerator.java (model)
 *      -Calls on NumberPairGenerator to generate new NumberPairs.
 *  -NumberPair.java (model)
 *      -NumberGameController keeps track of the most recent NumberPair created in variable currentNumberPair.
 *  -Player.java (model)
 *      -Updates Player information as the game progresses and responds.
 *  -GameLogic.java (model)
 *      -Calls on GameLogic to evaluate the correctness of a response from the subject.
 *  -DataWriter.java
 *      -Passes information (Player and NumberPair) to DataWriter to be exported.
 *      
 * @author Tony Jiang
 * 6-25-2015
 * 
 */
public class NumberGameController implements GameController {
    private static Logger logger = Logger.getLogger("mylog");
    
    /** Punish for wrong answers */
    static final boolean PUNISH = true;
    
    /** Time in milliseconds for the player to get ready after pressing start */
    final static int GET_READY_TIME = 2000;
    
    /** True if choices should vary in physical size */
    public static boolean SIZE_VARIATION;
    
    /** Time between rounds in milliseconds. */
    public static int TIME_BETWEEN_ROUNDS;
    
    /** DataWriter to export data to CSV. */
    private DataWriter dataWriter;
    
    /** NumberPairGenerator to generate an NumberPair */
    private NumberPairGenerator numberPairGenerator;
    /** The graphical user interface. */
    private GameGUI theView;
    /** The current scene. */
    private Scene theScene;

    /** The subject. */
    private Player thePlayer;
    /** The current NumberPair being evaluated by the subject. */
    private NumberPair currentNumberPair;
    
    /** Used to measure response time. */
    private static long responseTimeMetric;

    /** Current state of the game. */
    public static CurrentState state;
    
    /** Describes the current state of gameplay */
    private static GameState gameState;
    
    private enum GameState {
        /** Player has responded and next round is loading. */
        WAITING_BETWEEN_ROUNDS,
        
        /** Player has not responded and question is being displayed. */
        WAITING_FOR_RESPONSE,
    }
    
    /** Alternate reference to "this" to be used in inner methods */
    private NumberGameController gameController;
    
    /** 
     * Constructor for the controller. There is only meant
     * to be one instance of the controller. Attaches listener
     * for when user provides response during trials. On a response,
     * prepare the next round and record the data.
     * @param view The graphical user interface.
     */
    public NumberGameController(GameGUI view) {
        
        loadConfig();
        
        this.gameController = this;
        this.numberPairGenerator = new NumberPairGenerator();
        this.currentNumberPair = null;
        this.theView = view;
        this.theScene = view.getScene();
        this.thePlayer = new Player();
        this.dataWriter = new DataWriter(this);
    }
    
    /** 
     * Load configuration settings. 
     */
    private void loadConfig() {
        new Config();
        TIME_BETWEEN_ROUNDS = Config.getPropertyInt("time.between.rounds");
        SIZE_VARIATION = Config.getPropertyBoolean("size.variation");
    }
      
    /**
     * Sets event listener for when subject clicks the start button OR presses Enter.
     * Pass in the subject's ID number entered.
     */
    public void setLoginHandlers() {
        
        this.theScene = theView.getScene();
        
        this.theView.getStart().setOnAction(e -> 
            {
                onClickStartButton();
            });
        this.theScene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (event.getCode() == KeyCode.ENTER) {                
                    onClickStartButton();
                }
            }
        });
    }
    
    /**
     * Action to be executed upon clicking of Start on Login screen.
     */
    private void onClickStartButton() {
        try {
            gameController.thePlayer.setSubjectID(Integer.parseInt(theView.getEnterId().getText()));
            theView.setInstructionsScreen(); 
        } catch (NumberFormatException ex) {
            theView.getEnterId().setText("");
            theView.getEnterId().requestFocus();
            theView.getFeedback().setText("That's not your ID, silly!");
        }
    }
    
    /** 
     * Set event listener on the Next button and record the user's subject ID 
     */
    public void setInstructionsHandlers() {
        this.theView.getNext().setOnAction(e -> {
            theView.setGameScreen(); 
            state = CurrentState.PRACTICE;
        });
    }
    
    /**
     * Set handler upon clicking the "Start Assessment" button, preparing for actual assessment.
     * Sets the game screen and the state to GAMEPLAY from PRACTICE. Removes the "Practice" Label.
     * Resets the player's data.
     */
    public void setPracticeCompleteHandlers() {
        this.theView.getStartAssessment().setOnAction( e-> {
            theView.setGameScreen();
            theView.getPractice().setVisible(false);
            state = CurrentState.GAMEPLAY;
            gameState = null;
            SimpleIntegerProperty subjectID = new SimpleIntegerProperty(thePlayer.getSubjectID());
            thePlayer = new Player(subjectID);
        });
    }
    
    /** 
     * Sets event listener for when subject presses 'F' or 'J' key
     * during a round. 
     */
    public void setGameHandlers() {
        this.theScene = theView.getScene();
        this.theScene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if ((event.getCode() == KeyCode.F 
                        || event.getCode() == KeyCode.J) 
                        && gameState == GameState.WAITING_FOR_RESPONSE) {
                    
                    /** Set the state to prevent mass input from holding down
                     * 'F' or 'J' key. */
                    gameState = GameState.WAITING_BETWEEN_ROUNDS;
                    
                    /** Update models and view appropriately according to correctness
                     * of subject's response.
                     */
                    gameController.responseAndUpdate(event, theView);
                    
                    /** Prepare the next round */
                    gameController.prepareNextRound(); 
                    
                    /** Export data to CSV file in folder 'results/<subject_id>' */
                    if (state == CurrentState.GAMEPLAY) {
                        dataWriter.writeToCSV();    
                    }
                    
                    if (state == CurrentState.PRACTICE && thePlayer.getNumRounds() >= NUM_PRACTICE_ROUNDS) {
                        theView.setPracticeCompleteScreen();
                    }
                }

            }
        });
    }  
    
    /**
     * Update models and view appropriately according to correctness
     * of subject's response.  
     * @param e The key event to check which key the user pressed.
     * @param ap The current NumberPair being evaluated.
     * @param currentPlayer The subject.
     * @param pb the ProgressBar to update.
     * @return True if the player is correct. False otherwise.
     */
    public void responseAndUpdate (
            KeyEvent e, GameGUI view) {
        boolean correct;
        NumberPair ap = this.currentNumberPair;
        Player currentPlayer = this.thePlayer;
        URL feedbackSoundFileUrl = null;
        
        correct = GameLogic.checkAnswerCorrect(e, ap);
        
        this.updateProgressBar(view, correct);
        this.updatePlayer(currentPlayer, correct);   
        this.feedbackSound(feedbackSoundFileUrl, correct); 
        
        this.dataWriter.grabData(this);
    }
    
    /** Update the player appropriately.
     * 
     * @param currentPlayer The current player.
     * @param correct True if subject's reponse is correct. False otherwise.
     */
    private void updatePlayer(Player currentPlayer, boolean correct) {
        if (correct) {
            currentPlayer.addPoint();
            currentPlayer.setRight(true);
        } else {
            currentPlayer.setRight(false);
        }
        currentPlayer.incrementNumRounds();
    }
    
    /**
     * Update the progressbar. Resets to zero if progress bar is full.
     * @param pb The view's progress bar.
     */
    private void updateProgressBar(GameGUI view, boolean correct) {
        if (correct) {
            if (view.getProgressBar().isIndeterminate()) {
                view.getProgressBar().setProgress(0.0);
                view.getProgressBar().setStyle("-fx-accent: #0094C5;");
            }
            view.getProgressBar().setProgress(view.getProgressBar().getProgress() + .25);
            if (view.getProgressBar().getProgress() >= 1.00) {
                view.getProgressBar().setProgress(0.25);
                
                URL powerUpSound = getClass().getResource("/res/sounds/Powerup.wav");
                new AudioClip(powerUpSound.toString()).play();
                
                int starToReveal = this.thePlayer.getNumStars();
                view.getStarNodes()[starToReveal].setVisible(true);
                this.thePlayer.incrementNumStars();
                
                if (this.thePlayer.getNumStars() > 2) {
                    view.changeBackground(1);
                }
            }
        } else {
            view.getProgressBar().setStyle("-fx-accent: #0094C5;");
            if (PUNISH) {
                view.getProgressBar().setProgress(view.getProgressBar().getProgress() - .125);
                if (view.getProgressBar().isIndeterminate()) {
                    view.getProgressBar().setStyle("-fx-accent: red;");
                    
                }
            }
        }
    }
    
    /** If user inputs correct answer play positive feedback sound,
     * if not then play negative feedback sound.
     * @param feedbackSoundFileUrl the File Url of the Sound to be played.
     * @param correct whether the subject answered correctly or not.
     */
    private void feedbackSound(URL feedbackSoundFileUrl, boolean correct) {
        if (correct) {
            feedbackSoundFileUrl = 
                    getClass().getResource("/res/sounds/Ping.aiff");
        } else {
            feedbackSoundFileUrl = 
                    getClass().getResource("/res/sounds/Basso.aiff");
        }
        new AudioClip(feedbackSoundFileUrl.toString()).play();
    }
    
    /**
     * Prepare the first round by making a load bar to 
     * let the subject prepare for the first question.
     */
    public void prepareFirstRound() {
        
        Task<Void> sleeper = new Task<Void>() {   
            @Override
            protected Void call() throws Exception {
                for (int i = 0; i < GET_READY_TIME; i++) {
                    this.updateProgress(i, GET_READY_TIME); 
                    Thread.sleep(1);
                }
                return null;
            }
        };
        theView.getGetReadyBar().progressProperty().bind(sleeper.progressProperty());
        sleeper.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent e) {
                setOptions();
                gameState = GameState.WAITING_FOR_RESPONSE;
                responseTimeMetric = System.nanoTime();
                theView.getGetReadyBox().setVisible(false);
            }
        });
        new Thread(sleeper).start();
    }
    
    /**
     * Prepares the next round by recording reponse time,
     * clearing the previous round, waiting, and creating the next round.
     */
    public void prepareNextRound() {
        recordResponseTime();
        clearRound();
        waitBeforeNextRoundAndUpdate(TIME_BETWEEN_ROUNDS); 
        
        if (thePlayer.getNumRounds() >= NUM_ROUNDS) {
            this.finishGame();
        }
    }
    
    /**
     * If subject has completed the total number of rounds specified,
     * then change the scene to the finish screen.
     */
    private void finishGame() {
        state = CurrentState.FINISHED;
        System.out.println("Done");
        theView.setFinishScreen(gameController);
    }

    /**
     * Clears the options.
     */
    public void clearRound() {
        getTheView().getLeftOption().setText("");
        getTheView().getRightOption().setText("");
    }

    /**
     * Wait for a certain time and then set the next round.
     */
    public void waitBeforeNextRoundAndUpdate(int waitTime) {
        
        Task<Void> sleeper = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                for (int i = 0; i < waitTime; i++) {
                    this.updateProgress(i, waitTime); 
                    Thread.sleep(1);
                }
                return null;
            }
        };
        sleeper.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent e) {
                setOptions();
                gameState = GameState.WAITING_FOR_RESPONSE;
                responseTimeMetric = System.nanoTime();
            }
        });
        new Thread(sleeper).start();
    }

    /**
     * Set the next round's choices.
     */
    public void setOptions() {
        int numberOne, numberTwo;
        
        numberPairGenerator.getNewDifficultyPair();
        this.currentNumberPair = numberPairGenerator.getNumberPair();
        
        numberOne = this.currentNumberPair.getNumberOne();
        numberTwo = this.currentNumberPair.getNumberTwo();
        
        theView.getLeftOption().setText(String.valueOf(numberOne));
        theView.getRightOption().setText(String.valueOf(numberTwo));
        
        if (SIZE_VARIATION) {
            int numberSizeOne = this.currentNumberPair.getFontSizeOne();
            int numberSizeTwo = this.currentNumberPair.getFontSizeTwo();
            
            theView.getLeftOption().setFont(new Font("Tahoma", numberSizeOne));
            theView.getRightOption().setFont(new Font("Tahoma", numberSizeTwo));
        }
    }
    
    /** 
     * Record the response time of the subject. 
     */
    public void recordResponseTime() {
        long responseTime = System.nanoTime() - responseTimeMetric;
        thePlayer.setResponseTime(responseTime);
        
        //Convert from nanoseconds to seconds.
        double responseTimeSec = responseTime / 1000000000.0;

        logger.info("Your response time was: " 
                + responseTimeSec + " seconds");
    }
    
    /**
     * Slowly drains the progress bar to encourage the user not to spend too much time thinking.
     */
    public void beginProgressBarDrainage() {
        theView.getProgressBar().setProgress(0.6);
        
        Timeline drainer = new Timeline(
                new KeyFrame(Duration.seconds(0), evt -> {
                    if (gameController.theView.getProgressBar().getProgress() > 1.0) {
                        gameController.theView.getProgressBar().setStyle("-fx-accent: #00CC00;");
                    } else {
                        gameController.theView.getProgressBar().setStyle("-fx-accent: #0094C5;");
                    }
                    if (gameController.theView.getProgressBar().getProgress() > .005) {
                        gameController.theView.getProgressBar().setProgress(theView.getProgressBar().getProgress() - .0035);
                    }
                }), new KeyFrame(Duration.seconds(0.065)));
        drainer.setCycleCount(Animation.INDEFINITE);
        drainer.play();
    }

    public Player getThePlayer() {
        return thePlayer;
    }

    public void setThePlayer(Player thePlayer) {
        this.thePlayer = thePlayer;
    }

    public NumberPair getCurrentNumberPair() {
        return currentNumberPair;
    }

    public void setCurrentNumberPair(NumberPair currentNumberPair) {
        this.currentNumberPair = currentNumberPair;
    }

    public NumberPairGenerator getApg() {
        return numberPairGenerator;
    }

    public void setApg(NumberPairGenerator numberPairGenerator) {
        this.numberPairGenerator = numberPairGenerator;
    }
    
    public GameGUI getTheView() {
        return theView;
    }
    
    public void setTheScene(Scene scene) {
        this.theScene = scene;
    }
}