import javafx.geometry.*;
import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.scene.paint.*;
import javafx.scene.image.*;
import javafx.scene.effect.*;
import javafx.scene.text.*;
import javafx.scene.input.*;
import javafx.scene.canvas.*;
import javafx.scene.shape.*;
import javafx.application.*;
import javafx.stage.*;
import javafx.event.*;
import javafx.collections.*;

public class ViewAgent{
	//----fields----
	private Stage _stage;
	//--ui--
	private Button _button;
	private Label _reporter;
	private Canvas _canvas;
	private Image _whiteImgae, _blackImage;
	private ImageView _stone;
	private GraphicsContext _gc;
	private Game _parent;
	private MenuBar _menuBar;
	
	//----methods----
	//--constractor--
	public ViewAgent(Game parent){
		_parent = parent;
	}

	public void ViewConstraction(Stage stage){
		//initialize variables 
		_stage = stage;
		_button = new Button("Pass");
		_reporter = new Label("Hogehoge");
		_whiteImgae = new Image("data/White.png");
		_blackImage = new Image("data/Black.png");
		_reporter.setFont(new Font(18));
		_canvas = new Canvas(320, 320);
		_gc = _canvas.getGraphicsContext2D();
		_menuBar = new MenuBar();

		//setting menu bar
		_menuBar.setBackground(new Background(new BackgroundFill(Color.LIGHTGRAY, null, null)));
		//create menu
		Menu menu = new Menu("Game Manu");
		//create menu items
		MenuItem restart = new MenuItem("Restart Game");
		//regist id
		restart.setId(Game.RESET_WORD);
		//regist menu
		ObservableList<Menu> menuList = _menuBar.getMenus();
		menuList.add(menu);
		//regist menu items
		ObservableList<MenuItem> menuItemList = menu.getItems();
		menuItemList.add(restart);
		//regist event handler
		MenuEventHandler menuEventHandler = new MenuEventHandler(_parent);
		menu.addEventHandler(ActionEvent.ANY, menuEventHandler);


		
		//regist eventhandler
		MyEventHandler actionhandler = new MyEventHandler(_parent);
		_button.addEventHandler(ActionEvent.ANY, actionhandler);
		
		//create layeout of vbox
		VBox vb = new VBox();
		ObservableList<Node> lst = vb.getChildren();
		lst.add(_reporter);
		lst.add(_canvas);
		lst.add(_button);
		vb.setPadding(new Insets(10));
		vb.setSpacing(15);
		vb.setBackground(null);
		BorderPane borderPane = new BorderPane();
		borderPane.setBackground(null);
		borderPane.setTop(_menuBar);
		borderPane.setCenter(vb);
		
		//create scene object
		Scene scene = new Scene(borderPane);
		scene.setFill(Color.LIGHTGREEN);
		
		//regist scene to stage
		_stage.setScene(scene);
		_stage.setTitle("Happy Osero");
	}

	public void ResetFieldDisplay(OseroStone[][] currentOseroField){
		//create osero field
		_gc.setFill(Color.GREEN);
		_gc.fillRect(0, 0, Game.CANVAS_SIZE, Game.CANVAS_SIZE);
		_gc.setStroke(Color.BLACK);
		_gc.setLineWidth(1);
		int lineMax = currentOseroField.length;
		//draw line
		for(int i = 0; i < lineMax + 1; i++){
			int drawPosition = Game.CANVAS_SIZE / lineMax * i;
			_gc.strokeLine(drawPosition, 0, drawPosition, Game.CANVAS_SIZE);
			_gc.strokeLine(0, drawPosition, Game.CANVAS_SIZE, drawPosition);
		}
		//put stones
		for(int x = 0; x < lineMax; x++){
			for(int y = 0; y < lineMax; y++){
				//position decide
				double drawPositionX = Game.CANVAS_SIZE / lineMax * x + Game.CANVAS_SIZE / lineMax / 2 * 0.3;
				double drawPositionY = Game.CANVAS_SIZE / lineMax * y + Game.CANVAS_SIZE / lineMax / 2 * 0.3;
				double circleSize = Game.CANVAS_SIZE / lineMax * 0.7;
				//put black stone
				if(currentOseroField[x][y] == OseroStone.Black){
					_gc.setFill(Color.BLACK);
					_gc.fillOval(drawPositionX, drawPositionY, circleSize, circleSize);
					//System.out.println("passblack" + x + " " + y);
				}
				//put white stone
				if(currentOseroField[x][y] == OseroStone.White){
					_gc.setFill(Color.WHITE);
					_gc.fillOval(drawPositionX, drawPositionY, circleSize, circleSize);
					//System.out.println("passwhite" + x + " " + y);
				}
			}
		}
	}

	//--getter--
	public Stage getStage(){
		return _stage;
	}

	public Canvas getCanvas(){
		return _canvas;
	}

	public GraphicsContext getGC(){
		return _gc;
	}

	public Label getTopLabel(){
		return _reporter;
	}

	public Image getStoneImage(OseroStone color){
		return (color == OseroStone.Black) ? _blackImage : _whiteImgae;
	}

	public void setTopLabelText(String text){
		_reporter.setText(text);
	}

	//----event handler----
	private class MyEventHandler implements EventHandler<ActionEvent>{
		Game _parent;

		public MyEventHandler(Game parent){
			_parent = parent;
		}

		public void handle(ActionEvent e){
			Button bt = (Button)e.getTarget();
			int suffix = (_parent.gameManager.getPlayers()[0].getPlayerState() == PlayerState.Stop) ? 1 : 0;
			_parent.gameManager.changePlayer(_parent.gameManager.getPlayers()[suffix]);
			
		}
	}

	private class MenuEventHandler implements EventHandler<ActionEvent>{
		//----fields----
		private Game _parent;

		//----method----
		//--constractor--
		public MenuEventHandler(Game parent){
			_parent = parent;
		}

		//--hendler--
		public void handle(ActionEvent e){
			MenuItem mi = (MenuItem)e.getTarget();

			//when user selected field initialize button, call initialize method
			if(mi.getId() == Game.RESET_WORD){
				_parent.gameManager.initialize();
			}
		}
	}
	
}