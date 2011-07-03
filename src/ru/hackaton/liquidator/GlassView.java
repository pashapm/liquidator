package ru.hackaton.liquidator;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.*;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

public class GlassView extends ImageView {
    public static final int MAX_GLASS = 250;

    private Glass glass;
    private Bitmap fill;

    public GlassView(Context context) {
        this(context, null);
    }

    public GlassView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GlassView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        Resources resources = context.getResources();
        fill = BitmapFactory.decodeResource(resources, R.drawable.glass_fill);
    }

    public void setGlass(Glass glass) {
        this.glass = glass;
    }

    public Glass getGlass() {
        return glass;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (glass == null) {
            return;
        }
        canvas.save();
        int originalHeight = canvas.getHeight();
        float scale = 1.0f * glass.getMaxCapacity() / MAX_GLASS;
        canvas.scale(scale, scale);
        canvas.translate(0, originalHeight * (1 - scale));

        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setFilterBitmap(false);

        paint.setColor(Color.BLACK);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));

        int sc = canvas.saveLayer(new RectF(canvas.getClipBounds()), null,
                Canvas.MATRIX_SAVE_FLAG |
                        Canvas.CLIP_SAVE_FLAG |
                        Canvas.HAS_ALPHA_LAYER_SAVE_FLAG |
                        Canvas.FULL_COLOR_LAYER_SAVE_FLAG |
                        Canvas.CLIP_TO_LAYER_SAVE_FLAG);

        canvas.drawBitmap(fill, 0, 0, null);
        int width = (int) (135 + (glass.getPercent() * 40f / 100));
        int height = fill.getHeight();

        int fillHeight = width / 9;

        int top = height * (100 - glass.getPercent()) / 100;

        canvas.drawRect(new RectF(0, 0, fill.getWidth(), top + fillHeight), paint);

        int dy = (fill.getWidth() - width) / 2 + 4;

        Paint paint1 = new Paint();
        paint1.setColor(Color.parseColor("#70b9af"));
        canvas.drawOval(new RectF(dy, top, dy + width, top + fillHeight * 2), paint1);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
        canvas.drawBitmap(fill, 0, 0, paint);

        canvas.restoreToCount(sc);

        super.onDraw(canvas);
        canvas.restore();
    }
}