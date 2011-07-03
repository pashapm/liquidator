package ru.hackaton.liquidator;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

public class Bottle extends Activity {
	
	SimpleBottleView mView;
	SeekBar mAngleSeeker;
	SeekBar mVolumeSeeker;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ScrProps.initialize(this);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.bottle);
        mView = (SimpleBottleView) findViewById(R.id.bottle);
        mAngleSeeker = (SeekBar) findViewById(R.id.seekBar1);
        mAngleSeeker.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
			}
			
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
			}
			
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				mView.setAnglePercent(progress);
			}
		});
        
        mVolumeSeeker = (SeekBar) findViewById(R.id.seekBar2);
        mVolumeSeeker.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
			}
			
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
			}
			
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				mView.setVolumePercent(progress);
			}
		});
    }
    
    
}
