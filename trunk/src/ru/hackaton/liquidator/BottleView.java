package ru.hackaton.liquidator;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.SystemClock;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;

import java.util.Arrays;

public class BottleView extends View implements SensorEventListener {
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;

    private float mXDpi;
    private float mYDpi;
    private float mMetersToPixelsX;
    private float mMetersToPixelsY;
    private float[] values;
    private Paint paint;
    public static final int START_OFFSET = 30;

    public BottleView(Context context) {
        super(context);
        mSensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        mXDpi = metrics.xdpi;
        mYDpi = metrics.ydpi;
        mMetersToPixelsX = mXDpi / 0.0254f;
        mMetersToPixelsY = mYDpi / 0.0254f;

        paint = new Paint();
        paint.setColor(Color.WHITE);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (values == null) {
            return;
        }
        canvas.save();
        canvas.translate(START_OFFSET, START_OFFSET);

        float rawAngle = values[1];
        float angle = - rawAngle * 9;
        double radians = Math.toRadians(angle);
        float sin = (float) Math.sin(radians);
        canvas.drawLine(0, 0, 300, 300 * sin, paint);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        values = sensorEvent.values;
        invalidate();
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void startSimulation() {
        /*
        * It is not necessary to get accelerometer events at a very high
        * rate, by using a slower rate (SENSOR_DELAY_UI), we get an
        * automatic low-pass filter, which "extracts" the gravity component
        * of the acceleration. As an added benefit, we use less power and
        * CPU resources.
        */
        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_UI);
    }

    public void stopSimulation() {
        mSensorManager.unregisterListener(this);
    }

}
