package com.example.chess_android_app.models;

public class Knight extends Tile {

    int[][] moves = {  {2,1},
            {2,-1},
            {-2,1},
            {-2,-1},

            {1,-2},
            {1,2},
            {-1,-2},
            {-1,2} };

    public Knight(String boardName) {
        super(boardName);
        super.pieceName = "Knight";
        super.moves = this.moves;
    }
}
