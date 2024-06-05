package com.example.runningapplication.View;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

public class CircularStatView extends View {
    private Paint circlePaint;
    private Paint arcPaint;
    private RectF rectF;
    private float percentage;
    private Bitmap centerImage;

    public CircularStatView(Context context) {
        super(context);
        init();
    }

    public CircularStatView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CircularStatView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    // 初始化画笔和其他成员变量
    private void init() {
        // 初始化背景圆画笔
        circlePaint = new Paint();
        circlePaint.setColor(Color.GRAY);
        circlePaint.setStyle(Paint.Style.STROKE);
        circlePaint.setStrokeWidth(50);
        circlePaint.setAntiAlias(true);

        // 初始化弧形画笔
        arcPaint = new Paint();
        arcPaint.setColor(Color.GREEN);
        arcPaint.setStyle(Paint.Style.STROKE);
        arcPaint.setStrokeWidth(50);
        arcPaint.setAntiAlias(true);

        rectF = new RectF();
        percentage = 0;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float width = getWidth();
        float height = getHeight();
        float radius = Math.min(width, height) / 2 - 60;
        rectF.set(width / 2 - radius, height / 2 - radius, width / 2 + radius, height / 2 + radius);

        // 绘制背景圆
        canvas.drawCircle(width / 2, height / 2, radius, circlePaint);

        // 根据百分比绘制弧形
        canvas.drawArc(rectF, -90, percentage * 360 / 100, false, arcPaint);

        // 绘制中心图片
        if (centerImage != null) {
            float imageRadius = radius / 2;
            float left = (width / 2) - imageRadius;
            float top = (height / 2) - imageRadius;
            float right = (width / 2) + imageRadius;
            float bottom = (height / 2) + imageRadius;
            canvas.drawBitmap(centerImage, null, new RectF(left, top, right, bottom), null);
        }
    }

    // 设置百分比，并重绘视图
    public void setPercentage(float percentage) {
        this.percentage = percentage;
        invalidate();
    }

    // 设置中心图片，并重绘视图
    public void setCenterImage(Bitmap bitmap) {
        this.centerImage = bitmap;
        invalidate();
    }
}

