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

import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;

public class PlayGameActivity extends AppCompatActivity
{

    TextView wordText;
    TextView anagramText;
    AnagramView anagramView;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.play);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);


        anagramText = (TextView) findViewById(R.id.text1);
        anagramView = (AnagramView) findViewById(R.id.anagram1);

        anagramView.setAnagram("ANAGRAM");

        final String[] words = new String[] { "ANAGRAM",
                                              "KABURGA",
                                              "KABARMA",
                                              "KABLOCU",
                                              "EFLATUN",
                                              "EKONOMI",
                                              "ELEMENT"};

        anagramView.setMarkedSquareListener(new AnagramView.MarkedSquareListener() {
                java.util.Random gen = new java.util.Random();

                public void onMarkedCancel(String markedSquares) {
                    anagramText.animate().alpha(0).setListener(new android.animation.AnimatorListenerAdapter() {
                            public void onAnimationEnd(android.animation.Animator animator) {
                                anagramText.setText("");
                                anagramText.setAlpha(1);
                                //anagramView.setAnagram(anagramView.getAnagram());
                            }
                        });
                }

                public void onMarkedChange(String markedSquares) {
                    anagramText.setText(markedSquares);

                }

                public void onMarkedSuccess() {
                    int rWord = gen.nextInt(words.length - 1);
                    anagramView.setAnagram(words[rWord]);
                }
            });
    }
}
