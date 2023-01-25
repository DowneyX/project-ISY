package isy.team4.projectisy.model.player;

import isy.team4.projectisy.model.rule.IRuleSet;
import isy.team4.projectisy.util.Board;
import isy.team4.projectisy.util.Vector2D;

public class AIPlayer implements IPlayer {
    private String name;
    private char initial;
    private IPlayer opponent;
    private IRuleSet ruleSet;
    private int maxDepth = 2; // max depth for minimax

    public AIPlayer(String name) {
        this.name = name;
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

    public void setOpponent(IPlayer opponent) {  // TODO: Could later be opponents
        this.opponent = opponent;
    }

    public void setRuleSet(IRuleSet ruleSet) {
        this.ruleSet = ruleSet.clone();
    }

    public String toString() {
        return this.getName();
    }

    @Override
    public Vector2D getMove(Board board) {
        ruleSet.setBoard(board);

        int bestVal = Integer.MIN_VALUE;
        Vector2D BestMove = new Vector2D(-1, -1);

        long startTime = System.nanoTime();
        System.out.println("Starting to find move");

        for (Vector2D move : ruleSet.getValidMoves(this)) {
            Board newBoard = new Board(board);
            ruleSet.setBoard(newBoard);
            ruleSet.handleMove(move, this);

            long start = System.nanoTime();
            System.out.print("MINIMAX START ");

            int moveVal = this.alphaBeta(newBoard, 0, Integer.MIN_VALUE, Integer.MAX_VALUE, false);

            long elapsedTime = System.nanoTime() - start;

            System.out.print(" DONE " + elapsedTime / 1000000 + "ms");
            System.out.println();
            ruleSet.setBoard(board);

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

    @Override
    public void setName(String name) {
        this.name = name;
    }

    private int alphaBeta(Board node, int depth, double alpha, double beta, boolean isMaxPlayer) {
        ruleSet.setBoard(node);

        if (ruleSet.isWon()) {
            if (ruleSet.getWinningPlayer() == opponent) {
                return Integer.MIN_VALUE + depth;
            }

            return Integer.MAX_VALUE - depth;
        } else if (ruleSet.isDraw()) {
            return 0;
        } else if (depth >= maxDepth) {
            return ruleSet.getScore(this);
        }

        if (isMaxPlayer) {
            int value = Integer.MIN_VALUE;

            for (Vector2D move : ruleSet.getValidMoves(this)) {

                Board child = new Board(node);
                ruleSet.setBoard(child);
                ruleSet.handleMove(move, this);

                boolean nextIsMax = ruleSet.isPass(opponent);
                value = Math.max(value, alphaBeta(child, depth + 1, alpha, beta, nextIsMax));
                alpha = Math.max(alpha, value);
                ruleSet.setBoard(node);
                if (value >= beta) {
                    break;
                }
            }
            return value;
        } else {
            int value = Integer.MAX_VALUE;

            for (Vector2D move : ruleSet.getValidMoves(opponent)) {


                Board child = new Board(node);
                ruleSet.setBoard(child);
                ruleSet.handleMove(move, opponent);

                boolean nextIsMax = !ruleSet.isPass(this);
                value = Math.min(value, alphaBeta(child, depth + 1, alpha, beta, nextIsMax));
                beta = Math.min(beta, value);
                ruleSet.setBoard(node);
                if (value <= alpha) {
                    break;
                }
            }

            return value;
        }
    }
}
