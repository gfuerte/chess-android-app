package com.example.chess_android_app.models;

public class Pawn extends Tile {
    int[][] moves = {{-1, 0}, {-1,1}, {-1,-1}, {1, 0}, {1,1}, {1,-1}};

    public Pawn(String boardName) {
        super(boardName);
        super.pieceName = "Pawn";
        super.moves = this.moves;
    }
}
