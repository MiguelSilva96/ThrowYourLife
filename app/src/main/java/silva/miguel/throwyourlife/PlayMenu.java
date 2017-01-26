package silva.miguel.throwyourlife;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

/**
 * Created by Miguel on 14/09/2016.
 */
public class PlayMenu extends Activity {

    SecurePreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prefs = new SecurePreferences(this, "scoreAndLevel", "19041904LaLaLaLaLaLa", true);
        //No title
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //Fullscreen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                             WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.playmenu);
    }
}
