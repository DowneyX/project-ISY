package isy.team4.projectisy.model.rule;

import isy.team4.projectisy.model.player.IPlayer;
import isy.team4.projectisy.util.Board;
import isy.team4.projectisy.util.Vector2D;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class OthelloRuleSet implements IRuleSet {
    private Board board;
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
        return new char[] { '⭕', '⬤' };
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
    public boolean isLegal(IPlayer currentplayer, Vector2D move) {
        // TODO have a diffent way of checking if current situation is legal
        for (Vector2D validmove : getValidMoves(currentplayer)) {
            if (validmove.x == move.x && validmove.y == move.y) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void handleMove(Vector2D move, IPlayer player) {
        // get changed idx, first difference found
        int x = move.x;
        int y = move.y;

        if (!isLegal(player, move)) {
            try {
                throw new Exception("this move was not legal, check if move is legal before you handle it");
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        ArrayList<Integer> toChange = new ArrayList<>();

        // Loop over every 45 degrees
        for (int i = 0; i < 360; i += 45) {
            int[] moveChanges = doMove(i, new Vector2D(x, y), player);
            if (moveChanges != null) {
                for (int change : moveChanges) {
                    toChange.add(change);
                }
            }
        }

        board.setElement(player, x, y);

        // loop again and update every change
        for (int i = 0; i < board.getWidth() * board.getHeight(); i++) { // newboard is already changed
            for (int change : toChange) {
                if (i == change) {
                    int x2 = i % board.getWidth();
                    int y2 = i / board.getHeight();

                    board.setElement(player, x2, y2);
                }
            }
        }
    }

    @Override
    public boolean isWon() {
        IPlayer[][] grid = board.getData();

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
        return !this.isWon() && this.board.getFlatData().noneMatch(Objects::isNull);
    }

    /**
     * Returns the currently valid moves so they can be displayed. Also useful for
     * isLegal
     */
    public Vector2D[] getValidMoves(IPlayer currentplayer) {
        List<Vector2D> moves = new ArrayList<Vector2D>();

        for (int i = 0; i < board.getWidth() * board.getHeight(); i++) {
            int x = i % board.getWidth();
            int y = i / board.getHeight();

            // should be empty in order to be valid
            if (board.getElement(x, y) != null) {
                continue;
            }

            // Loop over every 45 degrees
            for (int j = 0; j < 360; j += 45) {
                if (doMove(j, new Vector2D(x, y), currentplayer) != null) {
                    moves.add(new Vector2D(x, y));
                }
            }
        }

        return moves.toArray(new Vector2D[0]);
    }

    /**
     * Checks if the made move is valid by going from the origin into a direction
     * where origin and end are currentplayer and everything in between is
     * oppositeplayer
     * Returns the changes for handleboard. Returns null if there are no changed,
     * which subsequently means a move is invalid
     *
     * @param direction - the direction we will look at in degrees
     * @param origin    - the move that is made
     */
    public int[] doMove(int direction, Vector2D origin, IPlayer currentplayer) {
        int c = 1; // count of steps between cells
        int x = origin.x;
        int y = origin.y;
        IPlayer origincell = board.getElement(x, y);

        // Origin should be empty. You can not play an unavailable position.
        if (origincell != null) {
            return null;
        }

        ArrayList<Integer> remember = new ArrayList<>(); // indices to remember. return if valid

        for (int i = 0; i < board.getWidth(); i++) {
            IPlayer between;
            try {
                switch (direction) {
                    case 0: // north
                        between = board.getElement(x, y + c);
                        remember.add((y + c) * board.getWidth() + x);
                        break;
                    case 45: // northeast
                        between = board.getElement(x + c, y + c);
                        remember.add((y + c) * board.getWidth() + (x + c));
                        break;
                    case 90: // east
                        between = board.getElement(x + c, y);
                        remember.add(y * board.getWidth() + (x + c));
                        break;
                    case 135: // southeast
                        between = board.getElement(x + c, y - c);
                        remember.add((y - c) * board.getWidth() + (x + c));
                        break;
                    case 180: // south
                        between = board.getElement(x, y - c);
                        remember.add((y - c) * board.getWidth() + x);
                        break;
                    case 225: // southwest
                        between = board.getElement(x - c, y - c);
                        remember.add((y - c) * board.getWidth() + (x - c));
                        break;
                    case 270: // west
                        between = board.getElement(x - c, y);
                        remember.add(y * board.getWidth() + (x - c));
                        break;
                    case 315: // northwest
                        between = board.getElement(x - c, y + c);
                        remember.add((y + c) * board.getWidth() + (x - c));
                        break;
                    default:
                        return null;
                }
            } catch (Exception e) {
                // out of bounds is always false
                return null;
            }

            if (between == null) { // there can be no empty spaces in between
                return null;
            }
            if (between.getInitial() == currentplayer.getInitial()) { // between is the current player, which will end
                                                                      // the check
                if (c > 1) { // check if there is more than one step, which means that there is at least one
                             // item in between two of ours
                    return remember.stream().mapToInt(j -> j).toArray();
                } else {
                    return null;
                }
            } else { // between is an opposite player, and we need to check the next iter
                c++;
            }
        }

        return null;
    }

    @Override
    public void setBoard(Board board) {
        this.board = board;
    }

    @Override
    public IRuleSet clone() {
        IRuleSet newRuleset = new OthelloRuleSet(players);
        return newRuleset;
    }
}
