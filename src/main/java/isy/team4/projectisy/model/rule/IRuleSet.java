package isy.team4.projectisy.model.rule;

import isy.team4.projectisy.model.player.IPlayer;
import isy.team4.projectisy.util.Board;
import isy.team4.projectisy.util.Vector2D;

public interface IRuleSet {
    Integer getMinPlayerSize();  // Nullable
    Integer getMaxPlayerSize();  // Nullable
    char[] getAllowedInitials();  // Nullable if all allowed
    Board getStartingBoard();

    void setTurn(Board board, Board newBoard);
    boolean isLegal();
    Board handleBoard(Board board);
    boolean isWon();
    IPlayer getWinningPlayer() throws NullPointerException;
    boolean isDraw();
}
