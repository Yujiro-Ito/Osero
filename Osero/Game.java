import java.util.stream.Stream;
import javafx.application.*;
import javafx.stage.*;
import javafx.event.*;
import javafx.collections.*;

public class Game{
    //----consts----
    public static final int LINE_SIZE = 8;
    public static final int CANVAS_SIZE = 320;
    public static final String RESET_WORD = "Reset";

    //----fields----
    public ViewAgent viewAgent;
    public Player playerOne;
    public Player playerTwo;
    public GameManager gameManager;

    //----methods----
    //--constracts--
    public Game(Stage stage){
        //constract display contents
        viewAgent = new ViewAgent(this);
        viewAgent.ViewConstraction(stage);
        stage = viewAgent.getStage();
        stage.show();

        //instantiate
        playerOne = new Player(this);
        playerTwo = new Player(this);
        gameManager = new GameManager(this);

        //initialize
        gameManager.initialize();
        playerOne.initialize();
        playerTwo.initialize();
    }
}