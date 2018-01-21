import javafx.geometry.*;

public class Player{
    //----fields----
    private PlayerState _currentState;
    private PlayerState _nextState;
    private Game _parent;
    private int _serialNumber;
    private OseroStone _myStone;
    private String _myName;

    //----methods----
    //--constractor--
    public Player(Game parent){
        _currentState = PlayerState.Stop;
        _parent = parent;
    }

    public void initialize(){
    }

    //--turn on move--
    public void CanMove(){
        _currentState = PlayerState.CanMove;
    }

    public void Stop(){
        _currentState = PlayerState.Stop;
    }

    //--getter setter--
    public void setSerialNumber(int num){
        _serialNumber = num;
    }

    public int getSerialNumber(){
        return _serialNumber;
    }
    
    public void setMyStone(OseroStone stone){
        _myStone = stone;
    }

    public OseroStone getOseroStone(){
        return _myStone;
    }

    public void setPlayerName(String name){
        _myName = name;
    }

    public String getPlayerName(){
        return _myName;
    }

    public PlayerState getPlayerState(){
        return _currentState;
    }
}