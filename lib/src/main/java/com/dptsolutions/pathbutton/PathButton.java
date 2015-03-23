package com.dptsolutions.pathbutton;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.Button;

/**
 * A Button that draws a Path around the edges of itself, with
 * curved ends. Path is the same color as the color of the text
 * of the button, and defaults to 2dp width. Area inside path can
 * be filled with a different color from the border.
 */
public class PathButton extends Button {

    public PathButton(Context context) {
        this(context, null);
    }

    public PathButton(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.pathButtonStyle);
    }

    public PathButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs, defStyleAttr, R.style.PathButton);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public PathButton(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs, defStyleAttr, defStyleRes);
    }

    private Paint borderPaint;
    private Path borderPath;
    private Paint fillPaint;
    private Path fillPath;
    private ColorStateList fillColors;
    private int currentFillColor;

    private void init(AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        final int defaultStrokeWidth = getResources().getDimensionPixelSize(R.dimen.path_button_default_border_width);

        //Initialize default values in case there are no attrs
        int strokeWidth = defaultStrokeWidth;
        fillColors = getResources().getColorStateList(R.color.default_fill_colors);

        if(attrs != null) {
            TypedArray a = getContext().getTheme().obtainStyledAttributes(attrs, R.styleable.PathButton, defStyleAttr, defStyleRes);
            try {
                strokeWidth = a.getDimensionPixelSize(R.styleable.PathButton_borderWidth, defaultStrokeWidth);

                final ColorStateList fillColorAttrValue = a.getColorStateList(R.styleable.PathButton_fillColor);
                if(fillColorAttrValue != null) {
                    fillColors = fillColorAttrValue;
                }

            } finally {
                a.recycle();
            }
        }

        //If fillColors isn't stateful, then the code to update the color will never fire
        //in drawableStateChange. So initialize currentFillColor to the default color
        //(the only color in fillColors)
        if(!fillColors.isStateful()) {
            currentFillColor = fillColors.getDefaultColor();
        }

        borderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        borderPaint.setStyle(Paint.Style.STROKE);
        borderPaint.setStrokeWidth(strokeWidth);
        borderPath = new Path();

        fillPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        fillPaint.setStyle(Paint.Style.FILL);
        fillPath = new Path();
    }

    private void updateFillColor() {
        boolean shouldInvalidate = false;

        //Only invalidate if fill color has changed
        int color = fillColors.getColorForState(getDrawableState(), 0);
        if(currentFillColor != color) {
            currentFillColor = color;
            shouldInvalidate = true;
        }

        if(shouldInvalidate) {
            invalidate();
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        final float strokeWidth = borderPaint.getStrokeWidth();
        final float radius = (float)( (h - (2 * strokeWidth)) / 2.0);
        final RectF outerLeftArcRect = new RectF(strokeWidth, strokeWidth, strokeWidth + radius * 2, h - strokeWidth);
        final RectF outerRightArcRect = new RectF(w - strokeWidth - (2 * radius), strokeWidth, w - strokeWidth, h - strokeWidth);

        borderPath.reset();
        borderPath.addArc(outerLeftArcRect, 90, 180);
        borderPath.addArc(outerRightArcRect, 270, 180);
        borderPath.moveTo(strokeWidth + radius, strokeWidth);
        borderPath.lineTo(w - strokeWidth - radius, strokeWidth);
        borderPath.moveTo(strokeWidth + radius, h - strokeWidth);
        borderPath.lineTo(w - strokeWidth - radius, h - strokeWidth);

        fillPath.reset();
        fillPath.addArc(outerLeftArcRect, 90, 180);
        fillPath.addArc(outerRightArcRect, 270, 180);
        fillPath.moveTo(strokeWidth + radius, strokeWidth);
        fillPath.lineTo(w - strokeWidth - radius, strokeWidth);
        fillPath.lineTo(w - strokeWidth - radius, h - strokeWidth);
        fillPath.lineTo(strokeWidth + radius, h - strokeWidth);
        fillPath.lineTo(strokeWidth + radius, strokeWidth);
        fillPath.close();
    }

    @Override
    protected void onDraw(Canvas canvas) {

        fillPaint.setColor(currentFillColor);
        canvas.drawPath(fillPath, fillPaint);

        borderPaint.setColor(getCurrentTextColor());
        canvas.drawPath(borderPath, borderPaint);

        super.onDraw(canvas);
    }

    @Override
    protected void drawableStateChanged() {
        super.drawableStateChanged();

        //Update the current fill color only if it can change
        if(fillColors != null && fillColors.isStateful()) {
            updateFillColor();
        }
    }

    /**
     * Set the width of the border of the PathButton. Cannot be less than 2dp
     *
     * @see #getBorderWidth()
     *
     * @param pixels Width of the border to set, in pixels
     */
    public void setBorderWidth(int pixels) {
        final int defaultStrokeWidth = getContext().getResources().getDimensionPixelSize(R.dimen.path_button_default_border_width);
        int strokeWidth = pixels < defaultStrokeWidth ? defaultStrokeWidth : pixels;
        borderPaint.setStrokeWidth(strokeWidth);
        invalidate();
    }
    /**
     * Get the width of the border of the PathButton
     *
     * @see #setBorderWidth(int)
     *
     * @return Width of the border, in pixels
     */
    public int getBorderWidth() {
        return (int) borderPaint.getStrokeWidth();
    }
    /**
     * Gets the fill colors for the different states (normal, pressed, focused) of the PathButton.
     *
     * @see #setFillColors(android.content.res.ColorStateList)
     * @see #setFillColor(int)
     */
    public ColorStateList getFillColors() {
        return fillColors;
    }
    /**
     * Sets the fill colors for the different states (normal, pressed, focused) of the PathButton.
     *
     * @see #getFillColors()
     * @see #setFillColor(int)
     */
    public void setFillColors(ColorStateList colors) {
        if(colors == null) {
            throw new NullPointerException();
        }
        fillColors = colors;
        updateFillColor();
    }
    /**
     * Sets the fill color for all the states (normal, pressed, focused) to be this color.
     *
     * @see #setFillColor(int)
     * @see #getFillColors()
     */
    public void setFillColor(int color) {
        fillColors = ColorStateList.valueOf(color);
        updateFillColor();
    }

}
