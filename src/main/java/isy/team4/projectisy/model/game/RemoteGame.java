package isy.team4.projectisy.model.game;

import isy.team4.projectisy.model.player.IPlayer;
import isy.team4.projectisy.model.rule.IRuleSet;

public class RemoteGame implements IGame {
    private final IPlayer player;
    private final IRuleSet ruleSet;
    private IPlayer remotePlayer;
    private IGameHandler gameHandler;

    /*
        TODO: See what info we need already, because we might already have a match at this point.
    */
    public RemoteGame(IPlayer player, IRuleSet ruleSet) {
        this.player = player;
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
