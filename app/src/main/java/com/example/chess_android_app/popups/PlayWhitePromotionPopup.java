package com.example.chess_android_app.popups;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.ImageButton;

import androidx.annotation.Nullable;

import com.example.chess_android_app.R;

public class PlayWhitePromotionPopup extends Activity {

    private ImageButton wQ, wR, wB, wN;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.play_white_promotion_popup);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout( (int) (width*.6),  (int) (height*.2725));

        wQ = findViewById(R.id.wQ_promotion);
        wR = findViewById(R.id.wR_promotion);
        wB = findViewById(R.id.wB_promotion);
        wN = findViewById(R.id.wN_promotion);

        Intent intent = new Intent();
        wQ.setOnClickListener(v -> {
            intent.putExtra("promotion", "wQ");
            setResult(RESULT_OK, intent);
            finish();
        });

        wR.setOnClickListener(v -> {
            intent.putExtra("promotion", "wR");
            setResult(RESULT_OK, intent);
            finish();
        });

        wB.setOnClickListener(v -> {
            intent.putExtra("promotion", "wB");
            setResult(RESULT_OK, intent);
            finish();
        });

        wN.setOnClickListener(v -> {
            intent.putExtra("promotion", "wN");
            setResult(RESULT_OK, intent);
            finish();
        });
    }
}
