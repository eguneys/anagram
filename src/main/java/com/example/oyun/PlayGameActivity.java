package com.example.oyun;

import android.app.Activity;
import android.os.Bundle;

import android.view.View;
import android.view.MotionEvent;

import android.widget.TextView;

import android.graphics.Rect;
import android.graphics.drawable.TransitionDrawable;

import android.content.Intent;

import java.util.ArrayList;
import java.util.List;

public class PlayGameActivity extends Activity
{
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.play);

        View table1 = findViewById(R.id.table1);
        table1.setOnTouchListener(new View.OnTouchListener() {
                Rect rect = new Rect();
                View s1 = findViewById(R.id.t1);
                View s2 = findViewById(R.id.t2);
                View s3 = findViewById(R.id.t3);
                View s11 = findViewById(R.id.t11);
                View s12 = findViewById(R.id.t12);
                View s13 = findViewById(R.id.t13);
                View s21 = findViewById(R.id.t21);
                View s22 = findViewById(R.id.t22);
                View s23 = findViewById(R.id.t23);

                View[] ss = {
                        s1, s2, s3,
                            s11, s12, s13,
                            s21, s22, s23
                            };

                public boolean onTouch(View v, MotionEvent event) {
                    int eventX = (int) event.getX();
                    int eventY = (int) event.getY();

                    int action = event.getAction();

                    if (action == MotionEvent.ACTION_DOWN) {
                        System.out.println("down");
                    } else if (action == MotionEvent.ACTION_UP) {
                        for (int i = 0; i < ss.length; i++) {
                            showSquare(ss[i]);
                        }
                    }
                    for (int i = 0; i < ss.length; i++) {
                        hideSquare(ss[i], event);
                    }
                    return true;
                }

                public void showSquare(View s1) {
                    // anim.setTarget(s1);
                    // anim.setEvaluator(new ArgbEvaluator());
                    // anim.start();
                }

                public void hideSquare(View s1, MotionEvent event) {
                    int eventX = (int) event.getX();
                    int eventY = (int) event.getY();

                    s1.getHitRect(rect);
                    if (rect.contains(eventX, eventY)) {
                        // anim.reverse();
                    }
                }
            });
    }

    public void playGame(View view) {
        Intent intent = new Intent(this, PlayGameActivity.class);
        startActivity(intent);
    }
}
