package com.example.chess_android_app.popups;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.ImageButton;

import androidx.annotation.Nullable;

import com.example.chess_android_app.R;

public class PlayBlackPromotionPopup extends Activity {

    private ImageButton bQ, bR, bB, bN;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.play_black_promotion_popup);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout( (int) (width*.6),  (int) (height*.2725));

        bQ = findViewById(R.id.bQ_promotion);
        bR = findViewById(R.id.bR_promotion);
        bB = findViewById(R.id.bB_promotion);
        bN = findViewById(R.id.bN_promotion);

        Intent intent = new Intent();
        bQ.setOnClickListener(v -> {
            intent.putExtra("promotion", "bQ");
            setResult(RESULT_OK, intent);
            finish();
        });

        bR.setOnClickListener(v -> {
            intent.putExtra("promotion", "bR");
            setResult(RESULT_OK, intent);
            finish();
        });

        bB.setOnClickListener(v -> {
            intent.putExtra("promotion", "bB");
            setResult(RESULT_OK, intent);
            finish();
        });

        bN.setOnClickListener(v -> {
            intent.putExtra("promotion", "bN");
            setResult(RESULT_OK, intent);
            finish();
        });
    }
}
