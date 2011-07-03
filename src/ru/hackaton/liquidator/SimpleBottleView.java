package ru.hackaton.liquidator;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

public class SimpleBottleView extends View  {

	float OFFSET_X;
	float OFFSET_Y;
	
	final float WIDTH = 100;
	final float HEIGHT = 300;
	
	final float MAX_VOLUME = WIDTH * HEIGHT;
	float mVolume = 0;
	double mAngle = Math.PI / 8 ;
	
	Path mLiquidPath;
	Paint mPaint = new Paint();
	
	boolean neg = false;
	
	Point mBottleneck;
	
//	public OnPouringListener mListener;
	
	public SimpleBottleView(Context context, AttributeSet attrs) {
		super(context, attrs);
		OFFSET_X = (ScrProps.screenWidth - WIDTH) / 2;
		OFFSET_Y = (ScrProps.screenHeight - HEIGHT) / 2;
		
		int bx = (int) (OFFSET_X + WIDTH/2);
		int by = (int) OFFSET_Y;
		mBottleneck = new Point(bx, by);
	}
	
	@Override
	public void draw(Canvas canvas) {
		super.draw(canvas);
		mPaint.setColor(Color.WHITE);
		canvas.drawRect(OFFSET_X, OFFSET_Y, WIDTH + OFFSET_X, HEIGHT + OFFSET_Y, mPaint);
		mPaint.setColor(Color.BLUE);
		if (mLiquidPath != null) {
			if (neg) {
				Matrix m = new Matrix();
				m.setScale(1, -1, ScrProps.screenWidth / 2,
						ScrProps.screenHeight / 2);
				canvas.setMatrix(m);
				canvas.drawPath(mLiquidPath, mPaint);
			} else {
				canvas.drawPath(mLiquidPath, mPaint);
			}
		}
		
		if (isPouring()) {
			canvas.drawCircle(10, 10, 10, mPaint);
		}
	}
	
	// 0 - 100
	public void setAnglePercent(int percent) {
		mAngle = Math.PI / 2 - ((Math.PI)*percent/100);
		if (mAngle < 0) {
			neg = true;
			mLiquidPath = getLiquidForm(-mAngle);
		} else {
			neg = false;
			mLiquidPath = getLiquidForm(mAngle);
		}
		invalidate();
	}
	
	public void setVolumePercent(int percent) {
		// 0 - 0.5;
		float quant = MAX_VOLUME / 200;
		mVolume = quant * percent;
		mLiquidPath = getLiquidForm(neg ? -mAngle : mAngle);
		invalidate();
	}
	
	boolean pour = false;
	
	Path getLiquidForm(double angle) {
		float b = (float) (mVolume / WIDTH + WIDTH / (Math.tan(angle) * 2));
		float a = (float) (b - WIDTH / Math.tan(angle));
		Path p = new Path();
		
		if (b <= HEIGHT) {
			if (a >= 0) { //bottom trapecy
				
				pour = true;
				
				p.setLastPoint(WIDTH + OFFSET_X, HEIGHT + OFFSET_Y);
				p.lineTo(OFFSET_X, HEIGHT + OFFSET_Y);
				p.lineTo(OFFSET_X, HEIGHT + OFFSET_Y - a);
				p.lineTo(WIDTH + OFFSET_X, HEIGHT + OFFSET_Y - b);
				p.close();
				return p;
			} else {  //  triangle
				 a = (float) Math.sqrt(2 * mVolume / Math.tan(angle));
				 b = (float) (a * Math.tan(angle));
				 
				 if (b >= WIDTH / 2) {
					 pour = true;
				 }
				 
				 p.setLastPoint(WIDTH + OFFSET_X, HEIGHT + OFFSET_Y);
				 p.lineTo(OFFSET_X + WIDTH - b, HEIGHT + OFFSET_Y);
				 p.lineTo(OFFSET_X + WIDTH, HEIGHT + OFFSET_Y - a);
				 p.close();
				 return p;
			}
		} else { // upper trapecy
			double modAngle = Math.PI/2 - angle;
			b = (float) (mVolume / HEIGHT + HEIGHT / (Math.tan(modAngle) * 2));
			a = (float) (b - HEIGHT / Math.tan(modAngle));
			
			if (a >= WIDTH / 2) {
				 pour = true;
			 }
			
			p.setLastPoint(WIDTH + OFFSET_X, HEIGHT + OFFSET_Y);
			p.lineTo(OFFSET_X + WIDTH - b, HEIGHT + OFFSET_Y);
			p.lineTo(OFFSET_X + WIDTH - a, OFFSET_Y);
			p.lineTo(OFFSET_X + WIDTH, OFFSET_Y);
			p.close();
			return p;
		}
	}
	
	boolean isPouring() {
		return neg && pour;
	}
	
//	public static interface OnPouringListener {
//		void pour(int intensity);
//	}
}
