package src.main;

import java.util.Random;

public class PlayerSmartTictactoe extends Player {

    Random rand = new Random();
    Board board;

    PlayerSmartTictactoe(String name, char turn) {
        super(name, turn);
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    public int[] getMove() {

        // calculate best move based on the current board object
        // (make sure that the computer gets this board object)
        int[] move = { rand.nextInt(3), rand.nextInt(3) };
        return move;
    }
}
