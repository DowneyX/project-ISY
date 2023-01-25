package isy.team4.projectisy.controller;

import isy.team4.projectisy.model.game.IGameObserver;
import isy.team4.projectisy.model.game.RemoteGame;
import isy.team4.projectisy.model.player.AIPlayer;
import isy.team4.projectisy.model.rule.IRuleSet;
import isy.team4.projectisy.server.ServerProperties;

// TODO: CLI based controller, for faster ai vs server
public class CLIController implements IGameObserver {
    RemoteGame game;
    public CLIController(IRuleSet ruleSet, ServerProperties serverProperties) {
        this.game = new RemoteGame(new AIPlayer(serverProperties.userName), ruleSet, serverProperties);
    }

    public void start() {
        this.game.start();
    }

    public void startTournament() {
        this.game.startTournament();
    }

    @Override
    public void onStarted() {
        System.out.println("Game has started");
    }

    @Override
    public void onUpdate() {
        System.out.println(this.game.getBoard().toString());
    }

    @Override
    public void onFinished() {
        System.out.println(this.game.getResult().toString());
    }

    @Override
    public void onIllegal() {
        System.out.println("Illegal set has been done by: " + this.game.getCurrentPlayer().toString());
    }
}
