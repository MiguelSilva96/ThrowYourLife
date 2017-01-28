package silva.miguel.throwyourlife;

import android.media.AudioManager;

import java.io.Serializable;

/**
 * Created by Miguel on 20/07/2016.
 */
public class Constants implements Serializable{

    /**
     * Instance variables
     */
    //screen running
    protected final int width;
    protected final int height;

    //scale factors
    protected final float scaleX;
    protected final float scaleY;

    protected final int textSize;
    protected final int enemySpeed;
    protected final int missilSpeed;
    protected final int scaledEnemyWidth;
    protected final int scaledEnemyHeight;
    protected final int scaledExplosionWidth;
    protected final int scaledExplosionHeight;
    protected final int heightToLose;
    protected final int positionHitWall;

    //places of screen infos -- WP: width point, HP: height point
    protected final int lifeIconWP;
    protected final int lifeIconHP;
    protected final int levelTextWP;
    protected final int levelTextHP;
    protected final int enemiesTextWP;
    protected final int enemiesTextHP;
    protected final int lifeTextWP;
    protected final int lifeTextHP;
    protected final int startTextWP;
    protected final int startTextHP;
    protected final int levelupWP;
    protected final int levelupHP;


    /**
     * Class variables
     */
    //hypothetical background dimensions for scale purposes
    protected static final int WIDTH  = 720;
    protected static final int HEIGHT = 1280;

    //player sprite dimensions
    protected static final int CHWIDTH  = 60;
    protected static final int CHHEIGHT = 60;

    //enemy sprite dimensions
    protected static final int EWIDTH  = 330;
    protected static final int EHEIGHT = 50;

    //bullet sprite dimensions
    protected static final int BWIDTH  = 100;
    protected static final int BHEIGHT = 20;

    //life icon dimensions
    protected static final int LWIDTH  = 30;
    protected static final int LHEIGHT = 30;

    //explosion sprite dimensions
    protected static final int EXWIDTH = 600;
    protected static final int EXHEIGHT = 100;

    //percent of pixels for speeds
    protected static final double PERCSPEED   = 0.0046875;
    protected static final double PERCSPDMISL = 0.0156250;

    //Constants related to Audio
    protected static final int STREAMTYPE = AudioManager.STREAM_MUSIC;
    protected static final int MAX_STREAMS = 10;

    //game frames per second
    protected static final int FPS = 60;

    /**
     * Constructor by parameter
     * @param tS - textSize
     * @param eS - enemySpeed
     * @param mS - missilSpeed
     * @param sew - scaledEnemyWidth
     * @param seh - scaledEnemyHeight
     * @param liw - lifeIconWP
     * @param lih - lifeIconHP
     * @param ltw - lifeTextWP
     * @param lth - lifeTextHP
     * @param lvtw - levelTextWP
     * @param lvth - levelTextHP
     * @param etw - enemiesTextWP
     * @param eth - enemiesTextHP
     * @param stw - startTextWP
     * @param sth - startTextHP
     * @param luw - levelupWP
     * @param luh - levelupHP
     * @param w - width screen running
     * @param h - height screen running
     * @param sx - scale factor X
     * @param sy - scale factor Y
     */
    public Constants(int tS, int eS, int mS, int liw, int lih, int ltw, int lth, int lvtw, int lvth, int etw, int eth, int stw, int sth, int luw, int luh, int w, int h, float sx, float sy) {
        this.textSize    = tS;
        this.enemySpeed  = eS;
        this.missilSpeed = mS;
        this.lifeIconWP  = liw;
        this.lifeIconHP  = lih;
        this.lifeTextWP  = ltw;
        this.lifeTextHP  = lth;
        this.levelTextWP = lvtw;
        this.levelTextHP = lvth;
        this.startTextWP = stw;
        this.startTextHP = sth;
        this.levelupWP   = luw;
        this.levelupHP   = luh;
        this.enemiesTextWP = etw;
        this.enemiesTextHP = eth;
        this.width  = w;
        this.height = h;
        this.scaleX = sx;
        this.scaleY = sy;
        this.scaledEnemyWidth  = (int) (EWIDTH * scaleX);
        this.scaledEnemyHeight = (int) (EHEIGHT * scaleY);
        this.heightToLose = (int) (this.height - (100 * scaleY));
        this.positionHitWall = (int) (this.width - (BHEIGHT * scaleX));
        this.scaledExplosionWidth = (int) (EXHEIGHT * scaleX);
        this.scaledExplosionHeight = (int) (EXHEIGHT * scaleY);
    }
}
