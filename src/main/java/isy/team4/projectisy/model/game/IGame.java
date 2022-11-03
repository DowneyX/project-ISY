package isy.team4.projectisy.model.game;

import isy.team4.projectisy.model.player.IPlayer;

public interface IGame {
    void start();
    void stop();
    IPlayer getCurrentPlayer();
    IPlayer[] getPlayers();
}
