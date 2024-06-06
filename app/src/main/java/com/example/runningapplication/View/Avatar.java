package com.example.runningapplication.View;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;

public class Avatar extends View {
    private Paint paint;
    private Paint borderPaint;
    private Bitmap bitmap;
    private BitmapShader shader;
    private int borderWidth = 10; // 边框宽度
    private int borderColor = 0xFF000000; // 边框颜色，黑色

    public Avatar(Context context) {
        super(context);
        init();
    }

    public Avatar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public Avatar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    // 初始化画笔和路径
    private void init() {
        paint = new Paint();
        paint.setAntiAlias(true);
        borderPaint = new Paint();
        borderPaint.setAntiAlias(true);
        borderPaint.setStyle(Paint.Style.STROKE);
        borderPaint.setStrokeWidth(borderWidth);
        borderPaint.setColor(borderColor);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (bitmap != null) {
            int width = getWidth();
            int height = getHeight();
            float radius = Math.min(width, height) / 2.0f-20;

            if (shader == null) {
                shader = new BitmapShader(Bitmap.createScaledBitmap(bitmap, width, height, false), Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
                paint.setShader(shader);
            }
            canvas.drawCircle(width / 2.0f, height / 2.0f, radius, paint);
            // 绘制边框
            canvas.drawCircle(width / 2.0f, height / 2.0f, radius + borderWidth / 2.0f, borderPaint);
        }
    }

    // 设置图片，并重绘视图
    public void setImageBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
        this.shader = null; // 需要重新创建Shader
        invalidate();
    }
}

