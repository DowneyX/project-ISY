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
    public void setInitial(char initial) {
        this.initial = initial;
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
        return "x";
    }

    @Override
    public Vector2D getMove(Board board) {
        ruleset.setBoard(board);

        int bestVal = Integer.MIN_VALUE;
        Vector2D BestMove = new Vector2D(-1, -1);

        for (Vector2D move : ruleset.getValidMoves(this)) {
            Board newBoard = new Board(board);
            ruleset.setBoard(newBoard);
            ruleset.handleMove(move, this);
            int moveVal = miniMax(newBoard, 0, false);
            ruleset.setBoard(board);

            if (moveVal > bestVal) {
                bestVal = moveVal;
                BestMove = new Vector2D(move.x, move.y);
            }
        }

        return BestMove;
    }

    private int miniMax(Board board, int depth, boolean isMax) {
        ruleset.setBoard(board);

        if (ruleset.isDraw() || ruleset.isWon() || board.isFull()) { // terminal state of the board

            int score = 0;
            if (ruleset.isWon() && ruleset.getWinningPlayer() == opponent) {
                score = -1000 + depth;
            }
            if (ruleset.isWon() && ruleset.getWinningPlayer() == this) {
                score = 1000 - depth;
            }
            return score;
        }

        if (isMax) { // if we are max player
            int bestVal = Integer.MIN_VALUE;
            for (Vector2D move : ruleset.getValidMoves(this)) {
                Board newBoard = new Board(board);
                ruleset.setBoard(newBoard);
                ruleset.handleMove(move, this);
                bestVal = Math.max(bestVal, miniMax(newBoard, depth + 1, false));
                ruleset.setBoard(board);
            }
            return bestVal;

        } else { // if we are min player
            int bestVal = Integer.MAX_VALUE;
            for (Vector2D move : ruleset.getValidMoves(opponent)) {
                Board newBoard = new Board(board);
                ruleset.setBoard(newBoard);
                ruleset.handleMove(move, opponent);
                bestVal = Math.min(bestVal, miniMax(newBoard, depth + 1, true));
                ruleset.setBoard(board);
            }
            return bestVal;
        }
    }
}
