package isy.team4.projectisy.model.player;

import isy.team4.projectisy.util.Board;
import isy.team4.projectisy.util.Vector2D;

import java.util.Random;

public class AIPlayer implements IPlayer {
    private final String name;
    private char initial;

    public AIPlayer(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public char getInitial() {
        return this.initial;
    }

    @Override
    public Vector2D getMove(Board board) {
//        char[] arrayboard = new char[board.getHeight() * board.getWidth()];
//        for(int i = 0; i < board.getHeight(); i++) {
//            for(int j = 0; j < board.getWidth(); j++) {
//                arrayboard[i*j] = board.toChars()[i][j];
//            }
//        }
//        int idx = minimax(arrayboard, 10, true);
        // TODO: minimax

        Random r = new Random();
        return new Vector2D(r.nextInt(3), r.nextInt(3));
    }

    @Override
    public void setInitial(char initial) {
        this.initial = initial;
    }

    public int minimax(char[] flatboard, int depth, boolean maximizingPlayer) {
        for(Character c : flatboard) {
            System.out.println(c);
        }
        return 1;
    }

    public String toString() {
        return "AIplayer";
    }
}
