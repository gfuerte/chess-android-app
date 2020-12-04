package com.example.chess_android_app.popups;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;

import com.example.chess_android_app.R;

public class GameOverTitlePopup extends Activity {

    private Button title_save_btn;
    private EditText game_title;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_over_title_popup);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout( (int) (width*.77),  (int) (height*.2725));

        title_save_btn = findViewById(R.id.title_save_btn);
        game_title = findViewById(R.id.game_title);

        title_save_btn.setOnClickListener(v -> {
            if (game_title.getText().toString().trim().length() > 0) {
                Intent intent = new Intent();
                intent.putExtra("title", game_title.getText().toString());
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }
}
