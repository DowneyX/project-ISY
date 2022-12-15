package isy.team4.projectisy.model.rule;

import isy.team4.projectisy.model.player.IPlayer;
import isy.team4.projectisy.util.Board;
import isy.team4.projectisy.util.Vector2D;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class TicTacToeRuleSet implements IRuleSet {
    private Board board;
    private IPlayer[] players;
    private IPlayer winningPlayer;

    public TicTacToeRuleSet() {
    }

    public TicTacToeRuleSet(TicTacToeRuleSet ticTacToeRuleSet) {
        this.board = ticTacToeRuleSet.board;
        this.players = ticTacToeRuleSet.players;
        this.winningPlayer = ticTacToeRuleSet.winningPlayer;
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
        return new char[] { 'X', 'O' };
    }

    @Override
    public Board getStartingBoard() {
        return new Board(3, 3);
    }

    @Override
    public boolean isLegal(IPlayer player, Vector2D move) {
        // TODO have a diffent way of checking if current situation is legal'

        return board.getElement(move.x, move.y) == null;
    }

    @Override
    public Board handleMove(Vector2D move, IPlayer player) {
        this.board.setElement(player, move.x, move.y);
        return this.board;
    }

    @Override
    public boolean isWon() {
        IPlayer[][] grid = board.getData();

        if (grid[0][0] == grid[0][1] && grid[0][1] == grid[0][2] && grid[0][0] != null
                || grid[0][0] == grid[1][1] && grid[1][1] == grid[2][2] && grid[0][0] != null
                || grid[0][0] == grid[1][0] && grid[1][0] == grid[2][0] && grid[0][0] != null) {
            this.winningPlayer = grid[0][0];
        } else if (grid[1][0] == grid[1][1] && grid[1][1] == grid[1][2] && grid[1][0] != null
                || grid[0][1] == grid[1][1] && grid[1][1] == grid[2][1] && grid[0][1] != null
                || grid[0][2] == grid[1][1] && grid[1][1] == grid[2][0] && grid[0][2] != null) {
            this.winningPlayer = grid[1][1];
        } else if (grid[2][0] == grid[2][1] && grid[2][1] == grid[2][2] && grid[2][0] != null
                || grid[0][2] == grid[1][2] && grid[1][2] == grid[2][2] && grid[0][2] != null) {
            this.winningPlayer = grid[2][2];
        } else {
            this.winningPlayer = null; // reset winning player this is nessary for the ai
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

    public Vector2D[] getValidMoves(IPlayer player) {
        List<Vector2D> moves = new ArrayList<Vector2D>();
        for (int i = 0; i < board.getWidth() * board.getHeight(); i++) {
            int x = i % board.getWidth();
            int y = i / board.getHeight();

            if (board.getElement(x, y) == null) {
                moves.add(new Vector2D(x, y));
            }
        }
        return moves.toArray(new Vector2D[0]);
    }

    @Override
    public IRuleSet clone() {
        return new TicTacToeRuleSet(this);
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
        return false;
    }

    @Override
    public int getScore() {
        if (isWon()) {
            return 100000;
        }
        return 0;
    }
}
