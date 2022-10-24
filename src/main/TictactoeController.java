package src.main;

public class TictactoeController {
    private Board board = new Board(3, 3);
    private char turn = 'X';
    private Player player1;
    private Player player2;

    TictactoeController(Player player1, Player player2) {
        this.player1 = player1;
        this.player2 = player2;
    }

    public void start_game() {
        while (true) {
            System.out.println(board.toString());
            System.out.println("it's [" + getPlayerWithTurn().getName() + "] their turn");
            player_move();
            if (has_won(turn)) {
                System.out.println(board.toString());
                System.out.println("[" + getPlayerWithTurn().getName() + "] won!");
                break;
            }
            increment_turn();
        }
    }

    public Player getPlayerWithTurn() {
        if (player1.getTurn() == turn) {
            return player1;
        } else {
            return player2;
        }
    }

    public void player_move() {
        int[] move = getPlayerWithTurn().getMove();

        if (!is_valid_move(move[0], move[1])) {
            System.out.println("invallid move.");
            player_move();
        } else {
            board.set_element(move[0], move[1], turn);
        }

    }

    public Board getBoard() {
        return board;
    }

    public char getTurn() {
        return turn;
    }

    public void increment_turn() {
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

    public boolean has_won(char ch) {
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
