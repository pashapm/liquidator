package ru.hackaton.liquidator;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.*;
import android.util.AttributeSet;
import android.view.View;

public class GlassView2 extends View {
    private Glass glass = new Glass(250, 50);
    private Bitmap cupBitmap;
    private Bitmap emptyBitmap;
    private Bitmap fillBitmap;
    private Bitmap glassBitmap;
    private Bitmap waterBitmap;
    private Bitmap waterMaskBitmap;

    public GlassView2(Context context) {
        this(context, null);
    }

    public GlassView2(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GlassView2(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        Resources resources = getResources();
        cupBitmap = BitmapFactory.decodeResource(resources, R.drawable.glass_cup);
        emptyBitmap = BitmapFactory.decodeResource(resources, R.drawable.glass_empty);
        fillBitmap = BitmapFactory.decodeResource(resources, R.drawable.glass_fill);
        glassBitmap = BitmapFactory.decodeResource(resources, R.drawable.glass);
        waterBitmap = BitmapFactory.decodeResource(resources, R.drawable.water);
        waterMaskBitmap = BitmapFactory.decodeResource(resources, R.drawable.water_mask);
        glass.add(125);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(glassBitmap.getWidth(), glassBitmap.getHeight());
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawBitmap(emptyBitmap, 0, 0, null);

        drawFillBitmapCropped(canvas);
        canvas.drawBitmap(cupBitmap, 0, 0, null);
        drawWaterBitmapCropped(canvas);
        canvas.drawBitmap(glassBitmap, 0, 0, null);
    }

    private void drawWaterBitmapCropped(Canvas canvas) {
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setFilterBitmap(false);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));
        paint.setColor(Color.BLACK);

        int sc = canvas.saveLayer(new RectF(canvas.getClipBounds()), null,
                Canvas.MATRIX_SAVE_FLAG |
                        Canvas.CLIP_SAVE_FLAG |
                        Canvas.HAS_ALPHA_LAYER_SAVE_FLAG |
                        Canvas.FULL_COLOR_LAYER_SAVE_FLAG |
                        Canvas.CLIP_TO_LAYER_SAVE_FLAG);

        Rect ovalRect = getOvalRect();
        int top = ovalRect.centerY() - waterBitmap.getHeight() + 55;
        canvas.save();
        float scale = 0.25f;
        canvas.scale(scale, 1f);
        canvas.drawBitmap(waterBitmap, (80 - 10) / scale, top, null);
        canvas.restore();

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
        canvas.drawBitmap(waterMaskBitmap, 0, 0, paint);

        canvas.restoreToCount(sc);

    }

    private void drawFillBitmapCropped(Canvas canvas) {
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

        canvas.drawBitmap(fillBitmap, 0, 0, null);

        Rect ovalRect = getOvalRect();

        canvas.drawRect(new RectF(0, 0, fillBitmap.getWidth(), ovalRect.centerY()), paint);

        Paint paint1 = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint1.setColor(Color.parseColor("#70b9af"));

        canvas.drawOval(new RectF(ovalRect), paint1);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
        canvas.drawBitmap(fillBitmap, 0, 0, paint);

        canvas.restoreToCount(sc);
    }

    private Rect getOvalRect() {
        int width = (int) (135 + (glass.getPercent() * 40f / 100));
        int height = fillBitmap.getHeight();
        int fillHeight = width / 9;
        int top = height * (100 - glass.getPercent()) / 100;
        int dy = (fillBitmap.getWidth() - width) / 2 + 4;
        return new Rect(dy, top, dy + width, top + fillHeight * 2);
    }

}
