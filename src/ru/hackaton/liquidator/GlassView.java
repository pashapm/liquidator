package ru.hackaton.liquidator;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.ImageView;

public class GlassView extends ImageView {
    private Glass glass;

    public GlassView(Context context) {
        super(context);
    }

    public GlassView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public GlassView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
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
        getBackground().setLevel(glass.getPercent() * 10000 / 100);
//        canvas.drawText(glass.getPercent() + "%, " + glass.getState(), 100, 100, paint);
    }
}
