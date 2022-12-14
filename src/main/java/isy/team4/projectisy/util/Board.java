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

        // Fill in with null values
        for(int y = 0; y < this.height; ++y) {
            for(int x = 0; x < this.width; ++x) {
                this.board[y][x] = null;
            }
        }
    }

    /**
     * Copy constructor to create a deep copy for checking if a move is valid
     * 
     * @param board
     */
    public Board(Board board) {
        this.width = board.width;
        this.height = board.height;
        this.board = new IPlayer[height][width];

        for (int i = 0; i < board.height; i++) {
            System.arraycopy(board.board[i], 0, this.board[i], 0, board.height);
        }

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

    /**
     * @throws ArrayIndexOutOfBoundsException when x or y out of bounds
     */
    public void setElement(IPlayer player, int x, int y) {
        if (x >= this.width || y >= this.height) {
            throw new ArrayIndexOutOfBoundsException();
        }

        this.board[y][x] = player;
    }

    public IPlayer getElement(int x, int y) {
        if (x >= this.width || y >= this.height) {
            throw new ArrayIndexOutOfBoundsException();
        }

        return this.board[y][x];
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
        for (IPlayer[] row : getData()) {
            // res.append(Arrays.stream(row).map(IPlayer::getInitial).toString()).append("\n");
            // // TODO: fix this
            for (IPlayer cell : row) {
                if (cell == null) {
                    res.append("null ");
                } else {
                    res.append(cell.toString()).append(" ");
                }
            }
            res.append("\n");
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
