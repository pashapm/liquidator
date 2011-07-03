package ru.hackaton.liquidator;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;

public class GlassServerActivity extends Activity {

    private GlassView2 glassView1;
    private GlassView2 glassView2;
    private GlassView2 glassView3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        startGame();
    }

    private void startGame() {
        setContentView(R.layout.three_glass_layout);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        glassView1 = ((GlassView2) findViewById(R.id.glass1));
        glassView2 = ((GlassView2) findViewById(R.id.glass2));
        glassView3 = ((GlassView2) findViewById(R.id.glass3));

        glassView1.setWaterPosition(20);
        glassView2.setWaterPosition(40);
        glassView3.setWaterPosition(60);

    }

}
