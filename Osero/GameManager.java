import javafx.geometry.*;
import javafx.event.*;
import javafx.scene.input.*;
import javafx.scene.*;
import javafx.scene.image.*;
import javafx.stage.*;
import javafx.scene.paint.*;
import java.util.*;
import java.lang.ArrayIndexOutOfBoundsException;
import javafx.collections.*;

public class GameManager{
    //----fields----
    private Game _parent;
    private Stage _chatStage;
    private Player[] _players;
    private OseroStone[][] _oseroField;
    private ArrayList<Vector2D> _oneLinePositions;

    //----methods----
    public GameManager(Game parent){
        _parent = parent;
        _oneLinePositions = new ArrayList<Vector2D>();

    }

    public void initialize(){
        //set first stone
        _oseroField = new OseroStone[Game.LINE_SIZE][Game.LINE_SIZE];
        for(int x = 0; x < _oseroField.length; x++){
            for(int y = 0; y < _oseroField[x].length; y++){
                _oseroField[x][y] = OseroStone.None;

                //Setting black stone position
                if(x == (Game.LINE_SIZE / 2 - 1) && y == (Game.LINE_SIZE / 2)){
                    _oseroField[x][y] = OseroStone.Black;
                }
                if(x == (Game.LINE_SIZE / 2) && y == (Game.LINE_SIZE / 2 - 1)){
                    _oseroField[x][y] = OseroStone.Black;
                }

                //Setting white stone position
                if(x == (Game.LINE_SIZE / 2) && y == (Game.LINE_SIZE / 2)){
                    _oseroField[x][y] = OseroStone.White;
                }
                if(x == (Game.LINE_SIZE / 2 - 1) && y == (Game.LINE_SIZE / 2 - 1)){
                    _oseroField[x][y] = OseroStone.White;
                }
            }
        }

        //regist serialnumber
        _players = new Player[2];
        _players[0] = _parent.playerOne;
        _players[1] = _parent.playerTwo;
        _players[0].setMyStone(OseroStone.Black);
        _players[0].setPlayerName("BLACK MAN");
        _players[1].setMyStone(OseroStone.White);
        _players[1].setPlayerName("WHITE MAN");
        changePlayer(_players[0]);

        //regist serialnumber
        for(int i = 0; i < _players.length; i++){
            _players[i].setSerialNumber(i);
        }
 
        //display stones
        _parent.viewAgent.ResetFieldDisplay(_oseroField);

        //regist event handler
        MyEventHandler eventhandler = new MyEventHandler(_parent);
        _parent.viewAgent.getCanvas().addEventHandler(MouseEvent.MOUSE_PRESSED, eventhandler);

        //--chat stage--
        
    }

    //When put stone call those method
    public Vector2D convertPosition(Vector2D screenPosition){
        //convert
        double size = Game.CANVAS_SIZE / Game.LINE_SIZE;
        double x = screenPosition.getX() / size;
        double y = screenPosition.getY() / size;

        return new Vector2D(x, y);
    }

    //simulation
    public boolean isPutPointExist(OseroStone color, List<Vector2D> canPutPositions){
        boolean result = false;
        Vector2D checkPoint = Vector2D.zero();

        //check all square position
        for(int x = 0; x < _oseroField.length; x++){
            for(int y = 0; y < _oseroField[x].length; y++){
                checkPoint.set(x, y);
                if(canPutStone(checkPoint, color, null)){
                    result = true;
                    if(canPutPositions != null) canPutPositions.add(new Vector2D(x, y));
                }
            }
        }

        return result;
    }

    //is put stone judge(one point)
    public boolean canPutStone(Vector2D squarePosition, OseroStone color, List<Vector2D> canReverceStones){
        //check byside 8 direction
        Vector2D tmpPosition = Vector2D.zero();
        OseroStone enemyColor = (color == OseroStone.Black) ? OseroStone.White : OseroStone.Black;
        boolean put = false;

        //----check mouse click position is none----
        if(_oseroField[(int)squarePosition.getX()][(int)squarePosition.getY()] != OseroStone.None){
            return false;
        }

        //----check and put around area stone----
        for(int x = -1; x <= 1; x++){
            for(int y = -1; y <= 1; y++){
                //reset variable
                tmpPosition.set(squarePosition);
                _oneLinePositions.clear();
                //need check other center position
                if(x != 0 || y != 0){
                    boolean stop = false;
                    //check exist enemy color block in line
                    do{
                        //update tmp position
                        tmpPosition.add(x, y);
                        //overflow check
                        if(isOverflowInField(tmpPosition)){
                            _oneLinePositions.clear();
                            stop = true;
                        } else if(_oseroField[(int)tmpPosition.getX()][(int)tmpPosition.getY()] == OseroStone.None){
                            //stone exist check
                            _oneLinePositions.clear();
                            stop = true;
                        } else if(_oseroField[(int)tmpPosition.getX()][(int)tmpPosition.getY()] == enemyColor){
                            //memory enemy color position
                            _oneLinePositions.add(new Vector2D(tmpPosition));
                        } else if(_oseroField[(int)tmpPosition.getX()][(int)tmpPosition.getY()] == color){
                            //if bumping to same color when break this loop
                            stop = true;
                        }
                        
                    } while(stop == false);

                    //Assign to all reverce positions list
                    for(Vector2D pos: _oneLinePositions){
                        if(canReverceStones != null)canReverceStones.add(new Vector2D(pos));
                        put = true;
                    }
                }
            }
        }

        return put;
    }

    //put stones
    public boolean putStone(Vector2D squarePosition, OseroStone color){
        //check byside 8 direction
        ArrayList<Vector2D> receivePositions = new ArrayList<Vector2D>();
        boolean put = false;

        //check can put stone
        if(canPutStone(squarePosition, color, receivePositions)){
            put = true;
            receivePositions.add(new Vector2D(squarePosition));
        }

        //change enemy stone color to mine color
        for(Vector2D point : receivePositions){
            System.out.println(point.toString());
            _oseroField[(int)point.getX()][(int)point.getY()] = color;
        }

        //reset field view
        _parent.viewAgent.ResetFieldDisplay(_oseroField);

        return put;
    }

    //change player
    public Player changePlayer(Player player){
        //change current player
        int serialNumber = player.getSerialNumber();
        _players[serialNumber].Stop();
        int enemySerialNumber = (serialNumber + 1) %  2;
        _players[enemySerialNumber].CanMove();
        
        //display next player name
        String enemyName = _players[enemySerialNumber].getPlayerName();
        _parent.viewAgent.getTopLabel().setText(enemyName + "'s turn");
        _parent.viewAgent.getTopLabel().setGraphic(new ImageView(_parent.viewAgent.getStoneImage(_players[enemySerialNumber].getOseroStone())));

        return _players[enemySerialNumber];
    }

    //finish judge
    public boolean finishJudge(OseroStone color){
        //when 80% buried place, start to look for exist empty put point.
        boolean finish = false;
        int count = 0;
        for(int i = 0; i < Game.LINE_SIZE * Game.LINE_SIZE; i++){
            int x = i / Game.LINE_SIZE;
            int y = i % Game.LINE_SIZE;
            if(_oseroField[x][y] != OseroStone.None)count++;
        }
        int buriedPercent = (int)((double)count / (double)(Game.LINE_SIZE * Game.LINE_SIZE) * 100.0);
        System.out.println("current : " + buriedPercent + "%");

        count = 0;
        if(buriedPercent > 80){
            for(int x = 0; x < _oseroField.length; x++){
                for(int y = 0; y < _oseroField[x].length; y++){
                    if(isPutPointExist(color, null) == false){
                        count++;
                    }
                }
            }
        }

        System.out.println(count);
        if(count == Game.LINE_SIZE * Game.LINE_SIZE){
            return true;
        }
        return false;
    }

    //position overflow check in osero field
    public boolean isOverflowInField(Vector2D position){
        //System.out.println("Overflow : " + position.toString());
        try{
            OseroStone stone = _oseroField[(int)position.getX()][(int)position.getY()];
        }catch(ArrayIndexOutOfBoundsException e){
            return true;
        }
        return false;
    }

    //--getter setter--
    public Player[] getPlayers(){
        return _players;
    }

    //----event handler----
    private class MyEventHandler implements EventHandler<MouseEvent>{
        private Game _parent;

        public MyEventHandler(Game parent){
            _parent = parent;
        }

		public void handle(MouseEvent e){
            //choose can move player
            int i;
            for(i = 0; i < _parent.gameManager.getPlayers().length; i++){
                if(_parent.gameManager.getPlayers()[i].getPlayerState() == PlayerState.CanMove){
                    break;
                }
            }

            //convert to sqare
            double x = e.getX();
            double y = e.getY();
            Vector2D square = _parent.gameManager.convertPosition(new Vector2D(x, y));

            //put stone judge
            if(_parent.gameManager.putStone(square, _parent.gameManager.getPlayers()[i].getOseroStone()) == false){
                //if cant put then stop this method
                return;
            }
            
            //change player
            Player nextPlayer = _parent.gameManager.changePlayer(_parent.gameManager.getPlayers()[i]);

            if(_parent.gameManager.finishJudge(nextPlayer.getOseroStone())){
                //initialize variable
                int whiteCount, blackCount;
                ArrayList<Vector2D> counter = new ArrayList<Vector2D>();

                //count stone num
                isPutPointExist(OseroStone.White, counter);
                whiteCount = counter.size();
                counter.clear();
                isPutPointExist(OseroStone.Black, counter);
                blackCount = counter.size();

                //get players
                Player white = null;
                Player black = null;
                Player winner = null;
                int whiteSuffix = (_parent.gameManager.getPlayers()[0].getOseroStone() == OseroStone.White) ? 0 : 1;
                white = _parent.gameManager.getPlayers()[whiteSuffix];
                black = _parent.gameManager.getPlayers()[(whiteSuffix + 1) % 2];

                //judge winner
                if(whiteCount > blackCount){
                    winner = white;
                } else if(whiteCount < blackCount){
                    winner = black;
                } else {
                    winner = null;
                }

                //winner display
                if(winner != null){
                    _parent.viewAgent.setTopLabelText(String.format("Winner:%s !!", winner.getPlayerName()));
                } else {
                    _parent.viewAgent.setTopLabelText(String.format("Draw!"));
                }
            }
		}
	}
}