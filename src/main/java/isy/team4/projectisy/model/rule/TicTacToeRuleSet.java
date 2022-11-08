package isy.team4.projectisy.model.rule;

import isy.team4.projectisy.model.player.IPlayer;
import isy.team4.projectisy.util.Board;

import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Stream;

public final class TicTacToeRuleSet implements IRuleSet {
    private Board oldBoard;
    private Board newBoard;
    private IPlayer[] players;
    private IPlayer winningPlayer;

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
    public void setTurn(Board board, Board newBoard) {
        this.oldBoard = board;
        this.newBoard = newBoard;
    }

    @Override
    public boolean isLegal() {
        return this.newBoard.getTotalMovesMade() > this.oldBoard.getTotalMovesMade();
    }

    @Override
    public Board handleBoard(Board board) {
        return null;
    }

    @Override
    public boolean isWon() {
        boolean won = false;
        IPlayer[][] grid = this.newBoard.getData();

        if (
                grid[0][0] == grid[0][1] && grid[0][1] == grid[0][2] && grid[0][0] != null ||
                grid[1][0] == grid[1][1] && grid[1][1] == grid[1][2] && grid[1][0] != null ||
                grid[2][0] == grid[2][1] && grid[2][1] == grid[2][2] && grid[2][0] != null ||
                grid[0][0] == grid[1][0] && grid[1][0] == grid[2][0] && grid[0][0] != null ||
                grid[0][1] == grid[1][1] && grid[1][1] == grid[2][1] && grid[0][1] != null ||
                grid[0][2] == grid[1][2] && grid[1][2] == grid[2][2] && grid[0][2] != null ||
                grid[0][0] == grid[1][1] && grid[1][1] == grid[2][2] && grid[0][0] != null ||
                grid[0][2] == grid[1][1] && grid[1][1] == grid[2][0] && grid[0][2] != null
        ) {
            this.winningPlayer = (grid[0][0] != null) ? grid[0][0] : (grid[1][1] != null) ? grid[1][1] : grid[2][2];
            won = true;
        }

        return won;
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
        return !this.isWon() && this.newBoard
                .getFlatData()
                .noneMatch(Objects::isNull);
    }
}
