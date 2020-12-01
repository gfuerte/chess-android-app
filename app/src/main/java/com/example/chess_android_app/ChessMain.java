package com.example.chess_android_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ChessMain extends AppCompatActivity {

    private Button newGame, replayGame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chess_main);

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