package silva.miguel.throwyourlife;

import android.app.Activity;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;

import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.ImageButton;

/**
 * Created by Miguel on 05/08/2016.
 */
public class Menu extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        final Animation animAlpha;
        animAlpha = AnimationUtils.loadAnimation(this, R.anim.anim_alpha);
        //No title
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //Fullscreen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                             WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.mainmenu);
        setButtons(animAlpha);
    }

    /**
     * Edit and place some buttons and its onclick actions
     * @param animAlpha - animation
     */
    public void setButtons(final Animation animAlpha) {
        ImageButton btnAlpha = (ImageButton)findViewById(R.id.imageButton);
        ImageButton btn = (ImageButton) findViewById(R.id.imageButton2);
        btnAlpha.setOnClickListener(new Button.OnClickListener(){

            @Override
            public void onClick(View arg0) {
                arg0.startAnimation(animAlpha);
                clicked_play(arg0);
            }});
        btn.setOnClickListener(new Button.OnClickListener(){

            @Override
            public void onClick(View arg0) {
                arg0.startAnimation(animAlpha);
                clicked_howtoplay(arg0);
            }});
    }

    /**
     * On click action for play button
     * @param v - View
     */
    public void clicked_play(View v) {
        Intent tyl = new Intent(this, ThrowYourLife.class);
        startActivity(tyl);
    }

    /**
     * On click action for how to play button
     * @param v - View
     */
    public void clicked_howtoplay(View v) {
        Intent htp = new Intent(this, HowToPlay.class);
        startActivity(htp);
    }
}
