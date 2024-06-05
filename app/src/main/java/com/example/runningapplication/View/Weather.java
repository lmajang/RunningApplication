package com.example.runningapplication.View;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

public class Weather extends View {
    private Bitmap centerImage;
    public Weather(Context context) {
        super(context);
        init();
    }
    public Weather(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public Weather(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }
    private void init() {}
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float width = getWidth();
        float height = getHeight();
        float radius = Math.min(width, height) / 2 - 60;
        if (centerImage != null) {
            float imageRadius = radius / 2;
            float left = (width / 2) - imageRadius;
            float top = (height / 2) - imageRadius;
            float right = (width / 2) + imageRadius;
            float bottom = (height / 2) + imageRadius;
            canvas.drawBitmap(centerImage, null, new RectF(left, top, right, bottom), null);
        }
    }
    public void setCenterImage(Bitmap bitmap) {
        this.centerImage = bitmap;
        invalidate();
    }
}
