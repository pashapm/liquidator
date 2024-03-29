package ru.hackaton.liquidator;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

public class GlassViewTestActivity extends Activity {

    public static final int MSG_DEFAULT = 0;
    private Glass glass;
    private GlassView glassView;

    private Handler uiHandler = new Handler(Looper.getMainLooper()){
        @Override
        public void handleMessage(Message msg) {
            glass.add(1);
            glassView.invalidate();
            sendEmptyMessageDelayed(MSG_DEFAULT, 64);
        }
    };

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_vodka);
        glass = new Glass(250, 50);
        glassView = (GlassView) findViewById(R.id.id_vodka);
        glassView.setGlass(glass);
    }

    @Override
    protected void onResume() {
        super.onResume();
        uiHandler.sendEmptyMessageDelayed(MSG_DEFAULT, 500);
    }

    @Override
    protected void onPause() {
        super.onPause();
        uiHandler.removeMessages(MSG_DEFAULT);
    }
}