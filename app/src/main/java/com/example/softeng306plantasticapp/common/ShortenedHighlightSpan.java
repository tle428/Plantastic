package com.example.softeng306plantasticapp.common;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.text.style.ReplacementSpan;

import androidx.annotation.NonNull;

public class ShortenedHighlightSpan extends ReplacementSpan {
    private final int backgroundColor;
    private final float reductionFactor;

    public ShortenedHighlightSpan(int backgroundColor, float reductionFactor) {
        this.backgroundColor = backgroundColor;
        // determines how much the height is reduced - 0 = original, 0.5 = half
        this.reductionFactor = reductionFactor;
    }

    @Override
    public int getSize(@NonNull Paint paint, CharSequence text, int start, int end, Paint.FontMetricsInt fm) {
        return Math.round(paint.measureText(text, start, end));
    }

    @Override
    public void draw(@NonNull Canvas canvas, CharSequence text, int start, int end, float x, int top, int y, int bottom, @NonNull Paint paint) {
        float newTop = y - paint.getTextSize() + paint.getTextSize() * reductionFactor / 2;
        float newBottom = y - paint.getTextSize() * reductionFactor / 2;
        int color = paint.getColor();
        paint.setColor(backgroundColor);
        canvas.drawRect(x, newTop, x + getSize(paint, text, start, end, null), newBottom, paint);
        paint.setColor(color);
        canvas.drawText(text, start, end, x, y, paint);
    }
}
