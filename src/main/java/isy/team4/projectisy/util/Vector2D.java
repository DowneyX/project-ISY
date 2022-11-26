package isy.team4.projectisy.util;

public class Vector2D {
    public int x;
    public int y;

    public Vector2D(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Vector2D(int i, Board board) {
        int y = (i) / board.getWidth();
        int x = (i) % board.getHeight();

        this.y = y;
        this.x = x;
    }

    public int toInt(Board board) {
        return y * board.getWidth() + x;
    }
}
