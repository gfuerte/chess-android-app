package com.example.chess_android_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.DragEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.chess_android_app.models.ChessGame;
import com.example.chess_android_app.popups.PlayBlackPromotionPopup;
import com.example.chess_android_app.popups.PlayGameOverPopup;
import com.example.chess_android_app.popups.PlayWhitePromotionPopup;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Set;
import java.util.stream.Collectors;

public class ChessPlay extends AppCompatActivity {
    private static final String PREFS_NAME = "PrefsFile";

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

    private Button undo_btn, ai_btn, draw_btn, resign_btn;
    private TextView text;

    private ChessGame game = new ChessGame();
    private View viewHolder = null;
    private View promotionView;

    private ArrayList<View> undoPieces = new ArrayList<>();
    private ArrayList<FrameLayout> undoLocations = new ArrayList<>();
    private ArrayList<String> undoTags = new ArrayList<>();
    private boolean undoPromotion = false;
    private boolean undoPawnWhite = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chess_play);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        init();

        undo_btn = findViewById(R.id.undo_btn);
        ai_btn = findViewById(R.id.ai_btn);
        draw_btn = findViewById(R.id.draw_btn);
        resign_btn = findViewById(R.id.resign_btn);

        text = findViewById(R.id.text);

        undo_btn.setOnClickListener(button -> {
            text.setText("Undo");
            if (undoPieces.size() > 0) {
                game.play("undo");
                View piece = undoPieces.get(0);
                FrameLayout undoOrigin = undoLocations.get(0);
                FrameLayout undoDestination = undoLocations.get(1);
                piece.setTag(undoTags.get(0));
                if (undoLocations.size() == 2) {
                    undoDestination.removeViewAt(1);
                    undoOrigin.addView(piece);
                    if (undoPieces.size() == 2) {
                        View target = undoPieces.get(1);
                        undoDestination.addView(target);
                    }
                    if (undoPromotion) {
                        ImageView image = (ImageView) promotionView;
                        if (undoPawnWhite) {
                            image.setImageResource(R.drawable.white_pawn);
                        } else {
                            image.setImageResource(R.drawable.black_pawn);
                        }
                    }
                } else if (undoLocations.size() == 3) {
                    View enpass = undoPieces.get(1);
                    FrameLayout enLocation = undoLocations.get(2);
                    undoDestination.removeViewAt(1);
                    undoOrigin.addView(piece);
                    enLocation.addView(enpass);
                } else if (undoLocations.size() == 4) {
                    View rook = undoPieces.get(1);
                    FrameLayout rookLocation = undoLocations.get(2);
                    FrameLayout emptyLocation = undoLocations.get(3);

                    undoDestination.removeViewAt(1);
                    undoOrigin.addView(piece);
                    emptyLocation.removeViewAt(1);
                    rookLocation.addView(rook);
                    rook.setTag(undoTags.get(1));
                }
                undoPieces.clear();
                undoLocations.clear();
                undoTags.clear();
                undoPromotion = false;
            }
        });

        ai_btn.setOnClickListener(button -> {
            text.setText("AI");
            String move = game.randomMove();

            String origin = move.substring(0, 2);
            String destination = move.substring(3, 5);
            int result = Integer.parseInt(move.substring(6));

            FrameLayout currentFrame = getFrame(origin);
            FrameLayout newFrame = getFrame(destination);

            if (result >= 0) {
                View v = currentFrame.getChildAt(1);

                undoLocations.clear();
                undoPieces.clear();
                undoTags.clear();
                undoPromotion = false;

                text.setText("AI: " + origin + " " + destination);

                currentFrame.removeView(v);

                v.setTag(destination);
                if (newFrame.getChildCount() == 1) {
                    newFrame.addView(v);

                    undoPieces.add(v);
                    undoLocations.add(currentFrame);
                    undoLocations.add(newFrame);
                    undoTags.add(origin);
                } else if (newFrame.getChildCount() == 2) {
                    View target = newFrame.getChildAt(1);
                    newFrame.removeView(target);
                    newFrame.addView(v);

                    undoPieces.add(v);
                    undoPieces.add(target);
                    undoLocations.add(currentFrame);
                    undoLocations.add(newFrame);
                    undoTags.add(origin);
                }

                View rook;
                View enpass;
                ImageView image;
                switch (result) {
                    case 1:
                        switch (v.getTag().toString().charAt(0)) {
                            case 'a':
                                enpass = a4.getChildAt(1);
                                a4.removeView(enpass);
                                undoLocations.add(a4);
                                break;
                            case 'b':
                                enpass = b4.getChildAt(1);
                                b4.removeViewAt(1);
                                undoLocations.add(b4);
                                break;
                            case 'c':
                                enpass = c4.getChildAt(1);
                                c4.removeViewAt(1);
                                undoLocations.add(c4);
                                break;
                            case 'd':
                                enpass = d4.getChildAt(1);
                                d4.removeViewAt(1);
                                undoLocations.add(d4);
                                break;
                            case 'e':
                                enpass = e4.getChildAt(1);
                                e4.removeViewAt(1);
                                undoLocations.add(e4);
                                break;
                            case 'f':
                                enpass = f4.getChildAt(1);
                                f4.removeViewAt(1);
                                undoLocations.add(f4);
                                break;
                            case 'g':
                                enpass = g4.getChildAt(1);
                                g4.removeViewAt(1);
                                undoLocations.add(g4);
                                break;
                            case 'h':
                                enpass = h4.getChildAt(1);
                                h4.removeViewAt(1);
                                undoLocations.add(h4);
                                break;
                            default:
                                enpass = null;
                        }
                        undoPieces.add(enpass);
                        break;
                    case 2:
                        switch (v.getTag().toString().charAt(0)) {
                            case 'a':
                                enpass = a5.getChildAt(1);
                                a5.removeViewAt(1);
                                undoLocations.add(a5);
                                break;
                            case 'b':
                                enpass = b5.getChildAt(1);
                                b5.removeViewAt(1);
                                undoLocations.add(b5);
                                break;
                            case 'c':
                                enpass = c5.getChildAt(1);
                                c5.removeViewAt(1);
                                undoLocations.add(c5);
                                break;
                            case 'd':
                                enpass = d5.getChildAt(1);
                                d5.removeViewAt(1);
                                undoLocations.add(d5);
                                break;
                            case 'e':
                                enpass = e5.getChildAt(1);
                                e5.removeViewAt(1);
                                undoLocations.add(e5);
                                break;
                            case 'f':
                                enpass = f5.getChildAt(1);
                                f5.removeViewAt(1);
                                undoLocations.add(f5);
                                break;
                            case 'g':
                                enpass = g5.getChildAt(1);
                                g5.removeViewAt(1);
                                undoLocations.add(g5);
                                break;
                            case 'h':
                                enpass = h5.getChildAt(1);
                                h5.removeViewAt(1);
                                undoLocations.add(h5);
                                break;
                            default:
                                enpass = null;
                        }
                        undoPieces.add(enpass);
                        break;
                    case 3:
                        image = (ImageView) v;
                        image.setImageResource(R.drawable.white_queen);
                        result = game.play("wQ");

                        promotionView = v;
                        undoPawnWhite = true;
                        undoPromotion = true;
                        break;
                    case 4:
                        image = (ImageView) v;
                        image.setImageResource(R.drawable.white_queen);
                        result = game.play("bQ");

                        promotionView = v;
                        undoPawnWhite = true;
                        undoPromotion = true;
                        break;
                    case 5:
                        rook = h1.getChildAt(1);
                        h1.removeView(rook);
                        f1.addView(rook);
                        undoPieces.add(rook);
                        undoLocations.add(h1);
                        undoLocations.add(f1);
                        undoTags.add("h1");
                        rook.setTag("f1");
                        break;
                    case 6:
                        rook = a1.getChildAt(1);
                        a1.removeView(rook);
                        d1.addView(rook);
                        undoPieces.add(rook);
                        undoLocations.add(a1);
                        undoLocations.add(d1);
                        undoTags.add("a1");
                        rook.setTag("d1");
                        break;
                    case 7:
                        rook = h8.getChildAt(1);
                        h8.removeView(rook);
                        f8.addView(rook);
                        undoPieces.add(rook);
                        undoLocations.add(h8);
                        undoLocations.add(f8);
                        undoTags.add("h8");
                        rook.setTag("f8");
                        break;
                    case 8:
                        rook = a8.getChildAt(1);
                        a8.removeView(rook);
                        d8.addView(rook);
                        undoPieces.add(rook);
                        undoLocations.add(a8);
                        undoLocations.add(d8);
                        undoTags.add("a8");
                        rook.setTag("d8");
                        break;
                }

                if (result == 9 || result == 10) {
                    text.setText(text.getText() + " Check");
                } else if (result == 11 || result == 12) {
                    Intent intent = new Intent(ChessPlay.this, PlayGameOverPopup.class);
                    intent.putExtra("cause","by Checkmate");
                    startActivityForResult(intent, 2);
                }
            }
        });

        draw_btn.setOnClickListener(button -> {
            text.setText("Draw");
            Intent intent = new Intent(ChessPlay.this, PlayGameOverPopup.class);
            intent.putExtra("cause","by Draw");
            startActivityForResult(intent, 2);
        });

        resign_btn.setOnClickListener(button -> {
            text.setText("Resign");
            Intent intent = new Intent(ChessPlay.this, PlayGameOverPopup.class);
            intent.putExtra("cause","by Resignation");
            startActivityForResult(intent, 2);
        });
    }

    View.OnClickListener pieceClickListener = view -> {
        if (viewHolder != null) {
            View v = viewHolder;
            String origin = v.getTag().toString();
            String destination = view.getTag().toString();
            int result = game.play(origin + " " + destination);

            if (result >= 0) {
                undoLocations.clear();
                undoPieces.clear();
                undoTags.clear();
                undoPromotion = false;

                text.setText(origin + " " + destination);

                FrameLayout currentFrame = (FrameLayout) v.getParent();
                currentFrame.removeView(v);

                FrameLayout newFrame = (FrameLayout) view.getParent();
                View target = newFrame.getChildAt(1);
                newFrame.removeView(target);
                newFrame.addView(v);
                v.setTag(destination);

                undoPieces.add(v);
                undoPieces.add(target);
                undoLocations.add(currentFrame);
                undoLocations.add(newFrame);
                undoTags.add(origin);

                switch (result) {
                    case 3:
                        startActivityForResult(new Intent(ChessPlay.this, PlayWhitePromotionPopup.class), 1);
                        promotionView = v;
                        undoPromotion = true;
                        break;
                    case 4:
                        startActivityForResult(new Intent(ChessPlay.this, PlayBlackPromotionPopup.class), 1);
                        promotionView = v;
                        undoPromotion = true;
                        break;
                }

                if (result == 9 || result == 10) {
                    text.setText(text.getText() + " Check");
                } else if (result == 11 || result == 12) {
                    Intent intent = new Intent(ChessPlay.this, PlayGameOverPopup.class);
                    intent.putExtra("cause", "by Checkmate");
                    startActivityForResult(intent, 2);
                }
            }
        }
        viewHolder = view;
    };

    View.OnClickListener tileClickListener = view -> {
        if (viewHolder != null) {
            View v = viewHolder;
            String origin = v.getTag().toString();
            String destination = view.getTag().toString();

            int result = game.play(origin + " " + destination);
            if (result >= 0) {
                undoLocations.clear();
                undoPieces.clear();
                undoTags.clear();
                undoPromotion = false;

                text.setText(origin + " " + destination);

                FrameLayout currentFrame = (FrameLayout) v.getParent();
                currentFrame.removeView(v);

                FrameLayout newFrame = (FrameLayout) view;
                newFrame.addView(v);
                v.setTag(destination);

                undoPieces.add(v);
                undoLocations.add(currentFrame);
                undoLocations.add(newFrame);
                undoTags.add(origin);

                View rook;
                View enpass;
                switch (result) {
                    case 1:
                        switch (v.getTag().toString().charAt(0)) {
                            case 'a':
                                enpass = a4.getChildAt(1);
                                a4.removeView(enpass);
                                undoLocations.add(a4);
                                break;
                            case 'b':
                                enpass = b4.getChildAt(1);
                                b4.removeViewAt(1);
                                undoLocations.add(b4);
                                break;
                            case 'c':
                                enpass = c4.getChildAt(1);
                                c4.removeViewAt(1);
                                undoLocations.add(c4);
                                break;
                            case 'd':
                                enpass = d4.getChildAt(1);
                                d4.removeViewAt(1);
                                undoLocations.add(d4);
                                break;
                            case 'e':
                                enpass = e4.getChildAt(1);
                                e4.removeViewAt(1);
                                undoLocations.add(e4);
                                break;
                            case 'f':
                                enpass = f4.getChildAt(1);
                                f4.removeViewAt(1);
                                undoLocations.add(f4);
                                break;
                            case 'g':
                                enpass = g4.getChildAt(1);
                                g4.removeViewAt(1);
                                undoLocations.add(g4);
                                break;
                            case 'h':
                                enpass = h4.getChildAt(1);
                                h4.removeViewAt(1);
                                undoLocations.add(h4);
                                break;
                            default:
                                enpass = null;
                        }
                        undoPieces.add(enpass);
                        break;
                    case 2:
                        switch (v.getTag().toString().charAt(0)) {
                            case 'a':
                                enpass = a5.getChildAt(1);
                                a5.removeViewAt(1);
                                undoLocations.add(a5);
                                break;
                            case 'b':
                                enpass = b5.getChildAt(1);
                                b5.removeViewAt(1);
                                undoLocations.add(b5);
                                break;
                            case 'c':
                                enpass = c5.getChildAt(1);
                                c5.removeViewAt(1);
                                undoLocations.add(c5);
                                break;
                            case 'd':
                                enpass = d5.getChildAt(1);
                                d5.removeViewAt(1);
                                undoLocations.add(d5);
                                break;
                            case 'e':
                                enpass = e5.getChildAt(1);
                                e5.removeViewAt(1);
                                undoLocations.add(e5);
                                break;
                            case 'f':
                                enpass = f5.getChildAt(1);
                                f5.removeViewAt(1);
                                undoLocations.add(f5);
                                break;
                            case 'g':
                                enpass = g5.getChildAt(1);
                                g5.removeViewAt(1);
                                undoLocations.add(g5);
                                break;
                            case 'h':
                                enpass = h5.getChildAt(1);
                                h5.removeViewAt(1);
                                undoLocations.add(h5);
                                break;
                            default:
                                enpass = null;
                        }
                        undoPieces.add(enpass);
                        break;
                    case 3:
                        startActivityForResult(new Intent(ChessPlay.this, PlayWhitePromotionPopup.class), 1);
                        promotionView = v;
                        undoPromotion = true;
                        break;
                    case 4:
                        startActivityForResult(new Intent(ChessPlay.this, PlayBlackPromotionPopup.class), 1);
                        promotionView = v;
                        undoPromotion = true;
                        break;
                    case 5:
                        rook = h1.getChildAt(1);
                        h1.removeView(rook);
                        f1.addView(rook);
                        undoPieces.add(rook);
                        undoLocations.add(h1);
                        undoLocations.add(f1);
                        undoTags.add("h1");
                        rook.setTag("f1");
                        break;
                    case 6:
                        rook = a1.getChildAt(1);
                        a1.removeView(rook);
                        d1.addView(rook);
                        undoPieces.add(rook);
                        undoLocations.add(a1);
                        undoLocations.add(d1);
                        undoTags.add("a1");
                        rook.setTag("d1");
                        break;
                    case 7:
                        rook = h8.getChildAt(1);
                        h8.removeView(rook);
                        f8.addView(rook);
                        undoPieces.add(rook);
                        undoLocations.add(h8);
                        undoLocations.add(f8);
                        undoTags.add("h8");
                        rook.setTag("f8");
                        break;
                    case 8:
                        rook = a8.getChildAt(1);
                        a8.removeView(rook);
                        d8.addView(rook);
                        undoPieces.add(rook);
                        undoLocations.add(a8);
                        undoLocations.add(d8);
                        undoTags.add("a8");
                        rook.setTag("d8");
                        break;
                }

                if (result == 9 || result == 10) {
                    text.setText(text.getText() + " Check");
                } else if (result == 11 || result == 12) {
                    Intent intent = new Intent(ChessPlay.this, PlayGameOverPopup.class);
                    intent.putExtra("cause","by Checkmate");
                    startActivityForResult(intent, 2);
                }
            }
            viewHolder = null;
        }
    };

    View.OnLongClickListener longClickListener = view -> {
        ClipData data = ClipData.newPlainText("", "");
        View.DragShadowBuilder myShadowBuilder = new View.DragShadowBuilder(view);
        view.startDragAndDrop(data, myShadowBuilder, view, 0);
        return false;
    };

    View.OnDragListener dragListener = (view, event) -> {
        int dragEvent = event.getAction();
        final View v = (View) event.getLocalState();

        switch (dragEvent) {
            case DragEvent.ACTION_DRAG_ENTERED:
            case DragEvent.ACTION_DRAG_EXITED:
                break;
            case DragEvent.ACTION_DROP:
                String origin = v.getTag().toString();
                String destination = view.getTag().toString();
                int result = game.play(origin + " " + destination);

                if (result >= 0) {
                    undoLocations.clear();
                    undoPieces.clear();
                    undoTags.clear();
                    undoPromotion = false;

                    text.setText(origin + " " + destination);

                    FrameLayout currentFrame = (FrameLayout) v.getParent();
                    currentFrame.removeView(v);

                    FrameLayout newFrame = (FrameLayout) view;

                    v.setTag(destination);
                    if (newFrame.getChildCount() == 1) {
                        newFrame.addView(v);

                        undoPieces.add(v);
                        undoLocations.add(currentFrame);
                        undoLocations.add(newFrame);
                        undoTags.add(origin);
                    } else if (newFrame.getChildCount() == 2) {
                        View target = newFrame.getChildAt(1);
                        newFrame.removeView(target);
                        newFrame.addView(v);

                        undoPieces.add(v);
                        undoPieces.add(target);
                        undoLocations.add(currentFrame);
                        undoLocations.add(newFrame);
                        undoTags.add(origin);
                    }

                    View rook;
                    View enpass;
                    switch (result) {
                        case 1:
                            switch (v.getTag().toString().charAt(0)) {
                                case 'a':
                                    enpass = a4.getChildAt(1);
                                    a4.removeView(enpass);
                                    undoLocations.add(a4);
                                    break;
                                case 'b':
                                    enpass = b4.getChildAt(1);
                                    b4.removeViewAt(1);
                                    undoLocations.add(b4);
                                    break;
                                case 'c':
                                    enpass = c4.getChildAt(1);
                                    c4.removeViewAt(1);
                                    undoLocations.add(c4);
                                    break;
                                case 'd':
                                    enpass = d4.getChildAt(1);
                                    d4.removeViewAt(1);
                                    undoLocations.add(d4);
                                    break;
                                case 'e':
                                    enpass = e4.getChildAt(1);
                                    e4.removeViewAt(1);
                                    undoLocations.add(e4);
                                    break;
                                case 'f':
                                    enpass = f4.getChildAt(1);
                                    f4.removeViewAt(1);
                                    undoLocations.add(f4);
                                    break;
                                case 'g':
                                    enpass = g4.getChildAt(1);
                                    g4.removeViewAt(1);
                                    undoLocations.add(g4);
                                    break;
                                case 'h':
                                    enpass = h4.getChildAt(1);
                                    h4.removeViewAt(1);
                                    undoLocations.add(h4);
                                    break;
                                default:
                                    enpass = null;
                            }
                            undoPieces.add(enpass);
                            break;
                        case 2:
                            switch (v.getTag().toString().charAt(0)) {
                                case 'a':
                                    enpass = a5.getChildAt(1);
                                    a5.removeViewAt(1);
                                    undoLocations.add(a5);
                                    break;
                                case 'b':
                                    enpass = b5.getChildAt(1);
                                    b5.removeViewAt(1);
                                    undoLocations.add(b5);
                                    break;
                                case 'c':
                                    enpass = c5.getChildAt(1);
                                    c5.removeViewAt(1);
                                    undoLocations.add(c5);
                                    break;
                                case 'd':
                                    enpass = d5.getChildAt(1);
                                    d5.removeViewAt(1);
                                    undoLocations.add(d5);
                                    break;
                                case 'e':
                                    enpass = e5.getChildAt(1);
                                    e5.removeViewAt(1);
                                    undoLocations.add(e5);
                                    break;
                                case 'f':
                                    enpass = f5.getChildAt(1);
                                    f5.removeViewAt(1);
                                    undoLocations.add(f5);
                                    break;
                                case 'g':
                                    enpass = g5.getChildAt(1);
                                    g5.removeViewAt(1);
                                    undoLocations.add(g5);
                                    break;
                                case 'h':
                                    enpass = h5.getChildAt(1);
                                    h5.removeViewAt(1);
                                    undoLocations.add(h5);
                                    break;
                                default:
                                    enpass = null;
                            }
                            undoPieces.add(enpass);
                            break;
                        case 3:
                            startActivityForResult(new Intent(ChessPlay.this, PlayWhitePromotionPopup.class), 1);
                            promotionView = v;
                            undoPromotion = true;
                            break;
                        case 4:
                            startActivityForResult(new Intent(ChessPlay.this, PlayBlackPromotionPopup.class), 1);
                            promotionView = v;
                            undoPromotion = true;
                            break;
                        case 5:
                            rook = h1.getChildAt(1);
                            h1.removeView(rook);
                            f1.addView(rook);
                            undoPieces.add(rook);
                            undoLocations.add(h1);
                            undoLocations.add(f1);
                            undoTags.add("h1");
                            rook.setTag("f1");
                            break;
                        case 6:
                            rook = a1.getChildAt(1);
                            a1.removeView(rook);
                            d1.addView(rook);
                            undoPieces.add(rook);
                            undoLocations.add(a1);
                            undoLocations.add(d1);
                            undoTags.add("a1");
                            rook.setTag("d1");
                            break;
                        case 7:
                            rook = h8.getChildAt(1);
                            h8.removeView(rook);
                            f8.addView(rook);
                            undoPieces.add(rook);
                            undoLocations.add(h8);
                            undoLocations.add(f8);
                            undoTags.add("h8");
                            rook.setTag("f8");
                            break;
                        case 8:
                            rook = a8.getChildAt(1);
                            a8.removeView(rook);
                            d8.addView(rook);
                            undoPieces.add(rook);
                            undoLocations.add(a8);
                            undoLocations.add(d8);
                            undoTags.add("a8");
                            rook.setTag("d8");
                            break;
                    }

                    if (result == 9 || result == 10) {
                        text.setText(text.getText() + " Check");
                    } else if (result == 11 || result == 12) {
                        Intent intent = new Intent(ChessPlay.this, PlayGameOverPopup.class);
                        intent.putExtra("cause","by Checkmate");
                        startActivityForResult(intent, 2);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            ImageView piece = (ImageView) promotionView;
            int result = -1;
            switch (data.getStringExtra("promotion")) {
                case "wQ":
                    piece.setImageResource(R.drawable.white_queen);
                    result = game.play("wQ");
                    undoPawnWhite = true;
                    break;
                case "wR":
                    piece.setImageResource(R.drawable.white_rook);
                    result = game.play("wR");
                    undoPawnWhite = true;
                    break;
                case "wB":
                    piece.setImageResource(R.drawable.white_bishop);
                    result = game.play("wB");
                    undoPawnWhite = true;
                    break;
                case "wN":
                    piece.setImageResource(R.drawable.white_knight);
                    result = game.play("wN");
                    undoPawnWhite = true;
                    break;
                case "bQ":
                    piece.setImageResource(R.drawable.black_queen);
                    result = game.play("bQ");
                    undoPawnWhite = false;
                    break;
                case "bR":
                    piece.setImageResource(R.drawable.black_rook);
                    result = game.play("bR");
                    undoPawnWhite = false;
                    break;
                case "bB":
                    piece.setImageResource(R.drawable.black_bishop);
                    result = game.play("bB");
                    undoPawnWhite = false;
                    break;
                case "bN":
                    piece.setImageResource(R.drawable.black_knight);
                    result = game.play("bN");
                    undoPawnWhite = false;
                    break;
            }

            if (result == 9 || result == 10) {
                text.setText(text.getText() + " Check");
            } else if (result == 11 || result == 12) {
                Intent intent = new Intent(ChessPlay.this, PlayGameOverPopup.class);
                intent.putExtra("cause","by Checkmate");
                startActivityForResult(intent, 2);
            }
        } else if (requestCode == 2) {
            if (data.getStringExtra("result").equals("save")) {
                String moveHistory = game.getMoveHistory().stream().collect(Collectors.joining(","));
                String title = data.getStringExtra("title");
                String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());

                SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
                SharedPreferences.Editor editor = settings.edit();
                Set<String> set = settings.getStringSet("set", null);
                set.add(title + "\t\t\t" + date);
                editor.putStringSet("set", set);
                editor.putString(title, moveHistory);
                editor.apply();

                finish();
            } else if (data.getStringExtra("result").equals("exit")) {
                finish();
            }
        }
    }

    private void init() {
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

    private FrameLayout getFrame(String input) {
        switch (input) {
            case "a1":
                return a1;
            case "a2":
                return a2;
            case "a3":
                return a3;
            case "a4":
                return a4;
            case "a5":
                return a5;
            case "a6":
                return a6;
            case "a7":
                return a7;
            case "a8":
                return a8;
            case "b1":
                return b1;
            case "b2":
                return b2;
            case "b3":
                return b3;
            case "b4":
                return b4;
            case "b5":
                return b5;
            case "b6":
                return b6;
            case "b7":
                return b7;
            case "b8":
                return b8;
            case "c1":
                return c1;
            case "c2":
                return c2;
            case "c3":
                return c3;
            case "c4":
                return c4;
            case "c5":
                return c5;
            case "c6":
                return c6;
            case "c7":
                return c7;
            case "c8":
                return c8;
            case "d1":
                return d1;
            case "d2":
                return d2;
            case "d3":
                return d3;
            case "d4":
                return d4;
            case "d5":
                return d5;
            case "d6":
                return d6;
            case "d7":
                return d7;
            case "d8":
                return d8;
            case "e1":
                return e1;
            case "e2":
                return e2;
            case "e3":
                return e3;
            case "e4":
                return e4;
            case "e5":
                return e5;
            case "e6":
                return e6;
            case "e7":
                return e7;
            case "e8":
                return e8;
            case "f1":
                return f1;
            case "f2":
                return f2;
            case "f3":
                return f3;
            case "f4":
                return f4;
            case "f5":
                return f5;
            case "f6":
                return f6;
            case "f7":
                return f7;
            case "f8":
                return f8;
            case "g1":
                return g1;
            case "g2":
                return g2;
            case "g3":
                return g3;
            case "g4":
                return g4;
            case "g5":
                return g5;
            case "g6":
                return g6;
            case "g7":
                return g7;
            case "g8":
                return g8;
            case "h1":
                return h1;
            case "h2":
                return h2;
            case "h3":
                return h3;
            case "h4":
                return h4;
            case "h5":
                return h5;
            case "h6":
                return h6;
            case "h7":
                return h7;
            case "h8":
                return h8;
            default:
                return null;
        }
    }
}