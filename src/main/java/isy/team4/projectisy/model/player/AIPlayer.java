package isy.team4.projectisy.model.player;

import isy.team4.projectisy.util.Board;
import isy.team4.projectisy.util.Vector2D;

public class AIPlayer implements IPlayer {
    private final String name;
    private String initial;

    public AIPlayer(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String getInitial() {
        return this.initial;
    }

    @Override
    public Vector2D getMove(Board board) {
        // TODO: AI Move
        // welcome to if hell
        // if there is a winning move play winning move
        String opInitial = initial.equals("X") ? "O" : "X";

        // horrizontal / vertical
        for (int i = 0; i < 3; i++) {
            if (board.getElement(i, 0).equals(initial) && board.getElement(i, 1).equals(initial)
                    && board.getElement(i, 2) == null) {
                return new Vector2D(i, 2);
            }

            if (board.getElement(i, 0).equals(initial) && board.getElement(i, 1) == null
                    && board.getElement(i, 2).equals(initial)) {
                return new Vector2D(i, 1);
            }

            if (board.getElement(i, 0) == null && board.getElement(i, 1).equals(initial)
                    && board.getElement(i, 2).equals(initial)) {
                return new Vector2D(i, 0);
            }

            if (board.getElement(0, i) == null && board.getElement(1, i).equals(initial)
                    && board.getElement(2, i).equals(initial)) {
                return new Vector2D(0, i);
            }

            if (board.getElement(0, i).equals(initial) && board.getElement(1, i) == null
                    && board.getElement(2, i).equals(initial)) {
                return new Vector2D(1, i);
            }

            if (board.getElement(0, i).equals(initial) && board.getElement(1, i).equals(initial)
                    && board.getElement(2, i) == null) {
                return new Vector2D(2, i);
            }

        }

        // diagonal
        if (board.getElement(0, 0) == null && board.getElement(1, 1).equals(initial)
                && board.getElement(2, 2).equals(initial)) {
            return new Vector2D(0, 0);
        }

        if (board.getElement(0, 0).equals(initial) && board.getElement(1, 1) == null
                && board.getElement(2, 2).equals(initial)) {
            return new Vector2D(1, 1);
        }

        if (board.getElement(0, 0).equals(initial) && board.getElement(1, 1).equals(initial)
                && board.getElement(2, 2) == null) {
            return new Vector2D(2, 2);
        }

        // anti diagonal

        if (board.getElement(0, 2) == null && board.getElement(1, 1).equals(initial)
                && board.getElement(2, 0).equals(initial)) {
            return new Vector2D(0, 2);
        }

        if (board.getElement(0, 2).equals(initial) && board.getElement(1, 1) == null
                && board.getElement(2, 0).equals(initial)) {
            return new Vector2D(1, 1);
        }

        if (board.getElement(0, 2).equals(initial) && board.getElement(1, 1).equals(initial)
                && board.getElement(2, 0) == null) {
            return new Vector2D(2, 0);
        }

        // if there is a move that should be blocked block it

        // horrizontal / vertical
        for (int i = 0; i < 3; i++) {
            if (board.getElement(i, 0).equals(initial) && board.getElement(i, 1).equals(initial)
                    && board.getElement(i, 2) == null) {
                return new Vector2D(i, 2);
            }

            if (board.getElement(i, 0).equals(initial) && board.getElement(i, 1) == null
                    && board.getElement(i, 2).equals(initial)) {
                return new Vector2D(i, 1);
            }

            if (board.getElement(i, 0) == null && board.getElement(i, 1).equals(initial)
                    && board.getElement(i, 2).equals(initial)) {
                return new Vector2D(i, 0);
            }

            if (board.getElement(0, i) == null && board.getElement(1, i).equals(initial)
                    && board.getElement(2, i).equals(initial)) {
                return new Vector2D(0, i);
            }

            if (board.getElement(0, i).equals(initial) && board.getElement(1, i) == null
                    && board.getElement(2, i).equals(initial)) {
                return new Vector2D(1, i);
            }

            if (board.getElement(0, i).equals(initial) && board.getElement(1, i).equals(initial)
                    && board.getElement(2, i) == null) {
                return new Vector2D(2, i);
            }

        }

        // diagonal
        if (board.getElement(0, 0) == null && board.getElement(1, 1).equals(opInitial)
                && board.getElement(2, 2).equals(opInitial)) {
            return new Vector2D(0, 0);
        }

        if (board.getElement(0, 0).equals(opInitial) && board.getElement(1, 1) == null
                && board.getElement(2, 2).equals(opInitial)) {
            return new Vector2D(1, 1);
        }

        if (board.getElement(0, 0).equals(opInitial) && board.getElement(1, 1).equals(opInitial)
                && board.getElement(2, 2) == null) {
            return new Vector2D(2, 2);
        }

        // anti diagonal

        if (board.getElement(0, 2) == null && board.getElement(1, 1).equals(opInitial)
                && board.getElement(2, 0).equals(opInitial)) {
            return new Vector2D(0, 2);
        }

        if (board.getElement(0, 2).equals(opInitial) && board.getElement(1, 1) == null
                && board.getElement(2, 0).equals(opInitial)) {
            return new Vector2D(1, 1);
        }

        if (board.getElement(0, 2).equals(opInitial) && board.getElement(1, 1).equals(opInitial)
                && board.getElement(2, 0) == null) {
            return new Vector2D(2, 0);
        }

        // do a move

        int[][] possibleMoves = {
                { 1, 1 }, { 0, 0 }, { 0, 2 }, { 2, 0 }, { 2, 2 },
                { 0, 1 }, { 1, 0 }, { 1, 2 }, { 2, 1 } };

        for (int[] move : possibleMoves) {
            if (board.getElement(move[0], move[1]) == null) {
                return new Vector2D(move[0], move[0]);
            }
        }

        return new Vector2D(0, 0);
    }

    @Override
    public void setInitial(String initial) {
        this.initial = initial;
    }
}
