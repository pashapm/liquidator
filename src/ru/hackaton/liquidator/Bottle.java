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

public class Bottle extends Activity {
	
	SimpleBottleView mView;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mView = new SimpleBottleView(this);
        setContentView(mView);
    }
    
    class SimpleBottleView extends View  {

    	float OFFSET_X = 50;
    	float OFFSET_Y = 50;
    	
    	float mFull = 60000;
    	float mVolume = 40000;
    	double mAngle = 2 * Math.PI / 3;
    	
    	float mWidth = 200;
    	float mHeight = 300;
    	
    	double ab[] = getAB();
    	
    	Paint mPaint = new Paint();
    	
		public SimpleBottleView(Context context) {
			super(context);
			Log.d("!!!", getAB()[0] +  "        " + getAB()[1]);
			Log.d("VOLUME: ", (ab[0]*ab[1] / mWidth) + "");
		}
		
		@Override
		public void draw(Canvas canvas) {
			super.draw(canvas);
			mPaint.setColor(Color.WHITE);
			canvas.drawRect(OFFSET_X, OFFSET_Y, mWidth + OFFSET_X, mHeight + OFFSET_Y, mPaint);
			mPaint.setColor(Color.BLUE);
			Path p = new Path();
			
			p.setLastPoint(mWidth + OFFSET_X, mHeight + OFFSET_Y);
			p.lineTo(OFFSET_X, (float) (mHeight + OFFSET_Y));
			p.lineTo(OFFSET_X, (float) (mHeight + OFFSET_Y - ab[0]));
			p.lineTo(mWidth + OFFSET_X, (float) (mHeight + OFFSET_Y - ab[1]));
			p.close();
			canvas.drawPath(p, mPaint);
		}
		
		double[] getAB() {  
//			double Q = mWidth / Math.tan(mAngle);
//			double Z = 2*mVolume / mWidth;
//			double a = (-Q + Math.sqrt(Q*Q + 4*Z)) / 2;
//			double b = a - Q;
//			return new double[] {a, b};
			
			double b = mVolume / mWidth + mWidth / (Math.tan(mAngle) * 2);
			double a = b - mWidth / Math.tan(mAngle);
			return new double[] {b, a};
			
		} 
    	
    }
}
