package isy.team4.projectisy.model.player;

import isy.team4.projectisy.model.rule.IRuleSet;
import isy.team4.projectisy.util.Board;
import isy.team4.projectisy.util.Vector2D;

public class AIPlayer implements IPlayer {
    private final String name;
    private char initial;
    private IPlayer opponent;
    private IRuleSet ruleset;

    public AIPlayer(String name, IPlayer opponent, IRuleSet ruleset) {
        this.opponent = opponent;
        this.name = name;
        this.ruleset = ruleset.clone();
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public char getInitial() {
        return this.initial;
    }

    public String toString() {
        if (this.name != null) {
            return this.name;
        }
        return "AIplayer";
    }

    @Override
    public Vector2D getMove(Board board) {
        int bestVal = Integer.MIN_VALUE;
        Board newBoard = new Board(board);
        Vector2D BestMove = new Vector2D(-1, -1);
        ruleset.setBoard(newBoard);
        ; // initialise the ruleset with board
        Vector2D[] validmoves = ruleset.getValidMoves(this);

        for (Vector2D move : validmoves) {
            newBoard.setElement(this, move.x, move.y);
            // ruleset.handleMove(newBoard, opponent);
            int moveVal = miniMax(newBoard, 0, false);
            newBoard.setElement(null, move.x, move.y);

            if (moveVal > bestVal) {
                bestVal = moveVal;
                BestMove = new Vector2D(move.x, move.y);
            }
        }

        return BestMove;

    }

    @Override
    public void setInitial(char initial) {
        this.initial = initial;
    }

    private int miniMax(Board board, int depth, boolean isMax) {
        Vector2D[] validmoves = ruleset.getValidMoves(this);

        if (ruleset.isDraw() || ruleset.isWon() || board.isFull()) { // terminal state of the board
            int score = 0;
            if (ruleset.isWon() && ruleset.getWinningPlayer() == opponent) {
                score = Integer.MIN_VALUE + depth;
            }
            if (ruleset.isWon() && ruleset.getWinningPlayer() == this) {
                score = Integer.MAX_VALUE - depth;
            }
            return score;
        }

        if (isMax) { // if we are max player
            int bestVal = Integer.MIN_VALUE;
            for (Vector2D move : ruleset.getValidMoves(this)) {
                board.setElement(this, move.x, move.y);
                bestVal = Math.max(bestVal, miniMax(board, depth + 1, false));
                board.setElement(null, move.x, move.y);

            }
            return bestVal;

        } else { // if we are min player
            int bestVal = Integer.MAX_VALUE;
            for (Vector2D move : ruleset.getValidMoves(this)) {
                board.setElement(opponent, move.x, move.y);
                bestVal = Math.min(bestVal, miniMax(board, depth + 1, true));
                board.setElement(null, move.x, move.y);

            }
            return bestVal;
        }
    }
}
