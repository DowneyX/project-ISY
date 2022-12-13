package isy.team4.projectisy.model.rule;

import isy.team4.projectisy.model.player.IPlayer;
import isy.team4.projectisy.util.Board;
import isy.team4.projectisy.util.Vector2D;

public interface IRuleSet {

    int getScore(IPlayer player);

    Integer getMinPlayerSize(); // Nullable

    Integer getMaxPlayerSize(); // Nullable

    char[] getAllowedInitials(); // Nullable if all allowed

    Board getStartingBoard();

    boolean isLegal(IPlayer currentplayer, Vector2D move); // othello requires currentplayer, because legal moves differ
                                                           // per player

    void handleMove(Vector2D move, IPlayer currentplayer);

    boolean isWon();

    IPlayer getWinningPlayer() throws NullPointerException;

    boolean isDraw();

    IRuleSet clone();

    void setBoard(Board board);

    boolean isPass(IPlayer player);

    Vector2D[] getValidMoves(IPlayer currentplayer); // board next to currentplayer because the controller
                                                     // requires the validmoves for newboard, and isLegal
                                                     // requires oldboard
}
