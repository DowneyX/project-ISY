package isy.team4.projectisy.game;

import isy.team4.projectisy.player.Player;
import isy.team4.projectisy.util.Board;

public class TictactoeManager {
    private Board board = new Board(3, 3);
    private char turn = 'X';
    private Player player1;
    private Player player2;

    public TictactoeManager(Player player1, Player player2) {
        this.player1 = player1;
        this.player2 = player2;
    }

    public void startGame() {
        while (true) {
            System.out.println(board.toString());
            System.out.println("it's [" + getPlayerWithTurn().getName() + "] their turn");
            playerMove();
            if (hasWon(turn)) {
                System.out.println(board.toString());
                System.out.println("[" + getPlayerWithTurn().getName() + "] won!");
                break;
            }
            incrementTurn();
        }
    }

    public Player getPlayerWithTurn() {
        if (player1.getTurn() == turn) {
            return player1;
        } else {
            return player2;
        }
    }

    public void instertMove(int[] move) {
        board.setElement(move[0], move[1], turn);
    }

    public void playerMove() {
        int[] move = getPlayerWithTurn().getMove();

        if (!isValidMove(move[0], move[1])) {
            System.out.println("invallid move.");
            playerMove();
        } else {
            instertMove(move);
        }

    }

    public Board getBoard() {
        return board;
    }

    public char getTurn() {
        return turn;
    }

    public void incrementTurn() {
        if (turn == 'X') {
            turn = 'O';
        } else {
            turn = 'X';
        }
    }

    public boolean isValidMove(int x, int y) {
        char[][] grid = board.getGrid();
        if (!(grid.length > x)) {
            return false;
        }
        if (!(grid[0].length > y)) {
            return false;
        }
        if (grid[x][y] != '\u0000') {
            return false;
        }
        return true;
    }

    public boolean hasWon(char ch) {
        // check rows
        if (has_3row_row(ch)) {
            return true;
        }

        // check cols
        if (has_3row_col(ch)) {
            return true;
        }
        // check diagonal
        if (has_3row_diagonal(ch)) {
            return true;
        }
        // check antidiagonal
        if (has_3row_anti_diagonal(ch)) {
            return true;
        }

        return false;
    }

    private boolean has_3row_row(char ch) {
        char[][] grid = board.getGrid();
        int count = 0;
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                if (grid[row][col] != ch) {
                    break;
                } else {
                    count++;
                }
            }
            if (count > 2) {
                return true;
            }
            count = 0;
        }
        return false;
    }

    private boolean has_3row_col(char ch) {
        char[][] grid = board.getGrid();
        int count = 0;

        for (int col = 0; col < 3; col++) {
            for (int row = 0; row < 3; row++) {
                if (grid[row][col] != ch) {
                    break;
                } else {
                    count++;
                }
            }
            if (count > 2) {
                return true;
            }
            count = 0;
        }
        return false;
    }

    private boolean has_3row_diagonal(char ch) {
        char[][] grid = board.getGrid();
        for (int i = 0; i < 3; i++) {
            if (grid[i][i] != ch) {
                return false;
            }
        }
        return true;
    }

    private boolean has_3row_anti_diagonal(char ch) {
        char[][] grid = board.getGrid();
        for (int i = 2; i >= 0; i--) {
            if (grid[i][i] != ch) {
                return false;
            }
        }
        return true;
    }
}
