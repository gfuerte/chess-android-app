package com.example.chess_android_app.models;

public class King extends Tile {
    int[][] moves = {  {1,0},
            {-1,0},
            {0,1},
            {0,-1},

            {1,1},
            {1,-1},
            {-1,1},
            {-1,-1}};

    public King(String boardName) {
        super(boardName);
        super.pieceName = "King";
        super.moves = this.moves;
    }
}
