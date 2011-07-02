package ru.hackaton.liquidator;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

public class GlassView extends View {
    private Paint paint;
    private Glass glass;

    public GlassView(Context context) {
        super(context);
        paint = new Paint();
        paint.setColor(Color.WHITE);
    }

    public void setGlass(Glass glass) {
        this.glass = glass;
    }

    public Glass getGlass() {
        return glass;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (glass == null) {
            System.out.println("GlassView.onDraw");
            return;
        }
        canvas.drawText(glass.getPercent() + "%, " + glass.getState(), 100, 100, paint);
    }
}
