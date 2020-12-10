package com.example.chess_android_app;


import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ReplayGames extends AppCompatActivity {

    private Button next_move_btn;
    private TextView replay_txt;

    private FrameLayout
            ra8, rb8, rc8, rd8, re8, rf8, rg8, rh8,
            ra7, rb7, rc7, rd7, re7, rf7, rg7, rh7,
            ra6, rb6, rc6, rd6, re6, rf6, rg6, rh6,
            ra5, rb5, rc5, rd5, re5, rf5, rg5, rh5,
            ra4, rb4, rc4, rd4, re4, rf4, rg4, rh4,
            ra3, rb3, rc3, rd3, re3, rf3, rg3, rh3,
            ra2, rb2, rc2, rd2, re2, rf2, rg2, rh2,
            ra1, rb1, rc1, rd1, re1, rf1, rg1, rh1;

    private String[] moveHistory;
    private int index = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.replay_game);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        moveHistory = getIntent().getExtras().getStringArray("moveHistory");

        init();

        next_move_btn = findViewById(R.id.next_move_btn);
        replay_txt = findViewById(R.id.replay_txt);

        next_move_btn.setOnClickListener(button -> {
            if (index < moveHistory.length) {
                String move = moveHistory[index];
                String origin = move.substring(0, 2);
                String destination = move.substring(3, 5);

                replay_txt.setText(origin + " " + destination);

                FrameLayout currentFrame = getFrame(origin);
                FrameLayout newFrame = getFrame(destination);

                View v = currentFrame.getChildAt(1);
                currentFrame.removeView(v);

                if (move.length() == 5) {
                    if (newFrame.getChildCount() == 1) {
                        newFrame.addView(v);
                    } else if (newFrame.getChildCount() == 2) {
                        View target = newFrame.getChildAt(1);
                        newFrame.removeView(target);
                        newFrame.addView(v);
                    }
                } else if (move.length() == 8) {
                    if (newFrame.getChildCount() == 1) {
                        newFrame.addView(v);
                    } else if (newFrame.getChildCount() == 2) {
                        View target = newFrame.getChildAt(1);
                        newFrame.removeView(target);
                        newFrame.addView(v);
                    }

                    ImageView piece = (ImageView) v;
                    switch(move.substring(6)) {
                        case "wQ":
                            piece.setImageResource(R.drawable.white_queen);
                            break;
                        case "wR":
                            piece.setImageResource(R.drawable.white_rook);
                            break;
                        case "wB":
                            piece.setImageResource(R.drawable.white_bishop);
                            break;
                        case "wN":
                            piece.setImageResource(R.drawable.white_knight);
                            break;
                        case "bQ":
                            piece.setImageResource(R.drawable.black_queen);
                            break;
                        case "bR":
                            piece.setImageResource(R.drawable.black_rook);
                            break;
                        case "bB":
                            piece.setImageResource(R.drawable.black_bishop);
                            break;
                        case "bN":
                            piece.setImageResource(R.drawable.black_knight);
                            break;
                    }
                } else if (move.indexOf("white enpass") >= 0) {
                    newFrame.addView(v);
                    FrameLayout enpassFrame = getFrame(destination.charAt(0) + "5");
                    enpassFrame.removeViewAt(1);
                } else if (move.indexOf("black enpass") >= 0) {
                    newFrame.addView(v);
                    FrameLayout enpassFrame = getFrame(destination.charAt(0) + "4");
                    enpassFrame.removeViewAt(1);
                } else if (move.indexOf("wK castle right") >= 0) {
                    newFrame.addView(v);
                    View rook = rh1.getChildAt(1);
                    rh1.removeView(rook);
                    rf1.addView(rook);
                } else if (move.indexOf("wK castle left") >= 0) {
                    newFrame.addView(v);
                    View rook = ra1.getChildAt(1);
                    ra1.removeView(rook);
                    rd1.addView(rook);
                } else if (move.indexOf("bK castle right") >= 0) {
                    newFrame.addView(v);
                    View rook = rh8.getChildAt(1);
                    rh8.removeView(rook);
                    rf8.addView(rook);
                } else if (move.indexOf("bK castle left") >= 0) {
                    newFrame.addView(v);
                    View rook = ra8.getChildAt(1);
                    ra8.removeView(rook);
                    rd8.addView(rook);
                }
                index++;
            } else {
                replay_txt.setText("Game Over");
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    private void init() {
        ra8 = findViewById(R.id.ra8);
        ra7 = findViewById(R.id.ra7);
        ra6 = findViewById(R.id.ra6);
        ra5 = findViewById(R.id.ra5);
        ra4 = findViewById(R.id.ra4);
        ra3 = findViewById(R.id.ra3);
        ra2 = findViewById(R.id.ra2);
        ra1 = findViewById(R.id.ra1);
        rb8 = findViewById(R.id.rb8);
        rb7 = findViewById(R.id.rb7);
        rb6 = findViewById(R.id.rb6);
        rb5 = findViewById(R.id.rb5);
        rb4 = findViewById(R.id.rb4);
        rb3 = findViewById(R.id.rb3);
        rb2 = findViewById(R.id.rb2);
        rb1 = findViewById(R.id.rb1);
        rc8 = findViewById(R.id.rc8);
        rc7 = findViewById(R.id.rc7);
        rc6 = findViewById(R.id.rc6);
        rc5 = findViewById(R.id.rc5);
        rc4 = findViewById(R.id.rc4);
        rc3 = findViewById(R.id.rc3);
        rc2 = findViewById(R.id.rc2);
        rc1 = findViewById(R.id.rc1);
        rd8 = findViewById(R.id.rd8);
        rd7 = findViewById(R.id.rd7);
        rd6 = findViewById(R.id.rd6);
        rd5 = findViewById(R.id.rd5);
        rd4 = findViewById(R.id.rd4);
        rd3 = findViewById(R.id.rd3);
        rd2 = findViewById(R.id.rd2);
        rd1 = findViewById(R.id.rd1);
        re8 = findViewById(R.id.re8);
        re7 = findViewById(R.id.re7);
        re6 = findViewById(R.id.re6);
        re5 = findViewById(R.id.re5);
        re4 = findViewById(R.id.re4);
        re3 = findViewById(R.id.re3);
        re2 = findViewById(R.id.re2);
        re1 = findViewById(R.id.re1);
        rf8 = findViewById(R.id.rf8);
        rf7 = findViewById(R.id.rf7);
        rf6 = findViewById(R.id.rf6);
        rf5 = findViewById(R.id.rf5);
        rf4 = findViewById(R.id.rf4);
        rf3 = findViewById(R.id.rf3);
        rf2 = findViewById(R.id.rf2);
        rf1 = findViewById(R.id.rf1);
        rg8 = findViewById(R.id.rg8);
        rg7 = findViewById(R.id.rg7);
        rg6 = findViewById(R.id.rg6);
        rg5 = findViewById(R.id.rg5);
        rg4 = findViewById(R.id.rg4);
        rg3 = findViewById(R.id.rg3);
        rg2 = findViewById(R.id.rg2);
        rg1 = findViewById(R.id.rg1);
        rh8 = findViewById(R.id.rh8);
        rh7 = findViewById(R.id.rh7);
        rh6 = findViewById(R.id.rh6);
        rh5 = findViewById(R.id.rh5);
        rh4 = findViewById(R.id.rh4);
        rh3 = findViewById(R.id.rh3);
        rh2 = findViewById(R.id.rh2);
        rh1 = findViewById(R.id.rh1);
    }

    private FrameLayout getFrame(String input) {
        switch (input) {
            case "a1":
                return ra1;
            case "a2":
                return ra2;
            case "a3":
                return ra3;
            case "a4":
                return ra4;
            case "a5":
                return ra5;
            case "a6":
                return ra6;
            case "a7":
                return ra7;
            case "a8":
                return ra8;
            case "b1":
                return rb1;
            case "b2":
                return rb2;
            case "b3":
                return rb3;
            case "b4":
                return rb4;
            case "b5":
                return rb5;
            case "b6":
                return rb6;
            case "b7":
                return rb7;
            case "b8":
                return rb8;
            case "c1":
                return rc1;
            case "c2":
                return rc2;
            case "c3":
                return rc3;
            case "c4":
                return rc4;
            case "c5":
                return rc5;
            case "c6":
                return rc6;
            case "c7":
                return rc7;
            case "c8":
                return rc8;
            case "d1":
                return rd1;
            case "d2":
                return rd2;
            case "d3":
                return rd3;
            case "d4":
                return rd4;
            case "d5":
                return rd5;
            case "d6":
                return rd6;
            case "d7":
                return rd7;
            case "d8":
                return rd8;
            case "e1":
                return re1;
            case "e2":
                return re2;
            case "e3":
                return re3;
            case "e4":
                return re4;
            case "e5":
                return re5;
            case "e6":
                return re6;
            case "e7":
                return re7;
            case "e8":
                return re8;
            case "f1":
                return rf1;
            case "f2":
                return rf2;
            case "f3":
                return rf3;
            case "f4":
                return rf4;
            case "f5":
                return rf5;
            case "f6":
                return rf6;
            case "f7":
                return rf7;
            case "f8":
                return rf8;
            case "g1":
                return rg1;
            case "g2":
                return rg2;
            case "g3":
                return rg3;
            case "g4":
                return rg4;
            case "g5":
                return rg5;
            case "g6":
                return rg6;
            case "g7":
                return rg7;
            case "g8":
                return rg8;
            case "h1":
                return rh1;
            case "h2":
                return rh2;
            case "h3":
                return rh3;
            case "h4":
                return rh4;
            case "h5":
                return rh5;
            case "h6":
                return rh6;
            case "h7":
                return rh7;
            case "h8":
                return rh8;
            default:
                return null;
        }
    }
}
