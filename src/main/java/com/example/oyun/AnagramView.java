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

import java.util.List;
import java.util.ArrayList;

public class AnagramView extends GridLayout
{

    private MarkedSquareListener listener;

    private String mAnagram = "";

    private SquareView[] ss;
    private Rect hitRect = new Rect();

    private List<Integer> markedSquares;

    private final List<Integer> allSquares = java.util.Arrays.asList(0, 1, 2, 3, 4, 5, 6);
    private List<Integer> allSquaresShuffle = java.util.Arrays.asList(0, 1, 2, 3, 4, 5, 6);

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

    public String getAnagram() {
        return mAnagram;
    }

    public void setAnagram(String anagram) {
        this.mAnagram = anagram;

        java.util.Collections.shuffle(allSquaresShuffle);

        for (int i : allSquares) {
            int randomI = allSquaresShuffle.get(i);
            ss[i].setText(this.mAnagram.charAt(randomI) + "");
        }
        invalidate();
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
            String gms = getMarkedSquares();

            for (int i : markedSquares) {
                ss[i].resetMark();
            }
            markedSquares.clear();

            listener.onMarkedChange(gms);
            System.out.println(mAnagram + "," + gms);
            System.out.println(mAnagram.equals(gms));

            if (mAnagram.equals(gms)) {
                listener.onMarkedSuccess();
            } else {
                listener.onMarkedCancel(gms);
            }
            break;
        }
        return true;
    }

    public void setMarkedSquareListener(MarkedSquareListener listener) {
        this.listener = listener;
    }

    public interface MarkedSquareListener {
        public void onMarkedChange(String markedSquares);
        public void onMarkedCancel(String markedSquares);
        public void onMarkedSuccess();
    }
}
