package isy.team4.projectisy.util;

public class Board {
    private char[][] grid;

    public Board(int x, int y) {
        this.grid = new char[x][y];
    }

    public void setElement(int x, int y, char value) {
        this.grid[x][y] = value;
    }

    public char getElement(int x, int y) {
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

    public char[][] getGrid() {
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
