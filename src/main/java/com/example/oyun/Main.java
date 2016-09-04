package com.example.oyun;

import android.app.Activity;
import android.os.Bundle;

import android.view.View;

import android.widget.TextView;

import android.content.Intent;


public class Main extends Activity
{

    TextView wordText;
    TextView anagramText;
    AnagramView anagramView;

    int foundWords = 0;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        wordText = (TextView) findViewById(R.id.wordText);

        anagramText = (TextView) findViewById(R.id.text1);
        anagramView = (AnagramView) findViewById(R.id.anagram1);

        System.out.println(wordText);

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
                    anagramText.setText("");
                    anagramView.setAnagram(anagramView.getAnagram());
                }

                public void onMarkedChange(String markedSquares) {
                    anagramText.setText(markedSquares);

                }

                public void onMarkedSuccess() {
                    int rWord = gen.nextInt(words.length - 1);
                    anagramView.setAnagram(words[rWord]);

                    invalidateScores();
                }
            });
    }

    public void invalidateScores() {
        foundWords++;
        wordText.setText(foundWords + "");
    }

    public void playGame(View view) {
        Intent intent = new Intent(this, PlayGameActivity.class);
        startActivity(intent);
    }
}
