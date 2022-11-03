package isy.team4.projectisy.model.game;

import isy.team4.projectisy.model.player.IPlayer;
import isy.team4.projectisy.model.rule.IRuleSet;

public class LocalGame implements IGame {
    private final IPlayer[] players;
    private final IRuleSet ruleSet;
    private IGameHandler gameHandler;

    public LocalGame(IPlayer[] players, IRuleSet ruleSet) {
        this.players = players;
        this.ruleSet = ruleSet;
    }

    @Override
    public void start() {
        // TODO: start game
    }

    @Override
    public void stop() {
        // TODO: stop game
    }

    @Override
    public IPlayer getCurrentPlayer() {
        // TODO
        return null;
    }

    @Override
    public IPlayer[] getPlayers() {
        // TODO
        return new IPlayer[0];
    }

    public void setGameHandler(IGameHandler gameHandler) {
        this.gameHandler = gameHandler;
    }
}
