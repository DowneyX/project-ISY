package isy.team4.projectisy.model.game;

import isy.team4.projectisy.model.player.IPlayer;
import isy.team4.projectisy.observer.IObservable;
import isy.team4.projectisy.util.Board;
import isy.team4.projectisy.util.Result;
import isy.team4.projectisy.util.Vector2D;

public interface IGame extends IObservable<IGameObserver> {
    void start();

    void stop();
    boolean isRunning();

    Board getBoard();

    IPlayer getCurrentPlayer();

    IPlayer[] getPlayers();

    Result getResult(); // Nullable

    Vector2D[] getValidMoves(Board board);
}
