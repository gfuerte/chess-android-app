package com.example.chess_android_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.Set;
import java.util.TreeSet;

public class ChessMain extends AppCompatActivity {

    private Button newGame, replayGame;
    private static final String PREFS_NAME = "PrefsFile";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chess_main);

        //initializes game title set
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        Set<String> set = settings.getStringSet("set", null);

        /*
        editor.clear();
        editor.apply();
        */

        if(set == null) {
            set = new TreeSet<String>();
            editor.putStringSet("set", set);
            editor.apply();
        }

        newGame = findViewById(R.id.play_button);
        replayGame = findViewById(R.id.replay_button);

        newGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ChessMain.this, ChessPlay.class));
            }
        });

        replayGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ChessMain.this, ChessReplay.class));
            }
        });
    }
}