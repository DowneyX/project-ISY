package src.main;

public abstract class Player {
    private String name;
    private char turn;

    Player(String name, char turn) {
        this.name = name;
        this.turn = turn;
    }

    public char getTurn() {
        return turn;
    }

    public abstract int[] getMove();

    public String getName() {
        return name;
    }
}
