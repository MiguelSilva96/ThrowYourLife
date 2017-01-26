package silva.miguel.throwyourlife;

import android.graphics.Rect;

import java.io.Serializable;

/**
 * Created by Miguel on 05/07/2016.
 */
public abstract class GameObject implements Serializable{
    //Var
    protected double x;
    protected double y;
    protected int dx;
    protected int dy;
    protected int width;
    protected int height;

    //Metodos
    public void setX(int x){
        this.x = x;
    }

    public void setY(int y){
        this.y = y;
    }

    public void setDx(int dx){
        this.dx = dx;
    }

    public void setDy(int dy){
        this.dy = dy;
    }

    public double getX(){
        return x;
    }

    public double getY(){
        return y;
    }

    public int getHeight(){
        return height;
    }

    public int getWidth(){
        return width;
    }

    public Rect getRectange(){
        return new Rect((int) x,(int) y,(int) x+width,(int) y+height);
    }

    /*@Override
    public abstract GameObject clone(); */
}
