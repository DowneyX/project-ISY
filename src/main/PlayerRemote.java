package src.main;

import java.util.Scanner;

public class PlayerRemote extends Player {

    PlayerRemote(String name, char turn) {
        super(name, turn);
    }

    public int[] getMove() {
        // remote player doesn't do anything at all
        int[] move = { 0, 0 };
        return move;
    }
}
