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

        
        GridLayout layout = (GridLayout) findViewById(R.id.grid1);
        layout.setClipChildren(false);

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
            int row = i / 3;
            int col = i % 3;
            ss[i].popDelay(row * 10 + col * 30);
            ss[i].popText(this.mAnagram.charAt(randomI) + "");
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
                        ss[i].setMark(true);
                    }
                }
            }

            listener.onMarkedChange(getMarkedSquares());
            break;
        case MotionEvent.ACTION_UP:
            String gms = getMarkedSquares();
            boolean isSuccess = mAnagram.equals(gms);


            // TODO delay until animation end
            for (int i : markedSquares) {
                ss[i].setMark(false);
            }

            if (isSuccess) {
                ss[markedSquares.get(markedSquares.size() - 1)].setAnimationListener(new SquareView.SquareAnimationListener() {
                        public void onSquarePop() {
                            
                        };

                        public void onSquareVanish() {
                            listener.onMarkedSuccess();
                        };
                    });
                for (int i : markedSquares) {
                    ss[i].setVisible(false);
                }
            } else {
                for (int i : markedSquares) {
                    ss[i].shake();
                }
                listener.onMarkedCancel(gms);
            }

            markedSquares.clear();
            listener.onMarkedChange(gms);
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
