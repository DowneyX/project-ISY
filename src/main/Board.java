package src.main;

public class Board {
    private char[][] grid;

    Board(int x, int y) {
        this.grid = new char[x][y];
    }

    public void set_element(int x, int y, char value) {
        this.grid[x][y] = value;
    }

    public char get_element(int x, int y) {
        return grid[x][y];
    }

    public String toString() {
        String string = "";
        for (char[] row : grid) {
            for (char col : row) {
                // note that \u0000 is the default value that a char array is populated with.
                // this is essentially an "empty" value
                if (col == '\u0000') {
                    string += "| |";
                } else {
                    string += "|" + col + "|";
                }
            }
            string += "\n";
        }
        return string;
    }

    public char[][] get_grid() {
        return grid;
    }

    public boolean isFull() {
        for (char[] row : grid) {
            for (char col : row) {
                // note that \u0000 is the default value that a char array is populated with.
                // this is essentially an "empty" value
                if (col == '\u0000') {
                    return false;
                }
            }
        }
        return true;
    }
}
