package silva.miguel.throwyourlife;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import java.io.Serializable;

/**
 * Created by Miguel on 18/07/2016.
 */
public class Missil extends GameObject implements Serializable {
    //Vars
    private int speed;
    private Animation animation;
    private Bitmap spritesheet;
    private double declive; //direcao do missil
    private boolean tabela;

    //Const

    /**
     * Constructor by parameter
     * @param res - spritesheet
     * @param xt - x touch event
     * @param yt - y touch event
     * @param sw - screen width
     * @param sh - screen height
     * @param w - missile width
     * @param h - missile height
     * @param numFrames - number of frames on the spritesheet
     * @param s - speed
     */
    public Missil(Bitmap res, float xt, float yt, int sw, int sh , int w, int h, int numFrames, int s) {
        animation = new Animation();
        spritesheet = res;
        speed = s;
        width = w;
        height = h;
        x = ((sw - w)/2);
        y = sh;
        declive = -((xt - x) / (yt - y));
        Bitmap[] image = new Bitmap[numFrames];
        for(int i = 0; i < numFrames; i++) {
            image[i] = Bitmap.createBitmap(spritesheet, i*width, 0, width, height);
        }
        animation.setFrames(image);
        animation.setDelay(50 - speed);
        tabela = false;
    }

    //Met
    public void update() {
        y -= speed;
        x += speed * declive;
        animation.update();
    }

    public void draw(Canvas canvas) {
        try {
            canvas.drawBitmap(animation.getImage(),(int) x,(int) y, null);
        }catch(Exception e) {}
    }

    public void inverteDeclive() {
        declive = -declive;
    }

    public void setDeclive(double declive) {
        this.declive = declive;
    }

    public void setTabela(boolean b) {
        tabela = b;
    }

    public boolean fezTabela() {
        return tabela;
    }

}
