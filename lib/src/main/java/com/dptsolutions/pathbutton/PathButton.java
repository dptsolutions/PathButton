package com.dptsolutions.pathbutton;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.os.Build;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.Button;

/**
 * A Button that draws a Path around the edges of itself, with
 * curved ends. Path is the same color as the color of the text
 * of the button, and defaults to 2dp width.
 */
public class PathButton extends Button {
    private static final int DEFAULT_STROKE_WIDTH_DP = 2;

    public PathButton(Context context) {
        super(context);
        init(context, null, 0, 0);
    }

    public PathButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0, 0);
    }

    public PathButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr, 0);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public PathButton(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs, defStyleAttr, defStyleRes);
    }

    Paint borderPaint;
    Path borderPath;


    private void init(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        final float defaultStrokeWidth = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, DEFAULT_STROKE_WIDTH_DP, context.getResources().getDisplayMetrics());
        float strokeWidth = defaultStrokeWidth;

        if(attrs != null) {
            TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.PathButton, defStyleAttr, defStyleRes);
            try{
                strokeWidth = a.getDimension(R.styleable.PathButton_borderWidth, defaultStrokeWidth);
            } finally {
                a.recycle();
            }
        }

        borderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        borderPaint.setStyle(Paint.Style.STROKE);
        borderPaint.setStrokeWidth(strokeWidth);
        borderPaint.setColor(getCurrentTextColor());
        borderPath = new Path();

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);


        float strokeWidth = borderPaint.getStrokeWidth();
        float radius = (float)( (h - (2 * strokeWidth)) / 2.0);
        RectF leftArcRect = new RectF();
        RectF rightArcRect = new RectF();

        leftArcRect.set(strokeWidth, strokeWidth, strokeWidth + radius * 2, h - strokeWidth);
        rightArcRect.set(w - strokeWidth - (2 * radius), strokeWidth, w - strokeWidth, h - strokeWidth);


        borderPath.reset();
        borderPath.addArc(leftArcRect, 90, 180);
        borderPath.addArc(rightArcRect, 270, 180);
        borderPath.moveTo(strokeWidth + radius, strokeWidth);
        borderPath.lineTo(w - strokeWidth - radius, strokeWidth);
        borderPath.moveTo(strokeWidth + radius, h - strokeWidth);
        borderPath.lineTo(w - strokeWidth - radius, h - strokeWidth);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawPath(borderPath, borderPaint);


    }


}
