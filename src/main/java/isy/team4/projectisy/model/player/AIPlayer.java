package isy.team4.projectisy.model.player;

import isy.team4.projectisy.model.rule.IRuleSet;
import isy.team4.projectisy.util.Board;
import isy.team4.projectisy.util.Vector2D;

public class AIPlayer implements IPlayer {
    private String name;
    private final int maxDepth = 3; // max depth for minimax
    private char initial;
    private IPlayer opponent;
    private IRuleSet ruleSet;

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
        this.ruleSet = ruleSet;
    }

    public String toString() {
        return this.getName();
    }

    @Override
    public Vector2D getMove(Board board) {
        ruleSet.setBoard(board);

        int bestVal = Integer.MIN_VALUE;
        Vector2D BestMove = new Vector2D(-1, -1);

        for (Vector2D move : ruleSet.getValidMoves(this)) {
            Board newBoard = new Board(board);
            ruleSet.setBoard(newBoard);
            ruleSet.handleMove(move, this);
            int moveVal = miniMax(newBoard, 0, false);
            ruleSet.setBoard(board);

            if (moveVal > bestVal) {
                bestVal = moveVal;
                BestMove = new Vector2D(move.x, move.y);
            }
        }

        return BestMove;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    private int miniMax(Board board, int depth, boolean isMax) {
        ruleSet.setBoard(board);

        if (ruleSet.isDraw() || ruleSet.isWon() || board.isFull() || depth >= maxDepth) { // terminal state of the board

            int score = 0;
            if (ruleSet.isWon() && ruleSet.getWinningPlayer() == opponent) {
                score = -1000 + depth;
            }
            if (ruleSet.isWon() && ruleSet.getWinningPlayer() == this) {
                score = 1000 - depth;
            }
            return score;
        }

        if (isMax) { // if we are max player
            int bestVal = Integer.MIN_VALUE;
            for (Vector2D move : ruleSet.getValidMoves(this)) {
                Board newBoard = new Board(board);
                ruleSet.setBoard(newBoard);
                ruleSet.handleMove(move, this);
                boolean nextIsMax = ruleSet.isPass(opponent);
                bestVal = Math.max(bestVal, miniMax(newBoard, depth + 1, nextIsMax));
                ruleSet.setBoard(board);
            }
            return bestVal;

        } else { // if we are min player
            int bestVal = Integer.MAX_VALUE;
            for (Vector2D move : ruleSet.getValidMoves(opponent)) {
                Board newBoard = new Board(board);
                ruleSet.setBoard(newBoard);
                ruleSet.handleMove(move, opponent);
                boolean nextIsMax = !ruleSet.isPass(this);
                bestVal = Math.min(bestVal, miniMax(newBoard, depth + 1, nextIsMax));
                ruleSet.setBoard(board);
            }
            return bestVal;
        }
    }
}
