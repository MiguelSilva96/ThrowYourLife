package silva.miguel.throwyourlife;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import java.io.Serializable;

/**
 * Created by Miguel on 17/07/2016.
 */
public class Enemy extends GameObject implements Serializable{
    //Vars
    private Animation animation;
    private Bitmap spritesheet;

    //Constructor
    public Enemy(Bitmap res, int x, int y, int w, int h, int numFrames, int speed) {
        animation = new Animation();
        super.x = x;
        super.y = y;
        width = w;
        height = h;
        Bitmap[] image = new Bitmap[numFrames];
        spritesheet = res;

        for(int i = 0; i < image.length; i++) {
            image[i] = Bitmap.createBitmap(spritesheet, i*width, 0, width, height);
        }
        animation.setFrames(image);
        animation.setDelay(65 - speed / 4);
    }

    public void update(int speed) {
        y += speed;
        animation.update();
    }

    public void draw(Canvas canvas) {
        try {
            canvas.drawBitmap(animation.getImage(),(int) x,(int) y, null);
        }catch(Exception e) {
            e.printStackTrace();
        }
    }

}
