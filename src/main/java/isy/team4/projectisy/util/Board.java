package isy.team4.projectisy.util;

import isy.team4.projectisy.model.player.IPlayer;

import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Stream;

public class Board {
    protected int width; // Could be private if no specific boards are needed
    protected int height; // Could be private if no specific boards are needed
    protected IPlayer[][] board; // Could be private if no specific boards are needed

    public Board(int width, int height) {
        this.width = width;
        this.height = height;
        this.board = new IPlayer[height][width]; // reversed for proper x-y notation
    }

    public IPlayer[][] getData() {
        return this.board;
    }

    public void clear() {
        this.board = new IPlayer[this.height][this.width];
    }

    public boolean isFull() {
        for (int x = 0; x < height; x++) {
            for (int y = 0; y < height; y++) {
                if (board[x][y] == null) {
                    return false;
                }
            }
        }
        return true;
    }

    public Board copyBoard() {
        Board newboard = new Board(width, height);
        for (int x = 0; x < height; x++) {
            for (int y = 0; y < height; y++) {
                newboard.setElement(board[x][y], x, y);
            }
        }
        return newboard;
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

    public Character[][] toChars() {
        Character[][] chars = new Character[this.height][this.width];

        for (int y = 0; y < this.height; y++) {
            for (int x = 0; x < this.width; x++) {
                if (this.board[y][x] == null) {
                    chars[y][x] = null;
                    continue;
                }

                chars[y][x] = this.board[y][x].getInitial();
            }
        }

        return chars;
    }

    public int getTotalMovesMade() {
        return (int) this.getFlatData()
                .filter(Objects::nonNull)
                .count();
    }

    /** 'Rotates' board 90deg to read data **/
    public IPlayer[][] getRotatedData() {
        IPlayer[][] currentData = this.getData();
        IPlayer[][] out = new IPlayer[this.height][this.width];

        for (int x = 0; x < this.getWidth(); x++) {
            for (int y = 0; y < this.getHeight(); y++) {
                out[x][y] = currentData[y][x];
            }
        }

        return out;
    }

    public Stream<IPlayer> getFlatData() {
        return Arrays.stream(this.board)
                .flatMap(Stream::of);
    }

    /**
     * For debugging purposes
     * 
     * @return The board array displayed as a string with linebreaks
     */
    public String toString() {
        StringBuilder res = new StringBuilder();
        for (IPlayer[] row : board) {
            res.append(Arrays.stream(row).map(IPlayer::getInitial).toString()).append("\n"); // TODO not sure if this
                                                                                             // works
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
