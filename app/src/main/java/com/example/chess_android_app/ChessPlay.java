package com.example.chess_android_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.os.Bundle;
import android.view.DragEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.chess_android_app.models.ChessGame;

public class ChessPlay extends AppCompatActivity {

    private ImageView
            bR_left, bN_left, bB_left, bQ, bK, bB_right, bN_right, bR_right,
            bp1, bp2, bp3, bp4, bp5, bp6, bp7, bp8,
            wR_left, wN_left, wB_left, wQ, wK, wB_right, wN_right, wR_right,
            wp1, wp2, wp3, wp4, wp5, wp6, wp7, wp8;

    private FrameLayout
            a8, b8, c8, d8, e8, f8, g8, h8,
            a7, b7, c7, d7, e7, f7, g7, h7,
            a6, b6, c6, d6, e6, f6, g6, h6,
            a5, b5, c5, d5, e5, f5, g5, h5,
            a4, b4, c4, d4, e4, f4, g4, h4,
            a3, b3, c3, d3, e3, f3, g3, h3,
            a2, b2, c2, d2, e2, f2, g2, h2,
            a1, b1, c1, d1, e1, f1, g1, h1;

    private View viewHolder = null;

    private ChessGame game = new ChessGame();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chess_play);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        bR_left = findViewById(R.id.bR_left);
        bN_left = findViewById(R.id.bN_left);
        bB_left = findViewById(R.id.bB_left);
        bQ = findViewById(R.id.bQ);
        bK = findViewById(R.id.bK);
        bB_right = findViewById(R.id.bB_right);
        bN_right = findViewById(R.id.bN_right);
        bR_right = findViewById(R.id.bR_right);
        bp1 = findViewById(R.id.bp1);
        bp2 = findViewById(R.id.bp2);
        bp3 = findViewById(R.id.bp3);
        bp4 = findViewById(R.id.bp4);
        bp5 = findViewById(R.id.bp5);
        bp6 = findViewById(R.id.bp6);
        bp7 = findViewById(R.id.bp7);
        bp8 = findViewById(R.id.bp8);

        wR_left = findViewById(R.id.wR_left);
        wN_left = findViewById(R.id.wN_left);
        wB_left = findViewById(R.id.wB_left);
        wQ = findViewById(R.id.wQ);
        wK = findViewById(R.id.wK);
        wB_right = findViewById(R.id.wB_right);
        wN_right = findViewById(R.id.wN_right);
        wR_right = findViewById(R.id.wR_right);
        wp1 = findViewById(R.id.wp1);
        wp2 = findViewById(R.id.wp2);
        wp3 = findViewById(R.id.wp3);
        wp4 = findViewById(R.id.wp4);
        wp5 = findViewById(R.id.wp5);
        wp6 = findViewById(R.id.wp6);
        wp7 = findViewById(R.id.wp7);
        wp8 = findViewById(R.id.wp8);

        a8 = findViewById(R.id.a8);
        a7 = findViewById(R.id.a7);
        a6 = findViewById(R.id.a6);
        a5 = findViewById(R.id.a5);
        a4 = findViewById(R.id.a4);
        a3 = findViewById(R.id.a3);
        a2 = findViewById(R.id.a2);
        a1 = findViewById(R.id.a1);
        b8 = findViewById(R.id.b8);
        b7 = findViewById(R.id.b7);
        b6 = findViewById(R.id.b6);
        b5 = findViewById(R.id.b5);
        b4 = findViewById(R.id.b4);
        b3 = findViewById(R.id.b3);
        b2 = findViewById(R.id.b2);
        b1 = findViewById(R.id.b1);
        c8 = findViewById(R.id.c8);
        c7 = findViewById(R.id.c7);
        c6 = findViewById(R.id.c6);
        c5 = findViewById(R.id.c5);
        c4 = findViewById(R.id.c4);
        c3 = findViewById(R.id.c3);
        c2 = findViewById(R.id.c2);
        c1 = findViewById(R.id.c1);
        d8 = findViewById(R.id.d8);
        d7 = findViewById(R.id.d7);
        d6 = findViewById(R.id.d6);
        d5 = findViewById(R.id.d5);
        d4 = findViewById(R.id.d4);
        d3 = findViewById(R.id.d3);
        d2 = findViewById(R.id.d2);
        d1 = findViewById(R.id.d1);
        e8 = findViewById(R.id.e8);
        e7 = findViewById(R.id.e7);
        e6 = findViewById(R.id.e6);
        e5 = findViewById(R.id.e5);
        e4 = findViewById(R.id.e4);
        e3 = findViewById(R.id.e3);
        e2 = findViewById(R.id.e2);
        e1 = findViewById(R.id.e1);
        f8 = findViewById(R.id.f8);
        f7 = findViewById(R.id.f7);
        f6 = findViewById(R.id.f6);
        f5 = findViewById(R.id.f5);
        f4 = findViewById(R.id.f4);
        f3 = findViewById(R.id.f3);
        f2 = findViewById(R.id.f2);
        f1 = findViewById(R.id.f1);
        g8 = findViewById(R.id.g8);
        g7 = findViewById(R.id.g7);
        g6 = findViewById(R.id.g6);
        g5 = findViewById(R.id.g5);
        g4 = findViewById(R.id.g4);
        g3 = findViewById(R.id.g3);
        g2 = findViewById(R.id.g2);
        g1 = findViewById(R.id.g1);
        h8 = findViewById(R.id.h8);
        h7 = findViewById(R.id.h7);
        h6 = findViewById(R.id.h6);
        h5 = findViewById(R.id.h5);
        h4 = findViewById(R.id.h4);
        h3 = findViewById(R.id.h3);
        h2 = findViewById(R.id.h2);
        h1 = findViewById(R.id.h1);

        bR_left.setOnLongClickListener(longClickListener);
        bN_left.setOnLongClickListener(longClickListener);
        bB_left.setOnLongClickListener(longClickListener);
        bQ.setOnLongClickListener(longClickListener);
        bK.setOnLongClickListener(longClickListener);
        bB_right.setOnLongClickListener(longClickListener);
        bN_right.setOnLongClickListener(longClickListener);
        bR_right.setOnLongClickListener(longClickListener);
        bp1.setOnLongClickListener(longClickListener);
        bp2.setOnLongClickListener(longClickListener);
        bp3.setOnLongClickListener(longClickListener);
        bp4.setOnLongClickListener(longClickListener);
        bp5.setOnLongClickListener(longClickListener);
        bp6.setOnLongClickListener(longClickListener);
        bp7.setOnLongClickListener(longClickListener);
        bp8.setOnLongClickListener(longClickListener);

        wR_left.setOnLongClickListener(longClickListener);
        wN_left.setOnLongClickListener(longClickListener);
        wB_left.setOnLongClickListener(longClickListener);
        wQ.setOnLongClickListener(longClickListener);
        wK.setOnLongClickListener(longClickListener);
        wB_right.setOnLongClickListener(longClickListener);
        wN_right.setOnLongClickListener(longClickListener);
        wR_right.setOnLongClickListener(longClickListener);
        wp1.setOnLongClickListener(longClickListener);
        wp2.setOnLongClickListener(longClickListener);
        wp3.setOnLongClickListener(longClickListener);
        wp4.setOnLongClickListener(longClickListener);
        wp5.setOnLongClickListener(longClickListener);
        wp6.setOnLongClickListener(longClickListener);
        wp7.setOnLongClickListener(longClickListener);
        wp8.setOnLongClickListener(longClickListener);

        a8.setOnDragListener(dragListener);
        a7.setOnDragListener(dragListener);
        a6.setOnDragListener(dragListener);
        a5.setOnDragListener(dragListener);
        a4.setOnDragListener(dragListener);
        a3.setOnDragListener(dragListener);
        a2.setOnDragListener(dragListener);
        a1.setOnDragListener(dragListener);
        b8.setOnDragListener(dragListener);
        b7.setOnDragListener(dragListener);
        b6.setOnDragListener(dragListener);
        b5.setOnDragListener(dragListener);
        b4.setOnDragListener(dragListener);
        b3.setOnDragListener(dragListener);
        b2.setOnDragListener(dragListener);
        b1.setOnDragListener(dragListener);
        c8.setOnDragListener(dragListener);
        c7.setOnDragListener(dragListener);
        c6.setOnDragListener(dragListener);
        c5.setOnDragListener(dragListener);
        c4.setOnDragListener(dragListener);
        c3.setOnDragListener(dragListener);
        c2.setOnDragListener(dragListener);
        c1.setOnDragListener(dragListener);
        d8.setOnDragListener(dragListener);
        d7.setOnDragListener(dragListener);
        d6.setOnDragListener(dragListener);
        d5.setOnDragListener(dragListener);
        d4.setOnDragListener(dragListener);
        d3.setOnDragListener(dragListener);
        d2.setOnDragListener(dragListener);
        d1.setOnDragListener(dragListener);
        e8.setOnDragListener(dragListener);
        e7.setOnDragListener(dragListener);
        e6.setOnDragListener(dragListener);
        e5.setOnDragListener(dragListener);
        e4.setOnDragListener(dragListener);
        e3.setOnDragListener(dragListener);
        e2.setOnDragListener(dragListener);
        e1.setOnDragListener(dragListener);
        f8.setOnDragListener(dragListener);
        f7.setOnDragListener(dragListener);
        f6.setOnDragListener(dragListener);
        f5.setOnDragListener(dragListener);
        f4.setOnDragListener(dragListener);
        f3.setOnDragListener(dragListener);
        f2.setOnDragListener(dragListener);
        f1.setOnDragListener(dragListener);
        g8.setOnDragListener(dragListener);
        g7.setOnDragListener(dragListener);
        g6.setOnDragListener(dragListener);
        g5.setOnDragListener(dragListener);
        g4.setOnDragListener(dragListener);
        g3.setOnDragListener(dragListener);
        g2.setOnDragListener(dragListener);
        g1.setOnDragListener(dragListener);
        h8.setOnDragListener(dragListener);
        h7.setOnDragListener(dragListener);
        h6.setOnDragListener(dragListener);
        h5.setOnDragListener(dragListener);
        h4.setOnDragListener(dragListener);
        h3.setOnDragListener(dragListener);
        h2.setOnDragListener(dragListener);
        h1.setOnDragListener(dragListener);

        bR_left.setOnClickListener(pieceClickListener);
        bN_left.setOnClickListener(pieceClickListener);
        bB_left.setOnClickListener(pieceClickListener);
        bQ.setOnClickListener(pieceClickListener);
        bK.setOnClickListener(pieceClickListener);
        bB_right.setOnClickListener(pieceClickListener);
        bN_right.setOnClickListener(pieceClickListener);
        bR_right.setOnClickListener(pieceClickListener);
        bp1.setOnClickListener(pieceClickListener);
        bp2.setOnClickListener(pieceClickListener);
        bp3.setOnClickListener(pieceClickListener);
        bp4.setOnClickListener(pieceClickListener);
        bp5.setOnClickListener(pieceClickListener);
        bp6.setOnClickListener(pieceClickListener);
        bp7.setOnClickListener(pieceClickListener);
        bp8.setOnClickListener(pieceClickListener);

        wR_left.setOnClickListener(pieceClickListener);
        wN_left.setOnClickListener(pieceClickListener);
        wB_left.setOnClickListener(pieceClickListener);
        wQ.setOnClickListener(pieceClickListener);
        wK.setOnClickListener(pieceClickListener);
        wB_right.setOnClickListener(pieceClickListener);
        wN_right.setOnClickListener(pieceClickListener);
        wR_right.setOnClickListener(pieceClickListener);
        wp1.setOnClickListener(pieceClickListener);
        wp2.setOnClickListener(pieceClickListener);
        wp3.setOnClickListener(pieceClickListener);
        wp4.setOnClickListener(pieceClickListener);
        wp5.setOnClickListener(pieceClickListener);
        wp6.setOnClickListener(pieceClickListener);
        wp7.setOnClickListener(pieceClickListener);
        wp8.setOnClickListener(pieceClickListener);

        a8.setOnClickListener(tileClickListener);
        a7.setOnClickListener(tileClickListener);
        a6.setOnClickListener(tileClickListener);
        a5.setOnClickListener(tileClickListener);
        a4.setOnClickListener(tileClickListener);
        a3.setOnClickListener(tileClickListener);
        a2.setOnClickListener(tileClickListener);
        a1.setOnClickListener(tileClickListener);
        b8.setOnClickListener(tileClickListener);
        b7.setOnClickListener(tileClickListener);
        b6.setOnClickListener(tileClickListener);
        b5.setOnClickListener(tileClickListener);
        b4.setOnClickListener(tileClickListener);
        b3.setOnClickListener(tileClickListener);
        b2.setOnClickListener(tileClickListener);
        b1.setOnClickListener(tileClickListener);
        c8.setOnClickListener(tileClickListener);
        c7.setOnClickListener(tileClickListener);
        c6.setOnClickListener(tileClickListener);
        c5.setOnClickListener(tileClickListener);
        c4.setOnClickListener(tileClickListener);
        c3.setOnClickListener(tileClickListener);
        c2.setOnClickListener(tileClickListener);
        c1.setOnClickListener(tileClickListener);
        d8.setOnClickListener(tileClickListener);
        d7.setOnClickListener(tileClickListener);
        d6.setOnClickListener(tileClickListener);
        d5.setOnClickListener(tileClickListener);
        d4.setOnClickListener(tileClickListener);
        d3.setOnClickListener(tileClickListener);
        d2.setOnClickListener(tileClickListener);
        d1.setOnClickListener(tileClickListener);
        e8.setOnClickListener(tileClickListener);
        e7.setOnClickListener(tileClickListener);
        e6.setOnClickListener(tileClickListener);
        e5.setOnClickListener(tileClickListener);
        e4.setOnClickListener(tileClickListener);
        e3.setOnClickListener(tileClickListener);
        e2.setOnClickListener(tileClickListener);
        e1.setOnClickListener(tileClickListener);
        f8.setOnClickListener(tileClickListener);
        f7.setOnClickListener(tileClickListener);
        f6.setOnClickListener(tileClickListener);
        f5.setOnClickListener(tileClickListener);
        f4.setOnClickListener(tileClickListener);
        f3.setOnClickListener(tileClickListener);
        f2.setOnClickListener(tileClickListener);
        f1.setOnClickListener(tileClickListener);
        g8.setOnClickListener(tileClickListener);
        g7.setOnClickListener(tileClickListener);
        g6.setOnClickListener(tileClickListener);
        g5.setOnClickListener(tileClickListener);
        g4.setOnClickListener(tileClickListener);
        g3.setOnClickListener(tileClickListener);
        g2.setOnClickListener(tileClickListener);
        g1.setOnClickListener(tileClickListener);
        h8.setOnClickListener(tileClickListener);
        h7.setOnClickListener(tileClickListener);
        h6.setOnClickListener(tileClickListener);
        h5.setOnClickListener(tileClickListener);
        h4.setOnClickListener(tileClickListener);
        h3.setOnClickListener(tileClickListener);
        h2.setOnClickListener(tileClickListener);
        h1.setOnClickListener(tileClickListener);
    }

    View.OnClickListener pieceClickListener = view -> {
        if(viewHolder == null) {
            viewHolder = view;
        } else {
            View v = viewHolder;
            String origin = v.getTag().toString();
            String destination = view.getTag().toString();
            int result = game.play(origin + " " + destination);

            if(result >= 0) {
                //Toast.makeText(ChessPlay.this, origin + " " + destination, Toast.LENGTH_SHORT).show();

                FrameLayout currentFrame = (FrameLayout) v.getParent();
                currentFrame.removeView(v);

                FrameLayout newFrame = (FrameLayout) view.getParent();
                newFrame.removeViewAt(1);
                newFrame.addView(v);

                v.setTag(destination);

                if(result == 1 || result == 2) {
                    Toast.makeText(ChessPlay.this, "Check", Toast.LENGTH_SHORT).show();
                }
            }
            viewHolder = null;
        }
    };

    View.OnClickListener tileClickListener = view -> {
        if(viewHolder != null) {
            View v = viewHolder;
            String origin = v.getTag().toString();
            String destination = view.getTag().toString();
            int result = game.play(origin + " " + destination);

            if(result >= 0) {
                //Toast.makeText(ChessPlay.this, origin + " " + destination, Toast.LENGTH_SHORT).show();

                FrameLayout currentFrame = (FrameLayout) v.getParent();
                currentFrame.removeView(v);

                FrameLayout newFrame = (FrameLayout) view;
                newFrame.addView(v);

                v.setTag(destination);

                if(result == 1 || result == 2) {
                    Toast.makeText(ChessPlay.this, "Check", Toast.LENGTH_SHORT).show();
                }
            }
            viewHolder = null;
        }
    };

    View.OnLongClickListener longClickListener = view -> {
        ClipData data = ClipData.newPlainText("","");
        View.DragShadowBuilder myShadowBuilder = new View.DragShadowBuilder(view);
        view.startDragAndDrop(data,myShadowBuilder, view, 0);
        return false;
    };

    View.OnDragListener dragListener = (target, event) -> {
        int dragEvent = event.getAction();
        final View view = (View) event.getLocalState();

        switch (dragEvent) {
            case DragEvent.ACTION_DRAG_ENTERED:
            case DragEvent.ACTION_DRAG_EXITED:
                break;
            case DragEvent.ACTION_DROP:
                String origin = view.getTag().toString();
                String destination = target.getTag().toString();
                int result = game.play(origin + " " + destination);

                if(result >= 0) {
                    //Toast.makeText(ChessPlay.this, origin + " " + destination, Toast.LENGTH_SHORT).show();

                    FrameLayout currentFrame = (FrameLayout) view.getParent();
                    currentFrame.removeView(view);

                    FrameLayout newFrame = (FrameLayout) target;
                    if (newFrame.getChildCount() == 1) {
                        newFrame.addView(view);
                    } else if (newFrame.getChildCount() == 2) {
                        newFrame.removeViewAt(1);
                        newFrame.addView(view);
                    }
                    view.setTag(destination);

                    if(result == 1 || result == 2) {
                        Toast.makeText(ChessPlay.this, "Check", Toast.LENGTH_SHORT).show();
                    }
                }

                break;
        }
        return true;
    };

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }
}