package isy.team4.projectisy.player;

public class PlayerRemote extends Player {

    public PlayerRemote(String name, char turn) {
        super(name, turn);
    }

    public int[] getMove() {
        // remote player doesn't do anything at all
        int[] move = { 0, 0 };
        return move;
    }
}
