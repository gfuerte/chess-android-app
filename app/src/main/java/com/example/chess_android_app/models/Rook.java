package com.example.chess_android_app.models;

public class Rook extends Tile {
    int[][] moves = {
            {1,0},
            {2,0},
            {3,0},
            {4,0},
            {5,0},
            {6,0},
            {7,0},

            {-1,0},
            {-2,0},
            {-3,0},
            {-4,0},
            {-5,0},
            {-6,0},
            {-7,0},

            {0,1},
            {0,2},
            {0,3},
            {0,4},
            {0,5},
            {0,6},
            {0,7},

            {0,-1},
            {0,-2},
            {0,-3},
            {0,-4},
            {0,-5},
            {0,-6},
            {0,-7} };

    public Rook(String boardName) {
        super(boardName);
        super.pieceName = "Rook";
        super.moves = this.moves;
    }
}
