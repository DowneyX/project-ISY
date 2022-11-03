package isy.team4.projectisy.model.game;

import isy.team4.projectisy.model.player.IPlayer;
import isy.team4.projectisy.util.Board;
import isy.team4.projectisy.util.Result;

public interface IGame {
    void start();
    void stop();
    Board getBoard();
    IPlayer getCurrentPlayer();
    IPlayer[] getPlayers();
    Result getResult();  // Nullable
}
