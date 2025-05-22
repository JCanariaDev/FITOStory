package com.example.fitostory;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class CustomPathDrawable extends Drawable {
    private final Paint paint;
    private final Paint strokePaint;
    private final Path path;

    public CustomPathDrawable(int fillColor, int strokeColor, int strokeWidth) {
        paint = new Paint();
        paint.setColor(fillColor);
        paint.setStyle(Paint.Style.FILL);
        paint.setAntiAlias(true);

        strokePaint = new Paint();
        strokePaint.setColor(strokeColor);
        strokePaint.setStyle(Paint.Style.STROKE);
        strokePaint.setStrokeWidth(strokeWidth);
        strokePaint.setAntiAlias(true);

        path = new Path();
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        // Reset path
        path.reset();

        int width = getBounds().width();
        int height = getBounds().height();

        // Scale factors to adjust to any size
        float scaleX = width / 300f;
        float scaleY = height / 220f;

        // Create the path based on the screenshot shape
        path.moveTo(50 * scaleX, 10 * scaleY);

        // Top curve
        path.cubicTo(
                120 * scaleX, 0 * scaleY,
                200 * scaleX, 5 * scaleY,
                250 * scaleX, 30 * scaleY
        );

        // Right side curve
        path.cubicTo(
                280 * scaleX, 55 * scaleY,
                290 * scaleX, 70 * scaleY,
                285 * scaleX, 120 * scaleY
        );

        // Bottom right curve
        path.cubicTo(
                280 * scaleX, 150 * scaleY,
                275 * scaleX, 180 * scaleY,
                250 * scaleX, 200 * scaleY
        );

        // Bottom curve
        path.cubicTo(
                220 * scaleX, 210 * scaleY,
                180 * scaleX, 215 * scaleY,
                120 * scaleX, 210 * scaleY
        );

        // Bottom left curve
        path.cubicTo(
                80 * scaleX, 205 * scaleY,
                50 * scaleX, 195 * scaleY,
                30 * scaleX, 170 * scaleY
        );

        // Left side curve
        path.cubicTo(
                10 * scaleX, 135 * scaleY,
                5 * scaleX, 100 * scaleY,
                20 * scaleX, 50 * scaleY
        );

        // Top left curve
        path.cubicTo(
                25 * scaleX, 30 * scaleY,
                35 * scaleX, 15 * scaleY,
                50 * scaleX, 10 * scaleY
        );

        path.close();

        // Draw fill and stroke
        canvas.drawPath(path, paint);
        canvas.drawPath(path, strokePaint);
    }

    @Override
    public void setAlpha(int alpha) {
        paint.setAlpha(alpha);
        strokePaint.setAlpha(alpha);
        invalidateSelf();
    }

    @Override
    public void setColorFilter(@Nullable ColorFilter colorFilter) {
        paint.setColorFilter(colorFilter);
        strokePaint.setColorFilter(colorFilter);
        invalidateSelf();
    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }

    public void setFillColor(int color) {
        paint.setColor(color);
        invalidateSelf();
    }

    public void setStrokeColor(int color) {
        strokePaint.setColor(color);
        invalidateSelf();
    }
}