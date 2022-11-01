package src.main;

import java.util.Random;

public class PlayerSmartTictactoe extends Player {

    Random rand = new Random();

    PlayerSmartTictactoe(String name, char turn) {
        super(name, turn);
    }

    public int[] getMove() {
        int[][] possibleMoves = {
                { 1, 1 }, { 0, 0 }, { 0, 2 }, { 2, 0 }, { 2, 2 },
                { 0, 1 }, { 1, 0 }, { 1, 2 }, { 2, 1 } };

        for (int[] move : possibleMoves) {
            if (board.get_element(move[0], move[1]) == '\u0000') {
                return move;
            }
        }

        // calculate best move based on the current board object
        // (make sure that the computer gets this board object)
        int[] move = { rand.nextInt(3), rand.nextInt(3) };
        return move;
    }
}
