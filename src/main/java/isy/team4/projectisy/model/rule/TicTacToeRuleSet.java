package isy.team4.projectisy.model.rule;

import isy.team4.projectisy.model.player.IPlayer;
import isy.team4.projectisy.util.Board;

public final class TicTacToeRuleSet implements IRuleSet {
    // TODO: Implement methods
    @Override
    public void setTurn(IPlayer player, Board board, Board newBoard) {

    }

    @Override
    public boolean isLegal() {
        return false;
    }

    @Override
    public Board handleBoard(Board board) {
        return null;
    }

    @Override
    public boolean isWon() {
        return false;
    }

    @Override
    public boolean getWinningPlayer() throws NullPointerException {
        return false;
    }

    @Override
    public boolean isDraw() {
        return false;
    }

    @Override
    public IPlayer getNextTurn() {
        return null;
    }

    @Override
    public Board[] getBoardHistory() {
        return new Board[0];
    }

    @Override
    public IPlayer[] getTurnHistory() {
        return new IPlayer[0];
    }
}
