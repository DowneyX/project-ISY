package isy.team4.projectisy.model.game;

import isy.team4.projectisy.model.player.AIPlayer;
import isy.team4.projectisy.model.player.IPlayer;
import isy.team4.projectisy.model.rule.IRuleSet;
import isy.team4.projectisy.util.Board;
import isy.team4.projectisy.util.Result;
import isy.team4.projectisy.util.Vector2D;

import java.util.ArrayList;
import java.util.Arrays;

public abstract class AGame implements IGame {
    protected final ArrayList<IGameObserver> observers = new ArrayList<>();
    protected final IRuleSet ruleSet;
    protected IPlayer[] players;
    protected IPlayer currentPlayer = null;
    protected Board board = null;
    protected boolean running = false;
    protected Result result = null;

    public AGame(IPlayer[] players, IRuleSet ruleSet) {
        this.players = players;
        this.ruleSet = ruleSet;
//        this.board = ruleSet.getStartingBoard();
        this.running = false;
//        this.ruleSet.setBoard(board); // set so board is available on init
    }

    @Override
    public boolean isRunning() {
        return this.running;
    }

    @Override
    public Board getBoard() {
        return this.board;
    }

    @Override
    public IPlayer getCurrentPlayer() {
        return this.currentPlayer;
    }

    @Override
    public IPlayer[] getPlayers() {
        return this.players;
    }

    @Override
    public Result getResult() {
        return this.result;
    }

    @Override
    public Vector2D[] getValidMoves() {
        this.ruleSet.setBoard(this.getBoard());
        return this.ruleSet.getValidMoves(this.getCurrentPlayer());
    }

    @Override
    public void addObserver(IGameObserver o) {
        this.observers.add(o);
    }

    @Override
    public void removeObserver(IGameObserver o) {
        this.observers.add(o);
    }

    protected void setupPlayers() {
        // Set initials to players
        char[] initials = this.ruleSet.getAllowedInitials();
        char initial = initials[0];
        for (int i = 0; i < this.players.length; i++) {
            this.players[i].setInitial(initial);
            initial = initials[(i + 1) % initials.length];
        }

        // Set players to RuleSet
        this.ruleSet.setPlayers(this.getPlayers());

        // Setup AI player
        for (int i = 0; i < this.players.length; i++) {
            if (this.players[i] instanceof AIPlayer) {
                ((AIPlayer)this.players[i]).setOpponent(this.players[(i + 1) % this.players.length]);
                ((AIPlayer)this.players[i]).setRuleSet(this.ruleSet);
            }
        }
    }

    protected void rotateCurrentPlayer() {
        this.currentPlayer = this.players[
                (Arrays.asList(this.players).indexOf(this.currentPlayer) + 1) % this.players.length];
    }
}
