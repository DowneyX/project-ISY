package isy.team4.projectisy.model.rule;

import isy.team4.projectisy.model.player.IPlayer;
import isy.team4.projectisy.util.Board;
import isy.team4.projectisy.util.Vector2D;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Objects;

public class OthelloRuleSet implements IRuleSet {
    private Board oldBoard;
    private Board newBoard;
    private IPlayer[] players;
    private IPlayer winningPlayer;

    /**
     * players is needed to count cells.
     *
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
        return new char[]{'⭕', '⬤'};
    } // ○

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
                if (cell == null) {
                    hasEmptyCells = true;
                }

                if (cell == players[0]) {
                    p1++;
                } else if (cell == players[1]) {
                    p2++;
                }
            }
        }

        if (!hasEmptyCells) {
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
     *
     * @return
     */
    public int[] getValidMoves(IPlayer currentplayer) {
        ArrayList<Integer> valid = new ArrayList<>();

        for (int i = 0; i < newBoard.getWidth() * newBoard.getHeight(); i++) {
            int x = i % newBoard.getWidth();
            int y = i / newBoard.getHeight();

            // should be empty in order to be valid
            if (newBoard.getElement(x, y) != null) {
                continue;
            }

            // Loop over every 45 degrees
            for(int j = 0; j < 360; j += 45) {
                if (checkValidMove(j, new Vector2D(x, y), currentplayer)) {
                    valid.add(i);
                }
            }
        }

        return valid.stream().mapToInt(i -> i).toArray();
    }

    /**
     * Checks if the made move is valid by going from the origin into a direction where origin and end are currentplayer and everything in between is oppositeplayer
     * TODO: refactor into makemove that returns a newboard where any change means move is valid
     *
     * @param direction - the direction we will look at in degrees
     * @param origin    - the move that is made
     */
    public boolean checkValidMove(int direction, Vector2D origin, IPlayer currentplayer) {
        int c = 1; // count of steps between cells
        int x = origin.x;
        int y = origin.y;
        IPlayer origincell = newBoard.getElement(x, y);

        // Origin should be empty
        if (origincell != null) {
            return false;
        }

        for (int i = 0; i < newBoard.getWidth(); i++) {
            IPlayer between;
            try {
                switch (direction) {
                    case 0: // north
                        between = newBoard.getElement(x, y + c);
                        break;
                    case 45: // northeast
                        between = newBoard.getElement(x + c, y + c);
                        break;
                    case 90: // east
                        between = newBoard.getElement(x + c, y);
                        break;
                    case 135: // southeast
                        between = newBoard.getElement(x + c, y - c);
                        break;
                    case 180: // south
                        between = newBoard.getElement(x, y - c);
                        break;
                    case 225: // southwest
                        between = newBoard.getElement(x - c, y - c);
                        break;
                    case 270: // west
                        between = newBoard.getElement(x - c, y);
                        break;
                    case 315: // northwest
                        between = newBoard.getElement(x - c, y + c);
                        break;
                    default:
                        return false;
                }
            } catch(Exception e) {
                // out of bounds is false
                return false; // TODO: check
            }

            if(between == null) {
                return false;
            } if(between.getInitial() == currentplayer.getInitial()) { // between is currentplayer
                return c > 1;
            } else {
                c++;
            }
        }

//        System.out.println("No space!");
        return false;
    }
}
