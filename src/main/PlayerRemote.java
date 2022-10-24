package src.main;

import java.util.Scanner;

public class PlayerRemote extends Player {

    Scanner scanner = new Scanner(System.in);

    PlayerRemote(String name, char turn) {
        super(name, turn);
    }

    public int[] getMove() {
        // wait for for player to make a move on the server

        // get move made on server

        // translate move into x y coordinates

        // return move
        int[] move = { 0, 0 };
        return move;
    }
}
