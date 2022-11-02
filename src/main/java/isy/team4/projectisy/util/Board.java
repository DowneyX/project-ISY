package isy.team4.projectisy.util;

import java.util.Arrays;

public class Board {
    protected int width; // Could be private if no specific boards are needed
    protected int height; // Could be private if no specific boards are needed
    protected String[][] board; // Could be private if no specific boards are needed

    public Board(int width, int height) {
        this.width = width;
        this.height = height;
        this.board = new String[height][width]; // reversed for proper x-y notation
    }

    public void clear() {
        this.board = new String[this.height][this.width];
    }

    /**
     * @throws ArrayIndexOutOfBoundsException when x or y out of bounds
     */
    public void setElement(String value, int x, int y) {
        if (x >= this.width || y >= this.height) {
            throw new ArrayIndexOutOfBoundsException();
        }

        this.board[y][x] = value;
    }

    public String getElement(int x, int y) {
        if (x >= this.width || y >= this.height) {
            throw new ArrayIndexOutOfBoundsException();
        }
        return this.board[y][x];
    }

    /**
     * For debugging purposes
     * 
     * @return The board array displayed as a string with linebreaks
     */
    public String toString() {
        StringBuilder res = new StringBuilder();
        for (String[] row : board) {
            res.append(Arrays.toString(row)).append("\n");
        }
        return res.toString();
    }
}
