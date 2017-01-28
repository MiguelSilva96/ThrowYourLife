package silva.miguel.throwyourlife;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.Image;
import android.media.SoundPool;
import android.os.Build;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Miguel on 05/07/2016.
 */
public class GamePanel extends SurfaceView implements SurfaceHolder.Callback, Serializable {

    /**
     * index 0 - player
     * index 1 - playerRight
     * index 2 - playerLeft
     * index 3 - enemy
     * index 4 - bullet
     * index 5 - life icon
     * index 6 - explosion
     */
    private Bitmap[] sSprites;
    private MainThread thread;
    private Player player;
    private ArrayList<Missil> missils;
    private ArrayList<Enemy> enemies;
    private ArrayList<Explosion> explosions;
    private AudioManager audioManager;
    private SoundPool soundEffects;
    private float volume;
    private boolean isTouchable;

    private boolean started;
    private boolean levelup;
    private long enemyStartTime;
    private long levelUpStartTime;
    private int speed;
    private int maxLevel;
    private int scoreStart;
    private int hitWallSound, throwSound;
    private int loseSound, levelUpSound;
    private Random rand;
    private Constants constants;

    //Layout and pauseButton for adding and removing
    private ImageButton pauseButton;
    private RelativeLayout rootPanel;

    //Constructor
    public GamePanel(Context context, ImageButton pauseButton, int width, int height) {
        super(context);
        float scaleX, scaleY;
        int textSize, missilSpeed, lifeIconWP, lifeTextHP, 
        	lifeTextWP, lifeIconHP, levelTextWP, levelTextHP,
        	levelupWP, levelupHP, enemiesTextWP, enemiesTextHP, 
        	startTextWP, startTextHP;
        maxLevel = 1;
        scoreStart = 0;
        thread = new MainThread(getHolder(), this);
        rand = new Random();
        sSprites = new Bitmap[7];
        started = levelup = false;
        speed = (int) (height * Constants.PERCSPEED);
        scaleX = (float) width / Constants.WIDTH;
        scaleY = (float) height / Constants.HEIGHT;
        textSize = (int) (40 * (scaleX + scaleY)/2);
        missilSpeed = (int) (height * Constants.PERCSPDMISL);
        lifeIconWP = (int) (10 * scaleX);
        lifeIconHP = (int) (height - (35 * scaleY));
        lifeTextWP = (int) (40 * scaleX);
        lifeTextHP = (int) (height - (10 * scaleY));
        levelTextWP = (int) (width - (220 * scaleX));
        levelupWP = (int) (width/2 - (110 * scaleX));
        enemiesTextWP = (int) (width - (530 * scaleX));
        enemiesTextHP = (int) (70 * scaleY);
        startTextWP = (int) (width/2 - (170 * scaleX));
        startTextHP = height/2;
        levelTextHP = lifeTextHP;
        levelupHP = height/2;
        constants = new Constants(textSize, speed, missilSpeed, 
        	lifeIconWP, lifeIconHP, lifeTextWP, lifeTextHP, levelTextWP, 
        	levelTextHP, enemiesTextWP, enemiesTextHP, startTextWP, 
        	startTextHP, levelupWP, levelupHP, width, height, scaleX, scaleY);
        getHolder().addCallback(this); //Add callback to intercept events
        setFocusable(true); //To control events
        this.pauseButton = pauseButton;
        setupSoundPool();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {}

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        mySurfaceDestroyed();
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        mySurfaceCreated();
    }

    public void mySurfaceDestroyed() {
        long aux;
        if(thread.isRunning()) {
            boolean retry = true;
            int counter = 0;
            while (retry && counter < 1000) {
                counter++;
                try {
                    thread.setRunning(false);
                    thread.join();
                    retry = false;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            aux = System.nanoTime() - enemyStartTime;
            enemyStartTime = aux;
            isTouchable = false;
        }
    }

    public void mySurfaceCreated(){
        if(!thread.isRunning()) {
            if (!started) {
                player = new Player(sSprites[0], 
                	(int) (Constants.CHWIDTH * constants.scaleX),
                    (int) (Constants.CHHEIGHT * constants.scaleY), 
                    scoreStart, maxLevel);
                player.setX((int) ((constants.width - (Constants.CHWIDTH * constants.scaleX)) / 2));
                player.setY((int) (constants.height - Constants.CHHEIGHT * constants.scaleY));
                explosions = new ArrayList<>();
                enemies = new ArrayList<>();
                missils = new ArrayList<>();
            }
            started = true;
            //Start game loop
            if (!thread.isAlive()) {
                thread = new MainThread(getHolder(), this);
                thread.setRunning(true);
                thread.start();
                long aux = enemyStartTime;
                enemyStartTime = System.nanoTime() - aux;
                isTouchable = true;
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        float x, y, ycheck;
        final int width = constants.width;
        final int height = constants.height;
        if(!isTouchable) return super.onTouchEvent(event);
        if(event.getAction() == MotionEvent.ACTION_DOWN) {
            x = event.getX();
            y = event.getY();
            if(!player.getPlaying()) {
                player.setPlaying(true);
                rootPanel.addView(pauseButton);
            }
            else {
                ycheck = (event.getY() * Constants.HEIGHT) / height;
                if(ycheck > Constants.HEIGHT - 80)
                    y = height - ((80 * height) / Constants.HEIGHT);
                missils.add(new Missil(sSprites[4], x, y, width, height,
                	(int) (Constants.BHEIGHT * constants.scaleX), 
                	(int) (Constants.BHEIGHT * constants.scaleY), 5, 
                	constants.missilSpeed));
                if(x > width / 2)
                    player.setSpritesheet(sSprites[1]);
                else
                    player.setSpritesheet(sSprites[2]);
                soundEffects.play(throwSound, volume, volume, 1, 0, 1f);
                player.decLife();
                }
            return true;
        }
        if(event.getAction() == MotionEvent.ACTION_UP) {
            player.setSpritesheet(sSprites[0]);
            return true;
        }

        return super.onTouchEvent(event);
    }

    public void setRL(RelativeLayout rootPanel) {
        this.rootPanel = rootPanel;
    }

    public boolean checkCollision(GameObject a, GameObject b) {
        return a.getRectange().intersect(b.getRectange());
    }

    public void checkCollisions() {
        for(int i = 0; i < missils.size(); i++) {
            for(int j = 0; j < enemies.size(); j++) {
                Missil m = missils.get(i);
                Enemy e  = enemies.get(j);
                if(checkCollision(m, e)) {
                    if(m.fezTabela())
                        player.incLife();
                    explosions.add(new Explosion(sSprites[6],
                    	(int) e.getX(), (int) e.getY(), 
                    	constants.scaledExplosionWidth, 
                    	constants.scaledExplosionHeight, 
                    	6, constants.scaledEnemyWidth/6, 
                    	constants.scaledEnemyHeight));
                    enemies.remove(j);
                    missils.remove(i);
                    player.enemyKilled();
                }
            }
        }
    }

    public void updateAndCheckCollisions() {
        int enemsize = enemies.size();
        if(player.getLife() < 0) {
            player.setPlaying(false);
            resumeGameObjects();
        }
        for(int i = 0; i < enemsize; i++) {
            Enemy e = enemies.get(i);
            e.update(speed);
            if(e.getY() >= constants.heightToLose) {
                enemies.remove(i);
                player.setPlaying(false);
                resumeGameObjects();
                break;
            }
        }
        for(int i = 0; i < missils.size(); i++) {
            Missil m = missils.get(i);
            m.update();
            double mx = m.getX();
            if(m.getY() <= -20) {
                missils.remove(i);
            }
            else {
                if(mx <= 0 || mx >= constants.positionHitWall) {
                    m.inverteDeclive();
                    m.setTabela(true);
                }
            }
        }
        checkCollisions();
    }

    public void updateLevelByScore() {
        int score = player.getScore();
        int level = player.getLevel();
        if(score >= (100 + (20*(level - 1))) + scoreStart) {
            levelup = true;
            player.incLevel();
            player.setLife(9 + player.getLevel());
            levelUpStartTime = System.nanoTime();
            if(level % 5 == 0)
                speed++;
            if(level + 1 > maxLevel)
                scoreStart = player.getScore();
        }
    }

    public void update() {
        if(player.getPlaying()) {
            addEnemies();
            updateAndCheckCollisions();
            for(int i = 0; i < explosions.size(); i++) {
                Explosion e = explosions.get(i);
                e.update();
                if(e.remove())
                    explosions.remove(i);
            }
            updateLevelByScore();
        }
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        if (canvas != null) {
            canvas.drawColor(0, PorterDuff.Mode.CLEAR);
            player.draw(canvas);
            for(Enemy e : enemies)
                e.draw(canvas);
            for(Missil m : missils)
                m.draw(canvas);
            for(Explosion e : explosions)
                e.draw(canvas);
            drawLifeAndLevel(canvas);
        }
    }

    public void drawStroke(Canvas canvas, int aimScore, Typeface plain) {
        Paint paintStroke = new Paint();
        int stroke = (int) (2 * (constants.scaleX+constants.scaleY/2));
        paintStroke.setColor(Color.BLACK);
        paintStroke.setTextSize(constants.textSize);
        paintStroke.setStyle(Paint.Style.STROKE);
        paintStroke.setStrokeWidth(stroke);
        paintStroke.setTypeface(Typeface.create(plain, Typeface.BOLD));
        if(player.getPlaying()) {
            canvas.drawText(" " + player.getLife(),
                    constants.lifeTextWP,
                    constants.lifeTextHP, paintStroke);
            canvas.drawText("LEVEL " + player.getLevel(),
                    constants.levelTextWP,
                    constants.levelTextHP, paintStroke);
            canvas.drawText("Enemies left: " + (aimScore - player.getScore())/10,
                    constants.enemiesTextWP,
                    constants.enemiesTextHP, paintStroke);
        }
        else
            canvas.drawText("Touch to start!",
                    constants.startTextWP,
                    constants.startTextHP, paintStroke);
    }

    public void drawLifeAndLevel(Canvas canvas) {
        Paint paint  = new Paint();
        int level    = player.getLevel();
        int aimScore = (100 + (20*(level - 1))) + scoreStart;
        paint.setColor(Color.argb(255,255,255,51));
        paint.setTextSize(constants.textSize);
        Typeface plain = Typeface.createFromAsset(getContext().getAssets(),
                "fonts/FREEDOM.ttf");
        paint.setTypeface(Typeface.create(plain, Typeface.BOLD));
        if(player.getPlaying()) {
            canvas.drawBitmap(sSprites[5], 
            	constants.lifeIconWP, 
            	constants.lifeIconHP, null);
            canvas.drawText(" " + player.getLife(), 
            	constants.lifeTextWP, 
            	constants.lifeTextHP, paint);
            canvas.drawText("LEVEL " + player.getLevel(), 
            	constants.levelTextWP, 
            	constants.levelTextHP, paint);
            canvas.drawText("Enemies left: " + (aimScore - player.getScore())/10, 
            	constants.enemiesTextWP, 
            	constants.enemiesTextHP, paint);
        }
        else
            canvas.drawText("Touch to start!", 
            	constants.startTextWP, 
            	constants.startTextHP, paint);
        if(levelup) {
            if((System.nanoTime() - levelUpStartTime)/1000000 >= 2000)
                levelup = false;
            else if(rand.nextInt(2) == 1)
                    canvas.drawText("LEVEL UP!!", 
                    	constants.levelupWP, 
                    	constants.levelupHP, paint);
        }
        drawStroke(canvas, aimScore, plain);
    }

    public void addEnemies() {
        int widthPoint;
        long enemyElapsed;
        int max;
        enemyElapsed = (System.nanoTime() - enemyStartTime)/1000000;
        if(enemyElapsed > (1700 - player.getScore()/20)) {
                max = constants.width - constants.scaledEnemyHeight;
                widthPoint = rand.nextInt(max + 1);
                enemies.add(new Enemy(sSprites[3], widthPoint, -20,
                        constants.scaledEnemyWidth/6, 
                        constants.scaledEnemyHeight, 
                        6, speed));
                enemyStartTime = System.nanoTime();
        }
    }

    public void resumeGameObjects() {
        int level = player.getLevel();
        enemies   = new ArrayList<>();
        missils   = new ArrayList<>();
        explosions = new ArrayList<>();
        enemyStartTime = System.nanoTime();
        player.setLife(9 + player.getLevel());
        player.setScore(scoreStart);
        if(maxLevel < level)
            maxLevel = level;
        rootPanel.post(new Runnable() {
            public void run() {
                rootPanel.removeView(pauseButton);
            }
        });
    }

    private void setupSoundPool() {
        // AudioManager audio settings for adjusting the volume
        Context c = getContext();
        audioManager = (AudioManager) c.getSystemService(c.AUDIO_SERVICE);
        // Current volume Index of particular stream type.
        float currentVolumeIndex = (float) audioManager.getStreamVolume(Constants.STREAMTYPE);
        // Get the maximum volume index for a particular stream type.
        float maxVolumeIndex  = (float) audioManager.getStreamMaxVolume(Constants.STREAMTYPE);
        // Volume (0 --> 1)
        volume = currentVolumeIndex / maxVolumeIndex;
        if (Build.VERSION.SDK_INT >= 21 ) {

            AudioAttributes audioAttrib = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_GAME)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build();

            SoundPool.Builder builder= new SoundPool.Builder();
            builder.setAudioAttributes(audioAttrib).setMaxStreams(Constants.MAX_STREAMS);

            soundEffects = builder.build();
        }
        else {
            // Ja nao se usa, versoes antigas só
            soundEffects = new SoundPool(Constants.MAX_STREAMS, AudioManager.STREAM_MUSIC, 0);
        }
        levelUpSound = soundEffects.load(c, R.raw.levelup,1);
        hitWallSound = soundEffects.load(c, R.raw.hitwall,1);
        throwSound = soundEffects.load(c, R.raw.throwlife,1);
        loseSound = soundEffects.load(c, R.raw.lose,1);
    }

    public void initBitmaps() {
        sSprites[0] = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(),
                R.drawable.characterfront),(int) (Constants.CHWIDTH * constants.scaleX),
                (int) (Constants.CHHEIGHT * constants.scaleY), true);
        sSprites[1] = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(),
                R.drawable.characterright),(int) (Constants.CHWIDTH * constants.scaleX),
                (int) (Constants.CHHEIGHT * constants.scaleY), true);
        sSprites[2] = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(),
                R.drawable.characterleft),(int) (Constants.CHWIDTH * constants.scaleX),
                (int) (Constants.CHHEIGHT * constants.scaleY), true);
        sSprites[3] = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(),
                R.drawable.enemy), constants.scaledEnemyWidth, constants.scaledEnemyHeight, true);
        sSprites[4] = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(),
                R.drawable.bala),(int) (Constants.BWIDTH * constants.scaleX),
                (int) (Constants.BHEIGHT * constants.scaleY), true);
        sSprites[5] = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(),
                R.drawable.iconvida),(int) (Constants.LWIDTH * constants.scaleX),
                (int) (Constants.LHEIGHT * constants.scaleY), true);
        sSprites[6] = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(),
                R.drawable.explosion),(int) (Constants.EXWIDTH * constants.scaleX),
                (int) (Constants.EXHEIGHT * constants.scaleY), true);
    }

}
