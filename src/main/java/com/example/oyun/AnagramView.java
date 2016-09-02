package com.example.oyun;

import android.content.Context;

import android.graphics.Canvas;
import android.graphics.Paint;

import android.graphics.Typeface;
import android.graphics.RectF;
import android.graphics.Rect;


import android.view.View;
import android.view.LayoutInflater;
import android.view.MotionEvent;

import android.widget.GridLayout;


import android.util.AttributeSet;
import android.content.res.TypedArray;

public class AnagramView extends GridLayout
{

    private MarkedSquareListener listener;

    private Rect hitRect = new Rect();
    private SquareView[] ss;

    private java.util.List<Integer> markedSquares;

    public AnagramView(Context context) {
        super(context);
    }

    public AnagramView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.getTheme()
            .obtainStyledAttributes(attrs,
                                    R.styleable.SquareView,
                                    0, 0);

        try {

        } finally {
            a.recycle();
        }
                                                                 
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_anagram, this, true);

        ss = new SquareView[] {
            (SquareView) findViewById(R.id.t1),
            (SquareView) findViewById(R.id.t2),
            (SquareView) findViewById(R.id.t3),
            (SquareView) findViewById(R.id.t4),
            (SquareView) findViewById(R.id.t5),
            (SquareView) findViewById(R.id.t6),
            (SquareView) findViewById(R.id.t7),
            (SquareView) findViewById(R.id.t8),
            (SquareView) findViewById(R.id.t9)
        };

        markedSquares = new java.util.ArrayList<Integer>();
    }

    public String getMarkedSquares() {
        StringBuilder sb = new StringBuilder();
        for (int i : markedSquares) {
            sb.append(ss[i].getMark());
        }
        return sb.toString();
    }

    public boolean onTouchEvent(MotionEvent event) {
        int eventX = (int) event.getX();
        int eventY = (int) event.getY();

        switch (event.getAction()) {
        case MotionEvent.ACTION_DOWN:
            System.out.println("down");
            break;
        case MotionEvent.ACTION_MOVE:
            for (int i = 0; i < ss.length; i++) {
                ss[i].getHitRect(hitRect);
                // TODO make it dp
                hitRect.inset(10, 10);
                if (hitRect.contains(eventX, eventY)) {

                    // add to list if new mark
                    if (!ss[i].getMarked()) {
                        markedSquares.add(i);
                    }
                    ss[i].setMark();
                }
            }

            listener.onMarkedChange(getMarkedSquares());
            break;
        case MotionEvent.ACTION_UP:
            // for (int i = 0; i < ss.length; i++) {
            //     ss[i].resetMark();
            // }
            for (int i : markedSquares) {
                ss[i].resetMark();
            }

            markedSquares.clear();

            listener.onMarkedChange(getMarkedSquares());
            break;
        }
        return true;
    }

    public void setMarkedSquareListener(MarkedSquareListener listener) {
        this.listener = listener;
    }

    public interface MarkedSquareListener {
        public void onMarkedChange(String markedSquares);
    }
}
