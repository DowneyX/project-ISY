package isy.team4.projectisy.model.player;

import isy.team4.projectisy.util.Board;
import isy.team4.projectisy.util.Vector2D;

public class AIPlayer implements IPlayer {
    private final String name;
    private char initial;

    public AIPlayer(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public char getInitial() {
        return this.initial;
    }

    @Override
    public Vector2D getMove(Board board) {
        // TODO: AI Move
        // welcome to if hell
        if (initial == 'X') {
            // if there is a winning move play winning move
            for (int i = 0; i < 3; i++) {
                // horizontal
                if (board.getElement(i, 0) == "X" && board.getElement(i, 1) == "X" && board.getElement(i, 2) == null) {
                    return new Vector2D(i, 2);
                }

                if (board.getElement(i, 0) == "X" && board.getElement(i, 1) == null && board.getElement(i, 2) == "X") {
                    return new Vector2D(i, 1);
                }

                if (board.getElement(i, 0) == null && board.getElement(i, 1) == "X" && board.getElement(i, 2) == "X") {
                    return new Vector2D(i, 0);
                }

                // vertical
                if (board.getElement(0, i) == null && board.getElement(1, i) == "X" && board.getElement(2, i) == "X") {
                    return new Vector2D(0, i);
                }

                if (board.getElement(0, i) == "X" && board.getElement(1, i) == null && board.getElement(2, i) == "X") {
                    return new Vector2D(1, i);
                }

                if (board.getElement(0, i) == "X" && board.getElement(1, i) == "X" && board.getElement(2, i) == null) {
                    return new Vector2D(2, i);
                }

            }

            // diagonal
            if (board.getElement(0, 0) == null && board.getElement(1, 1) == "X" && board.getElement(2, 2) == "X") {
                return new Vector2D(0, 0);
            }

            if (board.getElement(0, 0) == "X" && board.getElement(1, 1) == null && board.getElement(2, 2) == "X") {
                return new Vector2D(1, 1);
            }

            if (board.getElement(0, 0) == "X" && board.getElement(1, 1) == "X" && board.getElement(2, 2) == null) {
                return new Vector2D(2, 2);
            }

            // anti diagonal

            if (board.getElement(0, 2) == null && board.getElement(1, 1) == "X" && board.getElement(2, 0) == "X") {
                return new Vector2D(0, 2);
            }

            if (board.getElement(0, 2) == "X" && board.getElement(1, 1) == null && board.getElement(2, 0) == "X") {
                return new Vector2D(1, 1);
            }

            if (board.getElement(0, 2) == "X" && board.getElement(1, 1) == "X" && board.getElement(2, 0) == null) {
                return new Vector2D(2, 0);
            }

            // if there is a move that should be blocked block it

            // if board is empty pick bottom left corner

            // if bottom left has X and middle is not filled (we've won)

            // if bottom left has X and middle is filled (probably a tie)

        }

        return new Vector2D(0, 0);
    }

    @Override
    public void setInitial(char initial) {
        this.initial = initial;
    }
}
