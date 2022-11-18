package isy.team4.projectisy.model.rule;

import isy.team4.projectisy.model.player.IPlayer;
import isy.team4.projectisy.util.Board;

import java.util.ArrayList;
import java.util.Objects;

public class OthelloRuleSet implements IRuleSet {
    private Board oldBoard;
    private Board newBoard;
    private IPlayer[] players;
    private IPlayer winningPlayer;

    /**
     * players is needed to count cells.
     * @param players
     */
    public OthelloRuleSet(IPlayer[] players) {
        this.players = players;
    }

    @Override
    public Integer getMinPlayerSize() {
        return 2;
    }

    @Override
    public Integer getMaxPlayerSize() {
        return 2;
    }

    @Override
    public char[] getAllowedInitials() {
        return new char[]{'⚪', '⚫'}; // ◌ for possible next moves
    }

    @Override
    public Board getStartingBoard() {
        Board board = new Board(8, 8);
        board.setElement(players[0], 3, 3);
        board.setElement(players[0], 4, 4);
        board.setElement(players[1], 3, 4);
        board.setElement(players[1], 4, 3);
        return board;
    }

    @Override
    public void setTurn(Board board, Board newBoard) {
        this.oldBoard = board;
        this.newBoard = newBoard;
    }

    @Override
    public boolean isLegal() {
        return true; // TODO: add
    }

    @Override
    public Board handleBoard(Board board) {
        return null;
    }

    @Override
    public boolean isWon() {
        IPlayer[][] grid = this.newBoard.getData();

        int p1 = 0;
        int p2 = 0;

        boolean hasEmptyCells = false;

        // player with most cells wins
        for (IPlayer[] row : grid) {
            for (IPlayer cell : row) {
                if(cell == null) {
                    hasEmptyCells = true;
                }

                if (cell == players[0]) {
                    p1++;
                } else if (cell == players[1]) {
                    p2++;
                }
            }
        }

        if(!hasEmptyCells) {
            if (p1 > p2) {
                this.winningPlayer = players[0];
            } else if (p1 < p2) {
                this.winningPlayer = players[1];
            }
        }

        return this.winningPlayer != null;
    }

    @Override
    public IPlayer getWinningPlayer() throws NullPointerException {
        if (this.winningPlayer == null) {
            throw new NullPointerException();
        }

        return this.winningPlayer;
    }

    @Override
    public boolean isDraw() {
        return !this.isWon() && this.newBoard.getFlatData().noneMatch(Objects::isNull);
    }

    /**
     * Returns the currently valid moves so they can be displayed. Also useful for isLegal
     * @return
     */
    public int[] getValidMoves() {
        ArrayList<Integer> valid = new ArrayList<>();

        for (int i = 0; i < oldBoard.getWidth() * oldBoard.getHeight(); i++) {
            int x = i % oldBoard.getWidth();
            int y = i / oldBoard.getHeight();

            if(x == y) {
                valid.add(i); // TODO: return actual valid moves
            }
        }

        return valid.stream().mapToInt(i -> i).toArray();
    }
}
