package com.example.chess_android_app.popups;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.chess_android_app.ChessPlay;
import com.example.chess_android_app.R;

import java.util.ArrayList;

public class PlayGameOverPopup extends Activity {

    private Button save_btn, exit_btn;
    private TextView cause_txt;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.play_game_over_popup);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout( (int) (width*.75),  (int) (height*.2725));

        save_btn = findViewById(R.id.save_btn);
        exit_btn = findViewById(R.id.exit_btn);
        cause_txt = findViewById(R.id.cause_txt);

        cause_txt.setText(getIntent().getExtras().getString("cause"));

        save_btn.setOnClickListener(v -> {
            startActivityForResult(new Intent(PlayGameOverPopup.this, GameOverTitlePopup.class), 1);
        });

        exit_btn.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.putExtra("result", "exit");
            setResult(RESULT_OK, intent);
            finish();
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String title = data.getStringExtra("title");

        Intent intent = new Intent();
        intent.putExtra("result", "save");
        intent.putExtra("title", title);
        setResult(RESULT_OK, intent);
        finish();
    }
}
