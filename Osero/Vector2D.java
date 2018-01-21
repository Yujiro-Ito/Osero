

public class Vector2D{
    //----fields----
    private double _x;
    private double _y;

    //----methods----
    //--constractor--
    public Vector2D(double x, double y){
        this.set(x, y);
    }

    public Vector2D(Vector2D old){
        this.set(old.getX(), old.getY());
    }

    //--getter setter--
    public void setX(double x){
        _x = x;
    }

    public void setY(double y){
        _y = y;
    }

    public void set(double x, double y){
        _x = x;
        _y = y;
    }

    public void set(Vector2D vec){
        _x = vec.getX();
        _y = vec.getY();
    }

    public double getX(){
        return _x;
    }
    
    public double getY(){
        return _y;
    }

    public String toString(){
        return String.format("Vector2D[x:%f,y:%f]", this.getX(), this.getY());
    }

    //--basic calculation--
    public Vector2D add(Vector2D vec){
        _x += vec.getX();
        _y += vec.getY();
        return this;
    }

    public Vector2D add(double x, double y){
        _x += x;
        _y += y;
        return this;
    }

    public Vector2D subtract(Vector2D vec){
        _x -= vec.getX();
        _y -= vec.getY();
        return this;

    }

    public Vector2D subtract(double x, double y){
        _x -= x;
        _y -= y;
        return this;
    }

    public Vector2D multiply(Vector2D vec){
        _x *= vec.getX();
        _y *= vec.getX();
        return this;
    }

    public Vector2D multiply(double x, double y){
        _x *= x;
        _y *= y;
        return this;
    }

    public Vector2D division(Vector2D vec){
        _x /= vec.getX();
        _y /= vec.getY();
        return this;
    }

    public Vector2D division(double x, double y){
        _x /= x;
        _y /= y;
        return this;
    }

    //--static--
    public static Vector2D add(Vector2D one, Vector2D two){
        return new Vector2D(one.getX() + two.getX(), one.getY() + two.getY());
    }

    public static Vector2D subtract(Vector2D one, Vector2D two){
        return new Vector2D(one.getX() - two.getX(), one.getY() - two.getY());
    }

    public static Vector2D multiply(Vector2D one, Vector2D two){
        return new Vector2D(one.getX() * two.getX(), one.getY() * two.getY());
    }

    public static Vector2D division(Vector2D one, Vector2D two){
        return new Vector2D(one.getX() / two.getX(), one.getY() / two.getY());
    }

    public static double distance(Vector2D one, Vector2D two){
        return Math.sqrt( Math.pow( one.getX() - two.getX() , 2 ) - Math.pow( one.getY() - two.getY() , 2) );
    }

    public static Vector2D zero(){
        return new Vector2D(0, 0);
    }

    public static Vector2D one(){
        return new Vector2D(1, 1);
    }
}