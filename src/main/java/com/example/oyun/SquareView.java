package com.example.oyun;

import android.content.Context;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

import android.graphics.Typeface;
import android.graphics.RectF;

import android.view.View;

import android.view.animation.AnticipateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.CycleInterpolator;

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

    private SquareAnimationListener listener;

    private static final int TEXT_SIZE = 50;
    private static final int START_COLOR = 0xFFFFFFFF;
    private static final int SHADOW_COLOR = 0xFFCCCCCC;

    private static final int TEXT_COLOR = 0xFFBF0000;
    private static final int TEXT_SHADOW_COLOR = 0xFF7F0000;




    private float maxRotateShake = 8;
    private float maxRotateDegrees = 45;
    private long vanishScaleDelay = 100;

    private RectF shadowBounds;
    private RectF squareBounds;
    private float textSize;

    private Bitmap tempBitmap;
    private Canvas tempCanvas;

    private Paint shadowPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    private Paint squarePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint alphaPaint = new Paint(Paint.ANTI_ALIAS_FLAG |
                                         Paint.FILTER_BITMAP_FLAG |
                                         Paint.DITHER_FLAG);

    private String mLetter;

    private boolean isMarked = false;
    private boolean isVisible = true;

    private ValueAnimator textAnimator;
    private ValueAnimator squareAnimator;


    private ValueAnimator rotateAnimator;
    private ValueAnimator scaleAnimator;
    private AnimatorSet vanishAnimatorSet;

    private ValueAnimator scaleOutAnimator;
    private ValueAnimator alphaOnAnimator;

    private AnimatorSet popAnimatorSet;

    private ValueAnimator shakeAnimator;

    private ValueAnimator markAnimator;

    private float rotateProgress = 0f;
    private float scaleProgress = 1f;
    private float alphaProgress = 1f;

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

        shadowPaint.setStyle(Paint.Style.FILL);
        shadowPaint.setColor(SHADOW_COLOR);

        textPaint.setColor(TEXT_COLOR);
        Typeface typeface = Typeface.create(Typeface.SANS_SERIF, Typeface.NORMAL);
        textPaint.setTypeface(typeface);

        final ArgbEvaluator argbEvaluator = new ArgbEvaluator();

        squareAnimator = ValueAnimator.ofObject(argbEvaluator, START_COLOR, TEXT_COLOR).setDuration(150);
        textAnimator = ValueAnimator.ofObject(argbEvaluator, TEXT_COLOR, START_COLOR).setDuration(150);

        squareAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                public void onAnimationUpdate(ValueAnimator animator) {
                    int value = (int) animator.getAnimatedValue();
                    setSquareColor(value);
                }
            });

        squareAnimator.addListener(new AnimatorListenerAdapter() {
                public void onAnimationEnd(Animator animator) {
                    if (isMarked) {
                        shadowPaint.setColor(TEXT_SHADOW_COLOR);
                    } else {
                        shadowPaint.setColor(SHADOW_COLOR);
                    }
                }
            });

        textAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                public void onAnimationUpdate(ValueAnimator animator) {
                    int value = (int) animator.getAnimatedValue();
                    setTextColor(value);
                }
            });

        rotateAnimator = ValueAnimator.ofFloat(0f, 1f);
        rotateAnimator.setDuration(300L + vanishScaleDelay);

        rotateAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                public void onAnimationUpdate(ValueAnimator animator) {
                    float value = (float) animator.getAnimatedValue();
                    setRotateProgress(value * maxRotateDegrees);
                }                
            });

        scaleAnimator = ValueAnimator.ofFloat(1f, 0f);
        scaleAnimator.setStartDelay(vanishScaleDelay);

        scaleAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                public void onAnimationUpdate(ValueAnimator animator) {
                    float value = (float) animator.getAnimatedValue();
                    setScaleProgress(value);
                }                
            });

        vanishAnimatorSet = new AnimatorSet();
        vanishAnimatorSet.addListener(new AnimatorListenerAdapter() {
                public void onAnimationEnd(Animator animation) {
                    if (listener != null) {
                        listener.onSquareVanish();
                    }
                }
            });

        vanishAnimatorSet.setDuration(300);
        vanishAnimatorSet.playTogether(rotateAnimator,
                                       scaleAnimator);


        scaleOutAnimator = ValueAnimator.ofFloat(0f, 1f);

        scaleOutAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                public void onAnimationUpdate(ValueAnimator animator) {
                    float value = (float) animator.getAnimatedValue();
                    setScaleProgress(value);
                }
            });

        alphaOnAnimator = ValueAnimator.ofFloat(0f, 1f);
        // alphaOnAnimator.setDuration(500);
        // alphaOnAnimator.setInterpolator(new AccelerateInterpolator(2));

        alphaOnAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                public void onAnimationUpdate(ValueAnimator animator) {
                    float value = (float) animator.getAnimatedValue();
                    setAlphaProgress(value);
                }
            });

        popAnimatorSet = new AnimatorSet();
        popAnimatorSet.playTogether(scaleOutAnimator,
                                    alphaOnAnimator);

        popAnimatorSet.setDuration(300);
        // popAnimator.setInterpolator(new DecelerateInterpolator(2));

        popAnimatorSet.addListener(new AnimatorListenerAdapter() {
                public void onAnimationEnd(Animator animation) {
                    if (listener != null) {
                        listener.onSquarePop();
                    }
                }
            });

        shakeAnimator = ValueAnimator.ofFloat(0f, 1f);
        shakeAnimator.setDuration(300);
        shakeAnimator.setInterpolator(new CycleInterpolator(3));

        shakeAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                public void onAnimationUpdate(ValueAnimator animator) {
                    float value = (float) animator.getAnimatedValue();
                    setRotateProgress(value * maxRotateShake);
                }
            });

        markAnimator = ValueAnimator.ofFloat(1f, 0.9f);
        // markAnimator.setDuration(1000);
        markAnimator.setInterpolator(new CycleInterpolator(0.5f));

        markAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                public void onAnimationUpdate(ValueAnimator animator) {
                    float value = (float) animator.getAnimatedValue();
                    // bunny bounce
                    // 1.1 -> 0.9
                    if (value > 1f) {
                        value = 2f - value;
                    }
                    setScaleProgress(value);
                }
            });
    }

    public boolean getVisible() {
        return isVisible;
    }

    public boolean getMarked() {
        return isMarked;
    }

    public String getMark() {
        return mLetter;
    }

    public void setMark(boolean marked) {
        if (isMarked != marked) {
            isMarked = marked;
            invalidateAnimation();
        }

    }

    public void setVisible(boolean visible) {
        if (isVisible != visible) {
            isVisible = visible;

            if (!isVisible) {
                vanishAnimatorSet.start();
            }
        }
    }

    public void setText(String s) {
        this.mLetter = s;
        invalidate();
    }

    public void popText(String s) {
        this.mLetter = s;
        isVisible = true;
        rotateProgress = 0;

        popAnimatorSet.start();
    }

    public void popDelay(long delay) {
        popAnimatorSet.setStartDelay(delay);
    }

    public void shake() {
        shakeAnimator.start();
    }

    public void setRotateProgress(float rotateProgress) {
        this.rotateProgress = rotateProgress;
        postInvalidate();
    }

    public void setScaleProgress(float scaleProgress) {
        this.scaleProgress = scaleProgress;
        postInvalidate();
    }

    public void setAlphaProgress(float alphaProgress) {
        this.alphaProgress = alphaProgress;
        postInvalidate();
    }

    private void invalidateAnimation() {
        if (isMarked) {
            squareAnimator.start();
            textAnimator.start();
            markAnimator.start();
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
        squareBounds = new RectF(0, 0, w, h - 5);
        shadowBounds = new RectF(0, 0, w, h);
        textSize = TEXT_SIZE * getResources().getDisplayMetrics().scaledDensity;
        tempBitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
        tempCanvas = new Canvas(tempBitmap);
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        float width = squareBounds.width();
        float height = squareBounds.height();

        textPaint.setTextSize(this.textSize);
        textPaint.setTextAlign(Paint.Align.CENTER);

        //tempCanvas.drawRoundRect(squareBounds, 30, 30, squarePaint);
        tempCanvas.drawOval(shadowBounds, shadowPaint);
        tempCanvas.drawOval(squareBounds, squarePaint);

        tempCanvas.drawText(mLetter, width / 2f, height / 2f - ((textPaint.descent() + textPaint.ascent()) / 2f), textPaint);

        canvas.rotate(rotateProgress, width / 2f, height / 2f);

        canvas.scale(scaleProgress, scaleProgress, width / 2f, height / 2f);

        alphaPaint.setAlpha((int)(alphaProgress * 0xFF));

        canvas.drawBitmap(tempBitmap, 0, 0, alphaPaint);
    }

    public void setAnimationListener(SquareAnimationListener listener) {
        this.listener = listener;
    }

    public interface SquareAnimationListener {
        public void onSquarePop();
        public void onSquareVanish();
    }
}

