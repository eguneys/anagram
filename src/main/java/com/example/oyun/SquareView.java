package com.example.oyun;

import android.content.Context;

import android.graphics.Canvas;
import android.graphics.Paint;

import android.graphics.Typeface;
import android.graphics.RectF;

import android.view.View;

import android.util.AttributeSet;
import android.content.res.TypedArray;

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
        //canvas.drawText("A", width - 10, height - 10, textPaint);
        canvas.drawText(mLetter, width / 2f, height / 2f - ((textPaint.descent() + textPaint.ascent()) / 2f), textPaint);
        //canvas.drawText("A", 10, 10, textPaint);
    }
}