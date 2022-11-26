package isy.team4.projectisy.model.game;

import isy.team4.projectisy.model.player.IPlayer;
import isy.team4.projectisy.util.Board;
import isy.team4.projectisy.util.Result;
import isy.team4.projectisy.util.Vector2D;

public interface IGame {
    void start();

    void stop();

    Board getBoard();

    IPlayer getCurrentPlayer();

    IPlayer[] getPlayers();

    Result getResult(); // Nullable

    void setGameHandler(IGameHandler gameHandler); // set because else a generic IGame can not be used

    Vector2D[] getValidMoves(Board board);
}
