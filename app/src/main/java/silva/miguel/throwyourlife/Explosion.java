package silva.miguel.throwyourlife;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import java.io.Serializable;

/**
 * Created by Miguel on 21/09/2016.
 */
public class Explosion extends GameObject implements Serializable{

    private Bitmap spritesheet;
    private Animation animation;
    private int frames;
    private int frame;
    private boolean remove;

    public Explosion(Bitmap sps, int x, int y, int w, int h, int frames, int enemyW, int enemyH) {
        this.x = x - enemyW/4;
        this.y = y - enemyH/4;
        this.frames = frames;
        this.frame = 1;
        width = w;
        height = h;
        spritesheet= sps;
        remove = false;
        animation = new Animation();
        Bitmap[] fms = new Bitmap[frames];
        for(int i = 0; i < frames; i++) {
            fms[i] = Bitmap.createBitmap(spritesheet, i*width, 0, width, height);
        }
        animation.setFrames(fms);
        animation.setDelay(15);
    }

    public boolean remove() {
        return frame==frames;
    }

    public void update() {
        frame++;
        animation.update();
    }

    public void draw(Canvas canvas) {
        try {
            canvas.drawBitmap(animation.getImage(),(int) x,(int) y, null);
        }catch(Exception e) {}
    }

}
