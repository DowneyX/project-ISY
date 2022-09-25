package src.main;

import java.util.Scanner;

public class TicTacToe {
    private Board board = new Board(3, 3);
    private char turn = 'X';
    private Scanner scanner = new Scanner(System.in);
    private boolean isRunning = false;

    public void start_game() {
        isRunning = true;

        while (isRunning) {
            System.out.println(board.toString());
            System.out.println("het is speler [" + turn + "] z'n beurt");
            player_move();
            if (has_won(turn)) {
                System.out.println(board.toString());
                System.out.println("[" + turn + "] heeft gewonnen");
                break;
            }
            increment_turn();
        }
    }

    private void player_move() {
        String[] move;
        System.out.println("typ te positie van je zet als vogt 'x y'");
        move = scanner.nextLine().split(" ");
        if (!is_valid_move(Integer.parseInt(move[0]), Integer.parseInt(move[1]))) {
            System.out.println("dat is geen geldige zet");
            player_move();
        } else {
            board.set_element(Integer.parseInt(move[0]), Integer.parseInt(move[1]), turn);
        }

    }

    private void increment_turn() {
        if (turn == 'X') {
            turn = 'O';
        } else {
            turn = 'X';
        }
    }

    private boolean is_valid_move(int x, int y) {
        char[][] grid = board.get_grid();
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

    private boolean has_won(char ch) {
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
        char[][] grid = board.get_grid();
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
        char[][] grid = board.get_grid();
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
        char[][] grid = board.get_grid();
        for (int i = 0; i < 3; i++) {
            if (grid[i][i] != ch) {
                return false;
            }
        }
        return true;
    }

    private boolean has_3row_anti_diagonal(char ch) {
        char[][] grid = board.get_grid();
        for (int i = 2; i >= 0; i--) {
            if (grid[i][i] != ch) {
                return false;
            }
        }
        return true;
    }
}
