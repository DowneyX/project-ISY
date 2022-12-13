package isy.team4.projectisy.model.player;

import isy.team4.projectisy.model.rule.IRuleSet;
import isy.team4.projectisy.util.Board;
import isy.team4.projectisy.util.Vector2D;

public class AIPlayer implements IPlayer {
    private final String name;
    private char initial;
    private IPlayer opponent;
    private IRuleSet ruleset;
    private int maxDepth = 2; // max depth for minimax

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

        long startTime = System.nanoTime();
        System.out.print("Starting to find move");

        for (Vector2D move : ruleset.getValidMoves(this)) {
            Board newBoard = new Board(board);
            ruleset.setBoard(newBoard);
            ruleset.handleMove(move, this);

            int moveVal = alphabeta(newBoard, 0, Integer.MIN_VALUE, Integer.MAX_VALUE, false);
            ruleset.setBoard(board);
            if (moveVal > bestVal) {
                bestVal = moveVal;
                BestMove = new Vector2D(move.x, move.y);
            }
        }

        long elapsedTime = System.nanoTime() - startTime;

        System.out.print(" DONE " + elapsedTime / 1000000 + "ms");
        System.out.println();

        return BestMove;
    }


    private int alphabeta(Board node, int depth, double alpha, double beta, boolean isMaxPlayer) {
        ruleset.setBoard(node);

        if (ruleset.isWon()) {
            if (ruleset.getWinningPlayer() == opponent) {
                return Integer.MIN_VALUE + depth;
            }

            return Integer.MAX_VALUE - depth;
        } else if (ruleset.isDraw()) {
            return 0;
        } else if (depth >= maxDepth) {
            return ruleset.getScore(this);
        }

        if (isMaxPlayer) {
            int value = Integer.MIN_VALUE;

            for (Vector2D move: ruleset.getValidMoves(this)) {
                Board child = new Board(node);
                ruleset.setBoard(child);
                ruleset.handleMove(move, this);
                boolean nextIsMax = ruleset.isPass(opponent);
                value = Math.max(value, alphabeta(child, depth + 1, alpha, beta, nextIsMax));
                alpha = Math.max(alpha, value);
                ruleset.setBoard(node);
                if (value >= beta) {
                    break;
                }
            }
            return value;
        } else {
            int value = Integer.MAX_VALUE;

            for (Vector2D move: ruleset.getValidMoves(opponent)) {
                Board child = new Board(node);
                ruleset.setBoard(child);
                ruleset.handleMove(move, opponent);
                boolean nextIsMax = ! ruleset.isPass(this);
                value = Math.min(value, alphabeta(child, depth + 1, alpha, beta, nextIsMax));
                beta = Math.min(beta, value);
                ruleset.setBoard(node);
                if (value <= alpha) {
                    break;
                }
            }

            return value;
        }
    }
}
