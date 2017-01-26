package silva.miguel.throwyourlife;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import java.io.Serializable;

/**
 * Created by Miguel on 05/07/2016.
 */
public class Player extends GameObject implements Serializable{
    //Var
    private Bitmap spritesheet;
    private int score;
    private int life;
    private int level;
    private boolean playing;

    /**
     * Construtor por parametro
     * @param res -
     * @param w -
     * @param h -
     * @param numFrames -
     */
    public Player(Bitmap res, int w, int h, int score, int level) {
        dx = 0;
        this.score = score;
        this.level = level;
        life = 10 + level - 1;
        height = h;
        width = w;
        spritesheet = res;
    }

    public void update() { }

    public void draw(Canvas canvas) {
        canvas.drawBitmap(spritesheet ,(int) x,(int) y, null);
    }

    public void setSpritesheet(Bitmap img) {
        spritesheet = img;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public boolean getPlaying() {
        return playing;
    }

    public void setPlaying(boolean b) {
        playing = b;
    }

    public void enemyKilled() {
        score += 10;
    }

    public void resetScore() {
        score = 0;
    }

    public void incLife() {
        life++;
    }

    public void decLife() {
        life--;
    }

    public int getLife() {
        return life;
    }

    public void setLife(int life) {
        this.life = life;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public void incLevel() {
        level++;
    }
}
