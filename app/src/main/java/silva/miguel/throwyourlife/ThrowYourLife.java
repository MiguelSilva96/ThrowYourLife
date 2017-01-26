package silva.miguel.throwyourlife;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.graphics.Typeface;
import android.media.Image;
import android.os.Bundle;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.view.animation.AnimationUtils;
import android.view.animation.Animation;
import android.widget.RelativeLayout;

public class ThrowYourLife extends Activity {

    private GamePanel surfaceView;
    private boolean paused;
    ImageView bgImagePanel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RelativeLayout rootPanel;
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;
        paused = false;
        ImageButton pauseButton = setPauseButton();
        // No title
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        // Fullscreen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        // Setup SurfaceView
        surfaceView = new GamePanel(this, pauseButton, width, height);
        surfaceView.initBitmaps();
        surfaceView.setZOrderOnTop(true);
        surfaceView.getHolder().setFormat(PixelFormat.TRANSPARENT);
        rootPanel = setRelativeLayout(width, height);
        setContentView(rootPanel);
    }

    /**
     * Setting pause button
     *
     * @return - pause button
     */
    public ImageButton setPauseButton() {
        final Animation animAlpha;
        animAlpha = AnimationUtils.loadAnimation(this, R.anim.anim_alpha);
        ImageButton b = new ImageButton(this);
        RelativeLayout.LayoutParams rl;
        rl = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        rl.addRule(RelativeLayout.ALIGN_BOTTOM);
        b.setLayoutParams(rl);
        b.setBackgroundResource(R.drawable.pausebutton);
        b.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                arg0.startAnimation(animAlpha);
                clicked_Pause(arg0);
            }
        });
        return b;
    }

    /**
     * Set relative Layout to overlap surfaceview and image view that has the background
     *
     * @param width
     * @param height
     * @return - Relative Layout
     */
    public RelativeLayout setRelativeLayout(int width, int height) {
        Bitmap bg = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(),
                R.drawable.bg), width, height, true);
        RelativeLayout rootPanel = new RelativeLayout(this);
        RelativeLayout.LayoutParams fillParentLayout;
        bgImagePanel = new ImageView(this);
        bgImagePanel.setImageBitmap(bg);
        fillParentLayout = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT);
        rootPanel.setLayoutParams(fillParentLayout);
        rootPanel.addView(surfaceView, fillParentLayout);
        rootPanel.addView(bgImagePanel, fillParentLayout);
        surfaceView.setRL(rootPanel);
        return rootPanel;
    }

    public void clicked_Pause(View v) {
        if (!paused) {
            surfaceView.mySurfaceDestroyed();
            paused = true;
        } else {
            surfaceView.mySurfaceCreated();
            paused = false;
        }
    }

    @Override
    public void onPause() {
        surfaceView.mySurfaceDestroyed();
        super.onPause();
    }

    @Override
    public void onResume() {
        surfaceView.mySurfaceCreated();
        super.onResume();
    }
}

