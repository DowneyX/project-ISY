package isy.team4.projectisy.model.player;

import isy.team4.projectisy.util.Board;
import isy.team4.projectisy.util.Vector2D;

public class AIPlayer implements IPlayer {
    private final String name;
    private char initial;
    private int[][] possibleMoves = {
            { 1, 1 }, { 0, 0 }, { 0, 2 }, { 2, 0 }, { 2, 2 },
            { 0, 1 }, { 1, 0 }, { 1, 2 }, { 2, 1 } };

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
    public Vector2D getMove(Board board, IPlayer opponent) {
        // TODO: AI Move

        Vector2D BestMove = new Vector2D(0, 0);
        int bestVal = Integer.MIN_VALUE;
        Board newBoard = board.copyBoard();

        for (int[] move : possibleMoves) {
            if (newBoard.getData()[move[0]][move[1]] == null) {
                newBoard.setElement(this, move[0], move[1]);
                int moveVal = Math.max(bestVal, miniMax(newBoard, 0, false, opponent));
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

    private int miniMax(Board board, int depth, boolean isCurrentPlayer, IPlayer opponent) {

        int score = 0;
        Board newBoard = board.copyBoard();

        if (!hasWon(board) || board.isFull() || !hasLost(board)) { // terminal state of the board
            score += isCurrentPlayer && hasWon(newBoard) ? 10 : 0;
            score -= !isCurrentPlayer && hasWon(newBoard) ? 10 : 0;
            return score;
        }

        if (isCurrentPlayer) { // if we are max player
            int bestVal = Integer.MIN_VALUE;
            for (int[] move : possibleMoves) {
                if (newBoard.getData()[move[0]][move[1]] == null) {
                    newBoard.setElement(this, move[0], move[1]);
                    bestVal = Math.max(bestVal, miniMax(newBoard, depth + 1, !isCurrentPlayer, opponent));
                    newBoard.setElement(null, move[0], move[1]);
                }
            }
            return bestVal;

        } else { // if we are min player
            int bestVal = Integer.MAX_VALUE;
            for (int[] move : possibleMoves) {
                if (newBoard.getData()[move[0]][move[1]] == null) {
                    newBoard.setElement(opponent, move[0], move[1]);
                    bestVal = Math.min(bestVal, miniMax(newBoard, depth + 1, !isCurrentPlayer, opponent));
                    newBoard.setElement(null, move[0], move[1]);

                }
            }
            return bestVal;
        }
    }

    private boolean hasWon(Board board) {
        IPlayer[][] grid = board.getData();

        return grid[0][0] == grid[0][1] && grid[0][1] == grid[0][2] && grid[0][0] == this ||
                grid[1][0] == grid[1][1] && grid[1][1] == grid[1][2] && grid[1][0] == this ||
                grid[2][0] == grid[2][1] && grid[2][1] == grid[2][2] && grid[2][0] == this ||
                grid[0][0] == grid[1][0] && grid[1][0] == grid[2][0] && grid[0][0] == this ||
                grid[0][1] == grid[1][1] && grid[1][1] == grid[2][1] && grid[0][1] == this ||
                grid[0][2] == grid[1][2] && grid[1][2] == grid[2][2] && grid[0][2] == this ||
                grid[0][0] == grid[1][1] && grid[1][1] == grid[2][2] && grid[0][0] == this ||
                grid[0][2] == grid[1][1] && grid[1][1] == grid[2][0] && grid[0][2] == this;
    }

    private boolean hasLost(Board board) {
        IPlayer[][] grid = board.getData();

        return grid[0][0] == grid[0][1] && grid[0][1] == grid[0][2] && grid[0][0] != this && grid[0][0] != null ||
                grid[1][0] == grid[1][1] && grid[1][1] == grid[1][2] && grid[1][0] != this && grid[0][0] != null ||
                grid[2][0] == grid[2][1] && grid[2][1] == grid[2][2] && grid[2][0] != this && grid[0][0] != null ||
                grid[0][0] == grid[1][0] && grid[1][0] == grid[2][0] && grid[0][0] != this && grid[0][0] != null ||
                grid[0][1] == grid[1][1] && grid[1][1] == grid[2][1] && grid[0][1] != this && grid[0][0] != null ||
                grid[0][2] == grid[1][2] && grid[1][2] == grid[2][2] && grid[0][2] != this && grid[0][0] != null ||
                grid[0][0] == grid[1][1] && grid[1][1] == grid[2][2] && grid[0][0] != this && grid[0][0] != null ||
                grid[0][2] == grid[1][1] && grid[1][1] == grid[2][0] && grid[0][2] != this && grid[0][0] != null;
    }
}
