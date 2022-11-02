package isy.team4.projectisy.player;

import java.util.Random;

public class PlayerSmartTictactoe extends Player {

    Random rand = new Random();

    public PlayerSmartTictactoe(String name, char turn) {
        super(name, turn);
    }

    public int[] getMove() {
        char antiTurn = turn == 'X' ? 'O' : 'X';

        // horrizontal / vertical
        for (int i = 0; i < 3; i++) {
            if (board.getElement(i, 0) == turn && board.getElement(i, 1) == turn
                    && board.getElement(i, 2) == '\u0000') {
                int[] move = { i, 2 };
                return move;
            }

            if (board.getElement(i, 0) == turn && board.getElement(i, 1) == '\u0000'
                    && board.getElement(i, 2) == turn) {
                int[] move = { i, 1 };
                return move;
            }

            if (board.getElement(i, 0) == '\u0000' && board.getElement(i, 1) == turn
                    && board.getElement(i, 2) == turn) {
                int[] move = { i, 0 };
                return move;
            }

            if (board.getElement(0, i) == '\u0000' && board.getElement(1, i) == turn
                    && board.getElement(2, i) == turn) {
                int[] move = { 0, i };
                return move;
            }

            if (board.getElement(0, i) == turn && board.getElement(1, i) == '\u0000'
                    && board.getElement(2, i) == turn) {
                int[] move = { 1, i };
                return move;
            }

            if (board.getElement(0, i) == turn && board.getElement(1, i) == turn
                    && board.getElement(2, i) == '\u0000') {
                int[] move = { 2, i };
                return move;
            }

        }

        // diagonal
        if (board.getElement(0, 0) == '\u0000' && board.getElement(1, 1) == turn
                && board.getElement(2, 2) == turn) {
            int[] move = { 0, 0 };
            return move;
        }

        if (board.getElement(0, 0) == turn && board.getElement(1, 1) == '\u0000'
                && board.getElement(2, 2) == turn) {
            int[] move = { 1, 1 };
            return move;
        }

        if (board.getElement(0, 0) == turn && board.getElement(1, 1) == turn
                && board.getElement(2, 2) == '\u0000') {
            int[] move = { 2, 2 };
            return move;
        }

        // anti diagonal

        if (board.getElement(0, 2) == '\u0000' && board.getElement(1, 1) == turn
                && board.getElement(2, 0) == turn) {
            int[] move = { 0, 2 };
            return move;
        }

        if (board.getElement(0, 2) == turn && board.getElement(1, 1) == '\u0000'
                && board.getElement(2, 0) == turn) {
            int[] move = { 1, 1 };
            return move;
        }

        if (board.getElement(0, 2) == turn && board.getElement(1, 1) == turn
                && board.getElement(2, 0) == '\u0000') {
            int[] move = { 2, 0 };
            return move;
        }

        // if there is a move that should be blocked block it

        // horrizontal / vertical
        for (int i = 0; i < 3; i++) {
            if (board.getElement(i, 0) == antiTurn && board.getElement(i, 1) == antiTurn
                    && board.getElement(i, 2) == '\u0000') {
                int[] move = { 1, 2 };
                return move;
            }

            if (board.getElement(i, 0) == antiTurn && board.getElement(i, 1) == '\u0000'
                    && board.getElement(i, 2) == antiTurn) {
                int[] move = { i, 1 };
                return move;
            }

            if (board.getElement(i, 0) == '\u0000' && board.getElement(i, 1) == antiTurn
                    && board.getElement(i, 2) == antiTurn) {
                int[] move = { i, 0 };
                return move;
            }

            if (board.getElement(0, i) == '\u0000' && board.getElement(1, i) == antiTurn
                    && board.getElement(2, i) == antiTurn) {
                int[] move = { 0, i };
                return move;
            }

            if (board.getElement(0, i) == antiTurn && board.getElement(1, i) == '\u0000'
                    && board.getElement(2, i) == antiTurn) {
                int[] move = { 1, i };
                return move;
            }

            if (board.getElement(0, i) == antiTurn && board.getElement(1, i) == antiTurn
                    && board.getElement(2, i) == '\u0000') {
                int[] move = { 2, i };
                return move;
            }

        }

        // diagonal
        if (board.getElement(0, 0) == '\u0000' && board.getElement(1, 1) == antiTurn
                && board.getElement(2, 2) == antiTurn) {
            int[] move = { 0, 0 };
            return move;
        }

        if (board.getElement(0, 0) == antiTurn && board.getElement(1, 1) == '\u0000'
                && board.getElement(2, 2) == antiTurn) {
            int[] move = { 1, 1 };
            return move;
        }

        if (board.getElement(0, 0) == antiTurn && board.getElement(1, 1) == antiTurn
                && board.getElement(2, 2) == '\u0000') {
            int[] move = { 2, 2 };
            return move;
        }

        // anti diagonal

        if (board.getElement(0, 2) == '\u0000' && board.getElement(1, 1) == antiTurn
                && board.getElement(2, 0) == antiTurn) {
            int[] move = { 0, 2 };
            return move;
        }

        if (board.getElement(0, 2) == antiTurn && board.getElement(1, 1) == '\u0000'
                && board.getElement(2, 0) == antiTurn) {
            int[] move = { 1, 1 };
            return move;
        }

        if (board.getElement(0, 2) == antiTurn && board.getElement(1, 1) == antiTurn
                && board.getElement(2, 0) == '\u0000') {
            int[] move = { 2, 0 };
            return move;
        }

        // do a move

        int[][] possibleMoves = {
                { 1, 1 }, { 0, 0 }, { 0, 2 }, { 2, 0 }, { 2, 2 },
                { 0, 1 }, { 1, 0 }, { 1, 2 }, { 2, 1 } };

        for (int[] move : possibleMoves) {
            if (board.getElement(move[0], move[1]) == '\u0000') {
                return move;
            }
        }
        int[] move = { 2, 2 };
        return move;
    }
}
