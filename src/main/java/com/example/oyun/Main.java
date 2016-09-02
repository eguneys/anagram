package com.example.oyun;

import android.app.Activity;
import android.os.Bundle;

import android.view.View;

import android.widget.TextView;

import android.content.Intent;


public class Main extends Activity
{
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        final TextView anagramText = (TextView) findViewById(R.id.text1);
        final AnagramView anagramView = (AnagramView) findViewById(R.id.anagram1);

        anagramView.setMarkedSquareListener(new AnagramView.MarkedSquareListener() {
                public void onMarkedChange(String markedSquares) {
                    anagramText.setText(markedSquares);
                }
            });
    }

    public void playGame(View view) {
        Intent intent = new Intent(this, PlayGameActivity.class);
        startActivity(intent);
    }
}
