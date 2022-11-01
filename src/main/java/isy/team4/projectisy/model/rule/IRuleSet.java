package isy.team4.projectisy.model.rule;

import isy.team4.projectisy.model.player.IPlayer;
import isy.team4.projectisy.util.Board;

public interface IRuleSet {
    void setTurn(IPlayer player, Board board, Board newBoard);

    boolean isLegal();
    Board handleBoard(Board board);
    boolean isWon();
    boolean getWinningPlayer() throws NullPointerException;
    boolean isDraw();
    Board[] getBoardHistory();
    IPlayer[] getTurnHistory();
}
