package isy.team4.projectisy.util;

import isy.team4.projectisy.model.player.IPlayer;

import java.util.Arrays;

public class Board {
    protected int width;  // Could be private if no specific boards are needed
    protected int height;  // Could be private if no specific boards are needed
    protected IPlayer[][] board;  // Could be private if no specific boards are needed

    public Board(int width, int height) {
        this.width = width;
        this.height = height;
        this.board = new IPlayer[height][width];  // reversed for proper x-y notation
    }

    public IPlayer[][] getData() {
        return this.board;
    }

    public void clear() {
        this.board = new IPlayer[this.height][this.width];
    }

    /**
     * @throws ArrayIndexOutOfBoundsException when x or y out of bounds
     */
    public void setElement(IPlayer player, int x, int y) {
        if (x >= this.width || y >= this.height) {
            throw new ArrayIndexOutOfBoundsException();
        }

        this.board[y][x] = player;
    }

    public char[][] toChars() {
        char[][] chars = new char[this.height][this.width];

        for (int y = 0; y < this.height; y++) {
            for (int x = 0; x < this.width; x++) {
                chars[y][x] = this.board[y][x].getInitial();
            }
        }

        return chars;
    }

    /**
     * For debugging purposes
     * @return The board array displayed as a string with linebreaks
     */
    public String toString() {
        StringBuilder res = new StringBuilder();
        for(IPlayer[] row : board) {
            res.append(Arrays.stream(row).map(IPlayer::getInitial).toString()).append("\n");  // TODO not sure if this works
        }
        return res.toString();
    }

    public int getHeight() {
        return this.height;
    }

    public int getWidth() {
        return this.width;
    }
}
