package isy.team4.projectisy.model.rule;

import java.util.*;
import java.util.stream.IntStream;

import isy.team4.projectisy.model.player.IPlayer;
import isy.team4.projectisy.util.Board;
import isy.team4.projectisy.util.Vector2D;

public class OthelloRuleSet implements IRuleSet {
    private final int[] cellScores = {
            100, -20, 10, 5, 5, 10, -20, 100,
            -20, -50, -2, -2, -2, -2, -50, -20,
            10, -2, -1, -1, -1, -1, -2, 10,
            5, -2, -1, -1, -1, -1, -2, 5,
            5, -2, -1, -1, -1, -1, -2, 5,
            10, -2, -1, -1, -1, -1, -2, 10,
            -20, -50, -2, -2, -2, -2, -50, -20,
            100, -20, 10, 5, 5, 10, -20, 100
    };
    private Board board;
    private IPlayer[] players;
    private IPlayer winningPlayer;

    private int[] presetOrder;

    public OthelloRuleSet() {
        this.presetOrder = getPresortOrder();
    }

    public OthelloRuleSet(OthelloRuleSet othelloRuleSet) {
        this.players = othelloRuleSet.players;
        this.board = othelloRuleSet.board;
        this.winningPlayer = othelloRuleSet.winningPlayer;
        this.presetOrder = getPresortOrder();
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
        Board result = new Board(8, 8);
        result.setElement(players[0], 3, 4);
        result.setElement(players[0], 4, 3);
        result.setElement(players[1], 3, 3);
        result.setElement(players[1], 4, 4);
        return result;
    }

    @Override
    public boolean isLegal(IPlayer player, Vector2D move) {
        // TODO have a diffent way of checking if current situation is legal
        for (Vector2D validMove : getValidMoves(player)) {
            if (validMove.x == move.x && validMove.y == move.y) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Board handleMove(Vector2D move, IPlayer player) {
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

        this.board.setElement(player, x, y);

        // loop again and update every change
        for (int i = 0; i < this.board.getWidth() * this.board.getHeight(); i++) { // newboard is already changed
            for (int change : toChange) {
                if (i == change) {
                    int x2 = i % this.board.getWidth();
                    int y2 = i / this.board.getHeight();

                    this.board.setElement(player, x2, y2);
                }
            }
        }

        return this.board;
    }

    @Override
    public boolean isWon() {
        // if there are still moves available then game is not over yet tus not won
        for (IPlayer player : players) {
            if (getValidMoves(player).length > 0) {
                this.winningPlayer = null;
                return false;
            }
        }

        int[] scores = this.getScores();

        // return result
        if (scores[0] == scores[1]) {
            this.winningPlayer = null;
            return false;
        }

        this.winningPlayer = scores[0] > scores[1] ? players[0] : players[1];
        return true;
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
        // if there are still moves available then game is not over yet tus not won
        for (IPlayer player : players) {
            if (getValidMoves(player).length > 0) {
                return false;
            }
        }

        int[] scores = this.getScores();

        return scores[0] == scores[1];
    }

    /**
     * Returns the currently valid moves so they can be displayed. Also useful for
     * isLegal
     */
    public Vector2D[] getValidMoves(IPlayer player) {
        List<Vector2D> moves = new ArrayList<>();

        for (int i = 0; i < presetOrder.length; i++) {
            int x = presetOrder[i] % board.getWidth();
            int y = presetOrder[i] / board.getHeight();

            // should be empty in order to be valid
            if (board.getElement(x, y) != null) {
                continue;
            }

            // Loop over every 45 degrees
            for (int j = 0; j < 360; j += 45) {
                if (doMove(j, new Vector2D(x, y), player) != null) {
                    moves.add(new Vector2D(x, y));
                }
            }
        }

        return moves.toArray(new Vector2D[0]);
    }

    @Override
    public void setBoard(Board board) {
        this.board = board;
    }

    @Override
    public void setPlayers(IPlayer[] players) {
        this.players = players;
    }

    @Override
    public boolean isPass(IPlayer player) {
        return getValidMoves(player).length < 1;
    }

    @Override
    public IRuleSet clone() {
        return new OthelloRuleSet(this);
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
    public int[] doMove(int direction, Vector2D origin, IPlayer player) {
        int c = 1; // count of steps between cells
        int x = origin.x;
        int y = origin.y;
        IPlayer originCell = board.getElement(x, y);

        // Origin should be empty. You can not play an unavailable position.
        if (originCell != null) {
            return null;
        }

        ArrayList<Integer> remember = new ArrayList<>(64); // indices to remember. return if valid

        for (int i = 0; i < board.getWidth(); i++) {
            int x2;
            int y2;
            IPlayer between;
            try {
                switch (direction) {
                    case 0: // north
                        x2 = x;
                        y2 = y+c;
                        if(!isInBounds(x2, y2)) return null;
                        between = board.getElement(x, y + c);
                        remember.add((y + c) * board.getWidth() + x);
                        break;
                    case 45: // northeast
                        x2 = x+c;
                        y2 = y+c;
                        if(!isInBounds(x2, y2)) return null;
                        between = board.getElement(x + c, y + c);
                        remember.add((y + c) * board.getWidth() + (x + c));
                        break;
                    case 90: // east
                        x2 = x+c;
                        y2 = y;
                        if(!isInBounds(x2, y2)) return null;
                        between = board.getElement(x + c, y);
                        remember.add(y * board.getWidth() + (x + c));
                        break;
                    case 135: // southeast
                        x2 = x+c;
                        y2 = y-c;
                        if(!isInBounds(x2, y2)) return null;
                        between = board.getElement(x + c, y - c);
                        remember.add((y - c) * board.getWidth() + (x + c));
                        break;
                    case 180: // south
                        x2 = x;
                        y2 = y-c;
                        if(!isInBounds(x2, y2)) return null;
                        between = board.getElement(x, y - c);
                        remember.add((y - c) * board.getWidth() + x);
                        break;
                    case 225: // southwest
                        x2 = x-c;
                        y2 = y-c;
                        if(!isInBounds(x2, y2)) return null;
                        between = board.getElement(x - c, y - c);
                        remember.add((y - c) * board.getWidth() + (x - c));
                        break;
                    case 270: // west
                        x2 = x-c;
                        y2 = y;
                        if(!isInBounds(x2, y2)) return null;
                        between = board.getElement(x - c, y);
                        remember.add(y * board.getWidth() + (x - c));
                        break;
                    case 315: // northwest
                        x2 = x-c;
                        y2 = y+c;
                        if(!isInBounds(x2, y2)) return null;
                        between = board.getElement(x - c, y + c);
                        remember.add((y + c) * board.getWidth() + (x - c));
                        break;
                    default:
                        return null;
                }
            } catch (Exception e) {
                // out of bounds is always false
                System.out.println("Warning: Out of bounds in doMove. Should not happen.");
                return null;
            }

            if (between == null) { // there can be no empty spaces in between
                return null;
            }
            if (between.getInitial() == player.getInitial()) { // between is the current player, which will end
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

    /**
     * Checks if x and y is in bounds instead of try catch
     * @return
     */
    public boolean isInBounds(int x, int y) {
//        System.out.println(x + " " + y);
        return x > 0 && x < board.getWidth() && y > 0 && y < board.getHeight();
    }

    @Override
    public int getScore(IPlayer player) {
        int score = 0;
        IPlayer[] data = board.getFlatData().toArray(IPlayer[]::new);

        for (int i = 0; i < data.length; i++) {
            if (data[i] != null && data[i] == player) {
                score += cellScores[i];
            }
        }

        return score;
    }

    private int[] getScores() {
        int p1 = 0;
        int p2 = 0;
        IPlayer[] cells = board.getFlatData().toArray(IPlayer[]::new);

        for (IPlayer cell : cells) {
            if (cell == this.players[0]) {
                p1++;
            } else if (cell == this.players[1]) {
                p2++;
            }
        }

        return new int[]{p1, p2};
    }

    /**
     * Returns a list of indices for scores high to low to improve alpha-beta pruning
     * @return
     */
    public int[] getPresortOrder() {
        Integer[] heuristics = Arrays.stream( cellScores ).boxed().toArray( Integer[]::new );

        int[] sorted = IntStream.range(0, heuristics.length)
                .boxed().sorted((i, j) -> -heuristics[i].compareTo(heuristics[j]))
                .mapToInt(ele -> ele).toArray();

        for (int i = 0; i < sorted.length; i++) {
        }

        return sorted;
    }
}
