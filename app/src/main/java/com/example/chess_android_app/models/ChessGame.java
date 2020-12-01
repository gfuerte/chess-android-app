package com.example.chess_android_app.models;

public class ChessGame {
    private Tile[][] board;

    private int[] wKing;
    private int[] bKing;

    private int whiteCheck;
    private int blackCheck;

    private boolean whitesMove;
    private boolean drawCheck;

    private String previousMethod;

    public ChessGame() {
        this.board = new Tile[8][8];
        initBoard();

        this.wKing = new int[]{7, 4};
        this.bKing = new int[]{0, 4};
        this.whiteCheck = 0;
        this.blackCheck = 0;
        this.previousMethod = "";

        this.whitesMove = true;
        this.drawCheck = false;
    }

    public int play(String input) {
        /*
        -1: Input Error
        0: Normal Turn
        1: Check on White King
        2: Check on Black King
        3: Checkmate on White King
        4: Checkmate on Black King
         */
        int[] origin = getIndex(input.substring(0, 2));
        int[] destination = getIndex(input.substring(3, 5));
        if (validateMove(origin, destination, input, whitesMove)) {
            check();
            if (whitesMove && blackCheck >= 1) {
                if (checkmate("black")) {
                    System.out.println("Checkmate\n");
                    System.out.println("White wins");
                    return 4;
                }
                previousMethod = input.substring(0, 5);
                whitesMove = ((whitesMove) ? false : true);
                return 2;
            } else if (!whitesMove && whiteCheck >= 1) {
                if (checkmate("white")) {
                    System.out.println("Checkmate\n");
                    System.out.println("Black wins");
                    return 3;
                }
                previousMethod = input.substring(0, 5);
                whitesMove = ((whitesMove) ? false : true);
                return 1;
            }
            previousMethod = input.substring(0, 5);
            whitesMove = ((whitesMove) ? false : true);
        } else {
            return -1;
        }
        return 0;
    }


    private boolean validPrevious(String prev) {
        String[] arr = {"a2 a4", "b2 b4", "c2 c4", "d2 d4", "e2 e4", "f2 f4", "g2 g4", "h2 h4", "a7 a5", "b7 b5",
                "c7 c5", "d7 d5", "e7 e5", "f7 f5", "g7 g5", "h7 h5"};

        for (int i = 0; i < arr.length; i++) {
            if (prev.equals(arr[i])) {
                return true;
            }
        }
        return false;
    }

    private boolean validateMove(int[] origin, int[] destination, String input, boolean whitesMove) {
        if (destination[0] < 0 || destination[0] > 7 || destination[1] < 0 || destination[0] > 7) {
            return false;
        }

        Tile piece = board[origin[0]][origin[1]];
        Tile target = board[destination[0]][destination[1]];
        int[][] moves = piece.getMoves();

        if (piece.getOccupation() == false) {
            return false;
        }

        if ((whitesMove == true && piece.getTeam().equals("black"))
                || (whitesMove == false && piece.getTeam().equals("white"))) {
            return false;
        }

        if (piece.getTeam().equals(target.getTeam())) {
            return false;
        }

        if (piece.getPieceName().equals("Pawn")) { // Pawn
            boolean validMove = false;
            if (piece.getTeam().equals("white")) {
                int movesW[][] = {{-1, 0}};
                int moves2W[][] = {{-2, 0}};
                int attackW[][] = {{-1, 1}, {-1, -1}};
                if (checkCollisions(origin, destination, moves2W) && piece.getFirstMove()) { // double move
                    if (!target.getOccupation()) {
                        validMove = true;
                    }
                } else if (checkMove(origin, destination, movesW)) { // normal move
                    if (!target.getOccupation()) {
                        validMove = true;
                    }
                } else if (checkMove(origin, destination, attackW)) { // attack move
                    if (target.getOccupation()) {
                        validMove = true;
                    } else { // en passant
                        if (validPrevious(previousMethod)) {
                            int[] prevIndex = getIndex(previousMethod.substring(3, 5));
                            if ((prevIndex[0] - destination[0] == 1) && (prevIndex[1] == destination[1])) {
                                Tile temp = board[prevIndex[0]][prevIndex[1]];
                                movePiece(origin, destination, piece, target);
                                board[prevIndex[0]][prevIndex[1]] = new Tile();
                                if (checksOwnKing(piece.getTeam())) {
                                    reverseMove(origin, destination, temp, target);
                                    board[prevIndex[0]][prevIndex[1]] = temp;
                                    return false;
                                } else {
                                    if (piece.getFirstMove())
                                        piece.setFirstMove(false);
                                    return true;
                                }
                            }
                        }
                    }
                }
            } else if (piece.getTeam().equals("black")) {
                int movesB[][] = {{1, 0}};
                int moves2B[][] = {{2, 0}};
                int attackB[][] = {{1, 1}, {1, -1}};

                if (checkCollisions(origin, destination, moves2B) && piece.getFirstMove()) { // double move
                    if (!target.getOccupation()) {
                        validMove = true;
                    }
                } else if (checkMove(origin, destination, movesB)) { // normal move
                    if (!target.getOccupation()) {
                        validMove = true;
                    }
                } else if (checkMove(origin, destination, attackB)) { // attack move
                    if (target.getOccupation()) {
                        validMove = true;
                    } else { // en passant
                        if (validPrevious(previousMethod)) {
                            int[] prevIndex = getIndex(previousMethod.substring(3, 5));
                            if ((destination[0] - prevIndex[0] == 1) && (destination[1] == prevIndex[1])) {
                                Tile temp = board[prevIndex[0]][prevIndex[1]];
                                movePiece(origin, destination, piece, target);
                                board[prevIndex[0]][prevIndex[1]] = new Tile();
                                if (checksOwnKing(piece.getTeam())) {
                                    reverseMove(origin, destination, temp, target);
                                    board[prevIndex[0]][prevIndex[1]] = temp;
                                    return false;
                                } else {
                                    if (piece.getFirstMove())
                                        piece.setFirstMove(false);
                                    return true;
                                }
                            }
                        }
                    }
                }
            }

            if (validMove) {
                Tile temp = piece;
                if (destination[0] == 0 || destination[0] == 7) { // promotion
                    piece = (piece.getTeam().equals("white")) ? pawnPromotion(input, "white")
                            : pawnPromotion(input, "black");
                    movePiece(origin, destination, piece, target);
                    if (checksOwnKing(piece.getTeam())) {
                        reverseMove(origin, destination, temp, target);
                        return false;
                    }
                } else {
                    if (input.length() == 7 || input.length() == 13) return false;
                    movePiece(origin, destination, piece, target);
                    if (checksOwnKing(piece.getTeam())) {
                        reverseMove(origin, destination, piece, target);
                        return false;
                    }
                }
            } else {
                return false;
            }
            if (piece.getFirstMove())
                piece.setFirstMove(false);

        } else if (piece.getPieceName().equals("Knight")) {
            // Case: Knight jump over pieces, no need for collision check
            if (checkMove(origin, destination, moves)) {
                movePiece(origin, destination, piece, target);
                if (checksOwnKing(piece.getTeam())) {
                    reverseMove(origin, destination, piece, target);
                    return false;
                } else {
                    piece.setFirstMove(false);
                }
            } else {
                return false;
            }
        } else if (piece.getPieceName().equals("King")) { // King
            if (piece.getTeam().equals("white") && validCastling()) {
                // if its King is white and going to right side towards rook
                if (destination[0] == 7 && destination[1] == 6) {
                    check();
                    if (whiteCheck > 0) return false;

                    int[] temp = {7, 5};
                    movePiece(origin, temp, piece, board[7][5]);
                    check();
                    if (whiteCheck > 0) {
                        reverseMove(origin, temp, piece, board[7][4]);
                        return false;
                    }

                    movePiece(origin, destination, piece, target);
                    board[7][5] = board[7][7];
                    board[7][7] = new Tile();

                    check();
                    if (whiteCheck > 0) {
                        reverseCastle(destination, "white");
                        return false;
                    }
                    board[7][5].setFirstMove(false);
                } else if (destination[0] == 7 && destination[1] == 2) { // if king is white and going to left side
                    check();
                    if (whiteCheck > 0) return false;

                    int[] temp = {7, 3};
                    movePiece(origin, temp, piece, board[7][3]);
                    check();
                    if (whiteCheck > 0) {
                        reverseMove(origin, temp, piece, board[7][4]);
                        return false;
                    }

                    movePiece(origin, destination, piece, target);
                    board[7][3] = board[7][0];
                    board[7][0] = new Tile();

                    check();
                    if (whiteCheck > 0) {
                        reverseCastle(destination, "white");
                        return false;
                    }
                    board[7][3].setFirstMove(false);
                } else {
                    if (!checkMove(origin, destination, moves)) return false;
                    movePiece(origin, destination, piece, target);
                    if (piece.getTeam().equals("white") && checksOwnKing("white")) {
                        reverseMove(origin, destination, piece, target);
                        return false;
                    }
                }
            } else if (piece.getTeam().equals("black") && validCastling()) {
                // if its King is black and going to right side towards rook
                if (destination[0] == 0 && destination[1] == 6) {
                    check();
                    if (blackCheck > 0) return false;

                    int[] temp = {0, 5};
                    movePiece(origin, temp, piece, board[0][5]);
                    check();
                    if (blackCheck > 0) {
                        reverseMove(origin, temp, piece, board[0][4]);
                        return false;
                    }

                    movePiece(origin, destination, piece, target);
                    board[0][5] = board[0][7];
                    board[0][7] = new Tile();

                    check();
                    if (blackCheck > 0) {
                        reverseCastle(destination, "black");
                        return false;
                    }
                    board[0][5].setFirstMove(false);
                } else if (destination[0] == 0 && destination[1] == 2) { // if king is black and going to left side
                    check();
                    if (blackCheck > 0) return false;

                    int[] temp = {0, 3};
                    movePiece(origin, temp, piece, board[0][3]);
                    check();
                    if (blackCheck > 0) {
                        reverseMove(origin, temp, piece, board[0][4]);
                        return false;
                    }

                    movePiece(origin, destination, piece, target);
                    board[0][3] = board[0][0];
                    board[0][0] = new Tile();

                    check();
                    if (blackCheck > 0) {
                        reverseCastle(destination, "black");
                        return false;
                    }
                    board[0][3].setFirstMove(false);
                } else {
                    if (!checkMove(origin, destination, moves)) return false;
                    movePiece(origin, destination, piece, target); // for now
                    if (piece.getTeam().equals("black") && checksOwnKing("black")) {
                        reverseMove(origin, destination, piece, target);
                        return false;
                    }
                }
            } else { // normal move
                if (!checkMove(origin, destination, moves)) return false;
                movePiece(origin, destination, piece, target);
                if (piece.getTeam().equals("white") && checksOwnKing("white")) {
                    reverseMove(origin, destination, piece, target);
                    return false;
                } else if (piece.getTeam().equals("black") && checksOwnKing("black")) {
                    reverseMove(origin, destination, piece, target);
                    return false;
                }
            }
            piece.setFirstMove(false);

        } else { // Queen, Bishop, Rook no special rules to mind
            if (checkCollisions(origin, destination, moves)) {
                movePiece(origin, destination, piece, target);
                if (checksOwnKing(piece.getTeam())) {
                    reverseMove(origin, destination, piece, target);
                    return false;
                } else {
                    piece.setFirstMove(false);
                }
            } else {
                return false;
            }
        }
        return true;
    }

    private boolean checkCollisions(int[] origin, int[] destination, int[][] moves) {
        int count = -1;
        for (int i = 0; i < moves.length; i++) {
            if (origin[0] + moves[i][0] == destination[0] && origin[1] + moves[i][1] == destination[1]) {
                return true;
            }
            count++;
            try {
                Tile temp = board[origin[0] + moves[i][0]][origin[1] + moves[i][1]];
                if (temp.getOccupation() == true) { // collision has occurred
                    i = i + 6 - count;
                    count = -1;
                }

                if (count == 6)
                    count = 0;
            } catch (ArrayIndexOutOfBoundsException e) {
            }
        }
        return false;
    }

    private boolean checkMove(int[] origin, int[] destination, int[][] moves) {
        for (int i = 0; i < moves.length; i++) {
            if (origin[0] + moves[i][0] == destination[0] && origin[1] + moves[i][1] == destination[1]) {
                return true;
            }
        }
        return false;
    }

    private void movePiece(int[] origin, int[] destination, Tile piece, Tile target) {
        if (target.getTeam().equals("")) {
            board[destination[0]][destination[1]] = piece;
            board[origin[0]][origin[1]] = target;
        } else {
            board[destination[0]][destination[1]] = piece;
            board[origin[0]][origin[1]] = new Tile();
        }

        if (piece.getPieceName().equals("King")) {
            if (piece.getTeam().equals("white")) {
                wKing[0] = destination[0];
                wKing[1] = destination[1];
            } else if (piece.getTeam().equals("black")) {
                bKing[0] = destination[0];
                bKing[1] = destination[1];
            }
        }
    }

    private void reverseMove(int[] origin, int[] destination, Tile piece, Tile target) {
        board[destination[0]][destination[1]] = target;
        board[origin[0]][origin[1]] = piece;

        if (piece.getPieceName().equals("King")) {
            if (piece.getTeam().equals("white")) {
                wKing[0] = origin[0];
                wKing[1] = origin[1];
            } else if (piece.getTeam().equals("black")) {
                bKing[0] = origin[0];
                bKing[1] = origin[1];
            }
        }
    }

    private void reverseCastle(int[] origin, String team) {
        if (team.equals("white")) {
            if (origin[0] == 7 && origin[1] == 2) {
                board[7][0] = board[7][3];
                board[7][3] = new Tile();
                board[7][4] = board[7][2];
                board[7][2] = new Tile();
            } else if (origin[0] == 7 && origin[1] == 6) {
                board[7][7] = board[7][5];
                board[7][5] = new Tile();
                board[7][4] = board[7][6];
                board[7][6] = new Tile();
            }
            wKing[0] = 7;
            wKing[1] = 4;
        } else if (team.equals("black")) {
            if (origin[0] == 0 && origin[1] == 2) {
                board[0][0] = board[0][3];
                board[0][3] = new Tile();
                board[0][4] = board[0][2];
                board[0][2] = new Tile();
            } else if (origin[0] == 0 && origin[1] == 6) {
                board[0][7] = board[0][5];
                board[0][5] = new Tile();
                board[0][4] = board[0][6];
                board[0][6] = new Tile();
            }
            bKing[0] = 0;
            bKing[1] = 4;
        }
    }

    private Tile pawnPromotion(String input, String team) {
        if (input.length() != 7 && input.length() != 13)
            return new Queen(team.charAt(0) + "" + "Q");

        Tile piece = null;
        switch (input.charAt(6)) {
            case 'B':
                piece = new Bishop(team.charAt(0) + "" + input.charAt(6));
                break;
            case 'N':
                piece = new Knight(team.charAt(0) + "" + input.charAt(6));
                break;
            case 'Q':
                piece = new Queen(team.charAt(0) + "" + input.charAt(6));
                break;
            case 'R':
                piece = new Rook(team.charAt(0) + "" + input.charAt(6));
                break;
            default:
                piece = new Queen(team.charAt(0) + "" + input.charAt(6));
        }
        piece.setFirstMove(false);
        return piece;
    }

    private boolean validCastling() {
        if ((board[7][4].getPieceName().equals("King") && board[7][7].getPieceName().equals("Rook"))
                && board[7][4].getTeam().equals(board[7][7].getTeam()) && board[7][4].getFirstMove()
                && board[7][7].getFirstMove() &&
                // check if tiles are empty
                board[7][5].getPieceName().equals("") && board[7][6].getPieceName().equals("")) {
            return true;
        } else if ((board[7][4].getPieceName().equals("King") && board[7][0].getPieceName().equals("Rook"))
                && board[7][4].getTeam().equals(board[7][0].getTeam()) && board[7][4].getFirstMove()
                && board[7][0].getFirstMove() &&
                // check if tiles are empty
                board[7][1].getPieceName().equals("") && board[7][2].getPieceName().equals("")
                && board[7][3].getPieceName().equals("")) {
            return true;
        } else if ((board[0][4].getPieceName().equals("King") && board[0][7].getPieceName().equals("Rook"))
                && board[0][4].getTeam().equals(board[0][7].getTeam()) && board[0][4].getFirstMove()
                && board[0][7].getFirstMove() &&
                // check if tiles are empty
                board[0][5].getPieceName().equals("") && board[0][6].getPieceName().equals("")) {
            return true;
        } else if ((board[0][4].getPieceName().equals("King") && board[0][0].getPieceName().equals("Rook"))
                && board[0][4].getTeam().equals(board[0][0].getTeam()) && board[0][4].getFirstMove()
                && board[0][0].getFirstMove() &&
                // check if tiles are empty
                board[0][1].getPieceName().equals("") && board[0][2].getPieceName().equals("")
                && board[0][3].getPieceName().equals("")) {
            return true;
        }
        return false;
    }

    private boolean checksOwnKing(String team) {
        int wTemp = whiteCheck;
        int bTemp = blackCheck;
        check();
        if (team.equals("white") && whiteCheck >= 1) {
            whiteCheck = wTemp;
            blackCheck = bTemp;
            return true;
        } else if (team.equals("black") && blackCheck >= 1) {
            whiteCheck = wTemp;
            blackCheck = bTemp;
            return true;
        }
        return false;
    }

    private void check() {
        int[][][] moves = {{{1, 0}, {2, 0}, {3, 0}, {4, 0}, {5, 0}, {6, 0}, {7, 0}}, // S
                {{-1, 0}, {-2, 0}, {-3, 0}, {-4, 0}, {-5, 0}, {-6, 0}, {-7, 0}}, // N
                {{0, 1}, {0, 2}, {0, 3}, {0, 4}, {0, 5}, {0, 6}, {0, 7}}, // E
                {{0, -1}, {0, -2}, {0, -3}, {0, -4}, {0, -5}, {0, -6}, {0, -7}}, // W

                {{1, 1}, {2, 2}, {3, 3}, {4, 4}, {5, 5}, {6, 6}, {7, 7}}, // SE
                {{-1, -1}, {-2, -2}, {-3, -3}, {-4, -4}, {-5, -5}, {-6, -6}, {-7, -7}}, // NW
                {{-1, 1}, {-2, 2}, {-3, 3}, {-4, 4}, {-5, 5}, {-6, 6}, {-7, 7}}, // NE
                {{1, -1}, {2, -2}, {3, -3}, {4, -4}, {5, -5}, {6, -6}, {7, -7}}, // SW
                {{2, 1}, {2, -1}, {-2, 1}, {-2, -1}, {1, -2}, {1, 2}, {-1, -2}, {-1, 2}} // Knight
        };

        whiteCheck = 0;
        blackCheck = 0;
        for (int i = 0; i < moves.length; i++) {
            for (int j = 0; j < moves[i].length; j++) {
                try {
                    Tile temp = board[wKing[0] + moves[i][j][0]][wKing[1] + moves[i][j][1]];
                    if (temp.getOccupation()) {
                        if (temp.getTeam().equals("black")) {
                            if (((i == 6 && j == 0) || (i == 5 && j == 0)) && temp.getPieceName().equals("Pawn")) {
                                whiteCheck++;
                            } else if ((i >= 0 && i <= 3)
                                    && (temp.getPieceName().equals("Rook") || temp.getPieceName().equals("Queen"))) {
                                whiteCheck++;
                            } else if ((i >= 4 && i <= 7)
                                    && (temp.getPieceName().equals("Bishop") || temp.getPieceName().equals("Queen"))) {
                                whiteCheck++;
                            } else if (i == 8 && temp.getPieceName().equals("Knight")) {
                                whiteCheck++;
                            }
                        }
                        if (i == 8) continue;
                        break;
                    }
                } catch (ArrayIndexOutOfBoundsException e) {
                    if (i == 8) continue;
                    break;
                }
            }
        }

        for (int i = 0; i < moves.length; i++) {
            for (int j = 0; j < moves[i].length; j++) {
                try {
                    Tile temp = board[bKing[0] + moves[i][j][0]][bKing[1] + moves[i][j][1]];
                    if (temp.getOccupation()) {
                        if (temp.getTeam().equals("white")) {
                            if (((i == 4 && j == 0) || (i == 7 && j == 0)) && temp.getPieceName().equals("Pawn")) {
                                blackCheck++;
                            } else if ((i >= 0 && i <= 3)
                                    && (temp.getPieceName().equals("Rook") || temp.getPieceName().equals("Queen"))) {
                                blackCheck++;
                            } else if ((i >= 4 && i <= 7)
                                    && (temp.getPieceName().equals("Bishop") || temp.getPieceName().equals("Queen"))) {
                                blackCheck++;
                            } else if (i == 8 && temp.getPieceName().equals("Knight")) {
                                blackCheck++;
                            }
                        }
                        if (i == 8) continue;
                        break;
                    }
                } catch (ArrayIndexOutOfBoundsException e) {
                    if (i == 8) continue;
                    break;
                }
            }
        }
    }

    private boolean checkmate(String team) {
        if ((team.equals("white") && whiteCheck == 0) || (team.equals("black") && blackCheck == 0))
            return false;

        if (team.equals("white")) { // white king that is in check
            for (int i = 0; i < board.length; i++) {
                for (int j = 0; j < board[i].length; j++) {
                    Tile piece = board[i][j];
                    boolean firstMove = piece.getFirstMove();
                    if (piece.getTeam().equals("white")) {
                        int[] origin = {i, j};
                        int[][] moves = piece.getMoves();
                        if (piece.getPieceName().equals("Pawn")) {
                            int pawnMoves[][] = {{-1, 0}, {-1, 1}, {-1, -1}, {-2, 0}};
                            for (int k = 0; k < pawnMoves.length; k++) {
                                int[] destination = {i + pawnMoves[k][0], j + pawnMoves[k][1]};
                                try {
                                    Tile target = board[destination[0]][destination[1]];
                                    if (!target.getTeam().equals("white")
                                            && validateMove(origin, destination, "", true)) {
                                        reverseMove(origin, destination, piece, target);
                                        piece.setFirstMove(firstMove);
                                        return false;
                                    }
                                } catch (ArrayIndexOutOfBoundsException e) {
                                }
                            }
                        } else if (piece.getPieceName().equals("King")) {
                            int kingMoves[][] = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}, {1, 1}, {1, -1},
                                    {-1, 1}, {-1, -1}, {0, -2}, {0, 2}};
                            for (int k = 0; k < kingMoves.length; k++) {
                                int[] destination = {i + kingMoves[k][0], j + kingMoves[k][1]};
                                try {
                                    Tile target = board[destination[0]][destination[1]];
                                    if (!target.getTeam().equals("white")
                                            && validateMove(origin, destination, "", true)) {
                                        if (k == 8 || k == 9) {
                                            reverseCastle(destination, "white");
                                        } else {
                                            reverseMove(origin, destination, piece, target);
                                        }
                                        piece.setFirstMove(firstMove);
                                        return false;
                                    }
                                } catch (ArrayIndexOutOfBoundsException e) {
                                }
                            }
                        } else {
                            for (int k = 0; k < moves.length; k++) {
                                int[] destination = {i + moves[k][0], j + moves[k][1]};
                                try {
                                    Tile target = board[destination[0]][destination[1]];
                                    if (!target.getTeam().equals("white")
                                            && validateMove(origin, destination, "", true)) {
                                        reverseMove(origin, destination, piece, target);
                                        piece.setFirstMove(firstMove);
                                        return false;
                                    }
                                } catch (ArrayIndexOutOfBoundsException e) {
                                }
                            }
                        }
                    }
                }
            }
        } else if (team.equals("black")) {
            for (int i = 0; i < board.length; i++) {
                for (int j = 0; j < board[i].length; j++) {
                    Tile piece = board[i][j];
                    boolean firstMove = piece.getFirstMove();
                    if (piece.getTeam().equals("black")) {
                        int[] origin = {i, j};
                        int[][] moves = piece.getMoves();
                        if (piece.getPieceName().equals("Pawn")) {
                            int pawnMoves[][] = {{1, 0}, {1, 1}, {1, -1}, {2, 0}};
                            for (int k = 0; k < pawnMoves.length; k++) {
                                int[] destination = {i + pawnMoves[k][0], j + pawnMoves[k][1]};
                                try {
                                    Tile target = board[destination[0]][destination[1]];
                                    if (!target.getTeam().equals("black")
                                            && validateMove(origin, destination, "", false)) {
                                        reverseMove(origin, destination, piece, target);
                                        piece.setFirstMove(firstMove);
                                        return false;
                                    }
                                } catch (ArrayIndexOutOfBoundsException e) {
                                }
                            }
                        } else if (piece.getPieceName().equals("King")) {
                            int kingMoves[][] = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}, {1, 1}, {1, -1},
                                    {-1, 1}, {-1, 1}, {0, -2}, {0, 2}};
                            for (int k = 0; k < kingMoves.length; k++) {
                                int[] destination = {i + kingMoves[k][0], j + kingMoves[k][1]};
                                try {
                                    Tile target = board[destination[0]][destination[1]];
                                    if (!target.getTeam().equals("black")
                                            && validateMove(origin, destination, "", false)) {
                                        if (k == 8 || k == 9) {
                                            reverseCastle(destination, "white");
                                        } else {
                                            reverseMove(origin, destination, piece, target);
                                        }
                                        piece.setFirstMove(firstMove);
                                        return false;
                                    }
                                } catch (ArrayIndexOutOfBoundsException e) {
                                }
                            }
                        } else {
                            for (int k = 0; k < moves.length; k++) {
                                int[] destination = {i + moves[k][0], j + moves[k][1]};
                                try {
                                    Tile target = board[destination[0]][destination[1]];
                                    if (!target.getTeam().equals("black")
                                            && validateMove(origin, destination, "", false)) {
                                        reverseMove(origin, destination, piece, target);
                                        piece.setFirstMove(firstMove);
                                        return false;
                                    }
                                } catch (ArrayIndexOutOfBoundsException e) {
                                }
                            }
                        }
                    }
                }
            }
        }
        return true;
    }

    private int[] getIndex(String input) {
        if (input.length() != 2)
            return null;
        int[] index = new int[2];

        index[0] = 8 - (input.charAt(1) - '0');
        index[1] = input.charAt(0) - '0' - 49;

        return index;
    }

    private void initBoard() {
        board[0][0] = new Rook("bR");
        board[0][1] = new Knight("bN");
        board[0][2] = new Bishop("bB");
        board[0][3] = new Queen("bQ");
        board[0][4] = new King("bK");
        board[0][5] = new Bishop("bB");
        board[0][6] = new Knight("bN");
        board[0][7] = new Rook("bR");
        board[1][0] = new Pawn("bp");
        board[1][1] = new Pawn("bp");
        board[1][2] = new Pawn("bp");
        board[1][3] = new Pawn("bp");
        board[1][4] = new Pawn("bp");
        board[1][5] = new Pawn("bp");
        board[1][6] = new Pawn("bp");
        board[1][7] = new Pawn("bp");

        board[6][0] = new Pawn("wp");
        board[6][1] = new Pawn("wp");
        board[6][2] = new Pawn("wp");
        board[6][3] = new Pawn("wp");
        board[6][4] = new Pawn("wp");
        board[6][5] = new Pawn("wp");
        board[6][6] = new Pawn("wp");
        board[6][7] = new Pawn("wp");
        board[7][0] = new Rook("wR");
        board[7][1] = new Knight("wN");
        board[7][2] = new Bishop("wB");
        board[7][3] = new Queen("wQ");
        board[7][4] = new King("wK");
        board[7][5] = new Bishop("wB");
        board[7][6] = new Knight("wN");
        board[7][7] = new Rook("wR");

        for (int i = 2; i < 6; i++) {
            for (int j = 0; j < 8; j++) {
                board[i][j] = new Tile();
            }
        }
    }
}
