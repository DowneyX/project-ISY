package isy.team4.projectisy.player;

import isy.team4.projectisy.util.Board;

public abstract class Player {
    private String name;
    protected char turn;
    public Board board;

    Player(String name, char turn) {
        this.name = name;
        this.turn = turn;
    }

    public char getTurn() {
        return turn;
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    public abstract int[] getMove();

    public String getName() {
        return name;
    }
}
