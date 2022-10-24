package src.main;

import java.util.Scanner;

public class PlayerHuman extends Player {

    Scanner scanner = new Scanner(System.in);

    PlayerHuman(String name, char turn) {
        super(name, turn);
    }

    public int[] getMove() {
        int[] move = { 0, 0 };
        System.out.println("please enter your move");
        System.out.println("enter x coordinate");
        move[0] = scanner.nextInt();
        System.out.println("enter y coordinate");
        move[1] = scanner.nextInt();
        return move;
    }
}
