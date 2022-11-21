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

    boolean isLegal(IPlayer currentplayer); // othello requires currentplayer, because legal moves differ per player

    Board handleBoard(Board board, IPlayer currentplayer);

    boolean isWon();

    IPlayer getWinningPlayer() throws NullPointerException;

    boolean isDraw();

    int[] getValidMoves(IPlayer currentplayer, Board board); // board next to currentplayer because the controller requires the validmoves for newboard, and isLegal requires oldboard
}
