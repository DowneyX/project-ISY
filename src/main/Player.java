package src.main;

public abstract class Player {
    private String name;
    private char turn;
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
