package ru.hackaton.liquidator;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

public class SimpleBottleView extends View  {

	public SimpleBottleView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	float OFFSET_X = 50;
	float OFFSET_Y = 50;
	
	final float MAX_VOLUME = 60000;
	float mVolume = 25000;
	double mAngle = Math.PI / 8 ;
	
	float mWidth = 200;
	float mHeight = 300;
	
	Path mLiquidPath = getLiquidForm();
	Paint mPaint = new Paint();
	
	
	@Override
	public void draw(Canvas canvas) {
		super.draw(canvas);
		mPaint.setColor(Color.WHITE);
		canvas.drawRect(OFFSET_X, OFFSET_Y, mWidth + OFFSET_X, mHeight + OFFSET_Y, mPaint);
		mPaint.setColor(Color.BLUE);
		
		if (mLiquidPath != null) {
			canvas.drawPath(mLiquidPath, mPaint);
		}
	}
	
	// 0 - 100
	public void setAnglePercent(int percent) {
		mAngle = Math.PI / 2 - ((Math.PI / 2)*percent/100);
		mLiquidPath = getLiquidForm();
		invalidate();
	}
	
	public void setVolumePercent(int percent) {
		// 0 - 0.5;
		float quant = MAX_VOLUME / 200;
		mVolume = quant * percent;
		mLiquidPath = getLiquidForm();
		invalidate();
	}
	
	Path getLiquidForm() {  
		float b = (float) (mVolume / mWidth + mWidth / (Math.tan(mAngle) * 2));
		float a = (float) (b - mWidth / Math.tan(mAngle));
		Path p = new Path();
		
		if (b <= mHeight) {
			if (a >= 0) { //bottom trapecy
				p.setLastPoint(mWidth + OFFSET_X, mHeight + OFFSET_Y);
				p.lineTo(OFFSET_X, mHeight + OFFSET_Y);
				p.lineTo(OFFSET_X, mHeight + OFFSET_Y - a);
				p.lineTo(mWidth + OFFSET_X, mHeight + OFFSET_Y - b);
				p.close();
				return p;
			} else {  //  triangle
				 a = (float) Math.sqrt(2 * mVolume / Math.tan(mAngle));
				 b = (float) (a * Math.tan(mAngle));
				 p.setLastPoint(mWidth + OFFSET_X, mHeight + OFFSET_Y);
				 p.lineTo(OFFSET_X + mWidth - b, mHeight + OFFSET_Y);
				 p.lineTo(OFFSET_X + mWidth, mHeight + OFFSET_Y - a);
				 p.close();
				 return p;
			}
		} else { // upper trapecy
			double modAngle = Math.PI/2 - mAngle;
			b = (float) (mVolume / mHeight + mHeight / (Math.tan(modAngle) * 2));
			a = (float) (b - mHeight / Math.tan(modAngle));
			Log.d("!!!!", a+ "   "+b);
			p.setLastPoint(mWidth + OFFSET_X, mHeight + OFFSET_Y);
			p.lineTo(OFFSET_X + mWidth - b, mHeight + OFFSET_Y);
			p.lineTo(OFFSET_X + mWidth - a, OFFSET_Y);
			p.lineTo(OFFSET_X + mWidth, OFFSET_Y);
			p.close();
			return p;
		}
	} 
}
