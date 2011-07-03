package ru.hackaton.liquidator;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;

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
    }

    private void addItem(int angle, final int position) {
        View rootView = findViewById(android.R.id.content);
        rootView.post(new Runnable() {
            @Override
            public void run() {
                int position1 = position - glassView1.getLeft();
                glassView1.setWaterPosition(position1);
                int position2 = position - glassView2.getLeft();
                glassView2.setWaterPosition(position2);
                int position3 = position - glassView3.getLeft();
                glassView3.setWaterPosition(position3);
            }
        });
    }

    private void updateView(GlassView2 glassView, int position, int angle) {
        glassView.setWaterPosition(position - glassView.getLeft());
        if (glassView.getLeft() >= position && glassView.getRight() <= position) {
            glassView.getGlass().add(angle * 10);
        }
    }

}
