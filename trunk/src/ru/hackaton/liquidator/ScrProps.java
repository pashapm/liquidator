package ru.hackaton.liquidator;


import android.app.Activity;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

public class ScrProps {

	public static int screenHeight;
	public static int screenWidth;

	public static DisplayMetrics mMetrics;
	
	public static void initialize(Activity ctx) {
		Display disp = ((WindowManager) ctx.getSystemService(
				android.content.Context.WINDOW_SERVICE)).getDefaultDisplay();
		ScrProps.screenHeight = disp.getHeight();
		ScrProps.screenWidth = disp.getWidth();
		mMetrics = new DisplayMetrics();
		ctx.getWindowManager().getDefaultDisplay().getMetrics(mMetrics);
	}
	
	public static int scale(int p) {
		return (int) (mMetrics == null ? 0 : p*mMetrics.density);
	}

	public static float scale(float f) {
		return mMetrics == null ? 0 : f*mMetrics.density;
	}
	
	public static float getMultiplier() {
		return mMetrics == null ? 1f : mMetrics.density;
	}
}
