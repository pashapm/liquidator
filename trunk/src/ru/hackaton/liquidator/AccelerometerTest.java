package ru.hackaton.liquidator;

import android.app.Activity;
import android.os.Bundle;

public class AccelerometerTest extends Activity {

    private BottleView view;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view = new BottleView(this);
        setContentView(view);
    }

    @Override
    protected void onResume() {
        super.onResume();
        view.startSimulation();
    }

    @Override
    protected void onPause() {
        super.onPause();
        view.stopSimulation();
    }
}