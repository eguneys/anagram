package com.example.oyun;

import android.content.Context;

import android.graphics.Canvas;
import android.graphics.Paint;

import android.graphics.Typeface;
import android.graphics.RectF;

import android.view.View;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.animation.ArgbEvaluator;

import android.util.AttributeSet;
import android.content.res.TypedArray;


//TextDrawable https://github.com/amulyakhare/TextDrawable/blob/master/library/src/main/java/com/amulyakhare/textdrawable/TextDrawable.java
public class SquareView extends View
{

    private static final int TEXT_SIZE = 50;
    private static final int TEXT_COLOR = 0xFF000000;
    private static final int START_COLOR = 0xFFFFFFFF;

    private RectF squareBounds;
    private float textSize;
    
    private Paint squarePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    private String mLetter;

    private boolean isMarked;

    private AnimatorSet animatorSet;

    private ValueAnimator textAnimator;
    private ValueAnimator squareAnimator;

    public SquareView(Context context) {
        super(context);
    }

    public SquareView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.getTheme()
            .obtainStyledAttributes(attrs,
                                    R.styleable.SquareView,
                                    0, 0);

        try {
            mLetter = a.getString(R.styleable.SquareView_text);
        } finally {
            a.recycle();
        }
                                                                 
        init();
    }

    private void init() {
        squarePaint.setStyle(Paint.Style.FILL);
        squarePaint.setColor(START_COLOR);

        textPaint.setColor(TEXT_COLOR);
        Typeface typeface = Typeface.create(Typeface.SANS_SERIF, Typeface.NORMAL);
        textPaint.setTypeface(typeface);

        final ArgbEvaluator argbEvaluator = new ArgbEvaluator();

        squareAnimator = ValueAnimator.ofObject(argbEvaluator, START_COLOR, TEXT_COLOR);
        textAnimator = ValueAnimator.ofObject(argbEvaluator, TEXT_COLOR, START_COLOR);

        squareAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                public void onAnimationUpdate(ValueAnimator animator) {
                    int value = (int) animator.getAnimatedValue();
                    setSquareColor(value);
                }
            });

        textAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                public void onAnimationUpdate(ValueAnimator animator) {
                    int value = (int) animator.getAnimatedValue();
                    setTextColor(value);
                }
            });

        //animatorSet = new AnimatorSet();
        //animatorSet.playTogether(squareAnimator, textAnimator);
    }

    public boolean getMarked() {
        return isMarked;
    }

    public String getMark() {
        return mLetter;
    }

    public void setMark() {
        boolean tmp = isMarked;
        isMarked = true;
        if (tmp != isMarked)
            invalidateAnimation();
    }

    public void resetMark() {
        boolean tmp = isMarked;
        isMarked = false;
        if (tmp != isMarked)
            invalidateAnimation();
    }

    public void setText(String s) {
        this.mLetter = s;
        invalidate();
    }

    private void invalidateAnimation() {
        if (isMarked) {
            squareAnimator.start();
            textAnimator.start();
        } else {
            squareAnimator.reverse();
            textAnimator.reverse();            
        }
    }

    public void setTextColor(int color) {
        textPaint.setColor(color);
        invalidate();
    }

    public void setSquareColor(int color) {
        squarePaint.setColor(color);
        invalidate();
    }

    public int getTextColor() {
        return textPaint.getColor();
    }

    public int getSquareColor() {
        return squarePaint.getColor();
    }

    public void setTextSize(int size) {
        this.textSize = TEXT_SIZE * getResources().getDisplayMetrics().scaledDensity;
    }

    protected void onSizeChanged(int w, int h, int oldW, int oldH) {
        squareBounds = new RectF(0, 0, w, h);
        textSize = TEXT_SIZE * getResources().getDisplayMetrics().scaledDensity;
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawRoundRect(squareBounds, 30, 30, squarePaint);

        float width = squareBounds.width();
        float height = squareBounds.height();

        textPaint.setTextSize(this.textSize);
        textPaint.setTextAlign(Paint.Align.CENTER);
        canvas.drawText(mLetter, width / 2f, height / 2f - ((textPaint.descent() + textPaint.ascent()) / 2f), textPaint);
    }
}

