package com.example.oyun;

import android.app.Activity;
import android.os.Bundle;

import android.view.View;
import android.view.MotionEvent;


import android.widget.TextView;

import android.graphics.drawable.TransitionDrawable;

import android.content.Intent;

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
                public boolean onTouch(View v, MotionEvent event) {
                    TextView t1 = (TextView) findViewById(R.id.t1);
                    TransitionDrawable drawable = (TransitionDrawable) t1.getBackground();

                    drawable.startTransition(200);
                    return true;
                }
            });
    }

    public void playGame(View view) {
        Intent intent = new Intent(this, PlayGameActivity.class);
        startActivity(intent);
    }
}
