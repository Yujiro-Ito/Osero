import javafx.application.*;
import javafx.stage.*;
import javafx.event.*;
import javafx.collections.*;

//----main class----
public class Kadai extends Application{
	//----fields----
	private Game _game;

	//--override application interface--
	public void start(Stage stage) throws Exception{
		_game = new Game(stage);
		
	}

	//--entry point--
	public static void main(String[] args){
		launch(args);
	}
}