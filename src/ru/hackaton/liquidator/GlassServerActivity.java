package ru.hackaton.liquidator;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;

public class GlassServerActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        startGame();
    }

    private void startGame() {
        setContentView(R.layout.three_glass_layout);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        GlassView glassView1 = (GlassView) findViewById(R.id.glass1);
        Glass glass1 = new Glass(250, 50);
        glass1.add(100);
        glassView1.setGlass(glass1);

        GlassView glassView2 = (GlassView) findViewById(R.id.glass2);
        Glass glass2 = new Glass(150, 50);
        glass2.add(50);
        glassView2.setGlass(glass2);

        GlassView glassView3 = (GlassView) findViewById(R.id.glass3);
        Glass glass3 = new Glass(250, 50);
        glass3.add(75);
        glassView3.setGlass(glass3);
    }

}
