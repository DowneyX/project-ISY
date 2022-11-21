package isy.team4.projectisy.model.player;

import isy.team4.projectisy.util.Board;
import isy.team4.projectisy.util.Vector2D;

public class AIPlayer implements IPlayer {
    private final String name;
    private char initial;
    private IPlayer opponent;
    private int[][] possibleMoves = { // TODO: this works only for tictactoe. should get legal moves from ruleset
            { 0, 0 }, { 0, 1 }, { 0, 2 },
            { 1, 0 }, { 1, 1 }, { 1, 2 },
            { 2, 0 }, { 2, 1 }, { 2, 2 }, };

    public AIPlayer(String name, IPlayer opponent) {
        this.opponent = opponent;
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

        for (int[] move : possibleMoves) {
            if (newBoard.getElement(move[0], move[1]) == null) {
                newBoard.setElement(this, move[0], move[1]);
                int moveVal = miniMax(newBoard, 0, false);
                newBoard.setElement(null, move[0], move[1]);

                if (moveVal > bestVal) {
                    bestVal = moveVal;
                    BestMove = new Vector2D(move[0], move[1]);
                }
            }
        }

        return BestMove;

    }

    @Override
    public void setInitial(char initial) {
        this.initial = initial;
    }

    private int miniMax(Board board, int depth, boolean isMax) {

        if (hasWon(board, this) || hasWon(board, opponent) || board.isFull()) { // terminal state of the board
            int score = 0;
            if (hasWon(board, opponent)) {
                score = Integer.MIN_VALUE + depth;
            }
            if (hasWon(board, this)) {
                score = Integer.MAX_VALUE - depth;
            }
            return score;
        }

        if (isMax) { // if we are max player
            int bestVal = Integer.MIN_VALUE;
            for (int[] move : possibleMoves) {
                if (board.getElement(move[0], move[1]) == null) {
                    board.setElement(this, move[0], move[1]);
                    bestVal = Math.max(bestVal, miniMax(board, depth + 1, false));
                    board.setElement(null, move[0], move[1]);
                }
            }
            return bestVal;

        } else { // if we are min player
            int bestVal = Integer.MAX_VALUE;
            for (int[] move : possibleMoves) {
                if (board.getElement(move[0], move[1]) == null) {
                    board.setElement(opponent, move[0], move[1]);
                    bestVal = Math.min(bestVal, miniMax(board, depth + 1, true));
                    board.setElement(null, move[0], move[1]);
                }
            }
            return bestVal;
        }
    }

    private boolean hasWon(Board board, IPlayer player) {
        IPlayer[][] grid = board.getData();
        return grid[0][0] == grid[0][1] && grid[0][1] == grid[0][2] && grid[0][0] == player ||
                grid[1][0] == grid[1][1] && grid[1][1] == grid[1][2] && grid[1][0] == player ||
                grid[2][0] == grid[2][1] && grid[2][1] == grid[2][2] && grid[2][0] == player ||
                grid[0][0] == grid[1][0] && grid[1][0] == grid[2][0] && grid[0][0] == player ||
                grid[0][1] == grid[1][1] && grid[1][1] == grid[2][1] && grid[0][1] == player ||
                grid[0][2] == grid[1][2] && grid[1][2] == grid[2][2] && grid[0][2] == player ||
                grid[0][0] == grid[1][1] && grid[1][1] == grid[2][2] && grid[0][0] == player ||
                grid[0][2] == grid[1][1] && grid[1][1] == grid[2][0] && grid[0][2] == player;
    }
}
