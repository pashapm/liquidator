package ru.hackaton.liquidator;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
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

        Handler handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                addItem(5, 360);
                SystemClock.sleep(16);
                sendEmptyMessage(0);
            }
        };

        handler.sendEmptyMessage(0);

    }

    private void addItem(final int angle, final int position) {
        View rootView = findViewById(android.R.id.content);
        rootView.post(new Runnable() {
            @Override
            public void run() {
                updateView(glassView1, position, angle);
                updateView(glassView2, position, angle);
                updateView(glassView3, position, angle);
            }
        });
    }

    private void updateView(GlassView2 glassView, int position, int angle) {
        int left = glassView.getLeft();
        int right = glassView.getRight();
        glassView.setWaterPositionAndWidth(position - left, angle);
        if (left <= position && position <= right) {
            glassView.getGlass().add((int) (angle * 1.5));
        }
        glassView.invalidate();
    }

}
