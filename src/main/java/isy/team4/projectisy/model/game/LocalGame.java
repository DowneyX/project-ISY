package isy.team4.projectisy.model.game;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import isy.team4.projectisy.model.player.IPlayer;
import isy.team4.projectisy.model.rule.IRuleSet;
import isy.team4.projectisy.observer.IObserver;
import isy.team4.projectisy.util.Board;
import isy.team4.projectisy.util.EResult;
import isy.team4.projectisy.util.Result;
import isy.team4.projectisy.util.Vector2D;

public class LocalGame implements IGame {
    private List<IObserver> observers = new ArrayList<>();
    private final IPlayer[] players;
    private final IRuleSet ruleSet;
    private IPlayer currentPlayer;
    private Board board;
    private boolean running;
    private Result result;

    public LocalGame(IPlayer[] players, IRuleSet ruleSet) {
        this.players = players;
        this.ruleSet = ruleSet;
        this.board = ruleSet.getStartingBoard();
        this.running = false;
        this.ruleSet.setBoard(board); // set so board is available on init
    }

    @Override
    public void start() throws IllegalArgumentException, ArrayIndexOutOfBoundsException {
        // Check if Player count is correct
        if ((this.ruleSet.getMinPlayerSize() != null && this.players.length < this.ruleSet.getMinPlayerSize())
                || (this.ruleSet.getMaxPlayerSize() != null && this.players.length > ruleSet.getMaxPlayerSize())) {
            throw new IllegalArgumentException(String.format(
                    "Player count needs to be between %o and %o",
                    this.ruleSet.getMinPlayerSize(),
                    this.ruleSet.getMaxPlayerSize()));
        }

        // Set initials to players
        char[] initials = this.ruleSet.getAllowedInitials();
        for (int i = 0; i < this.players.length; i++) {
            this.players[i].setInitial(initials[0]);

            initials[0] = initials[(i + 1) % initials.length];
        }

        // set current player
        currentPlayer = players[0];

        // Start thread and run the loop
        this.running = true;
        new Thread(() -> {
            while (this.running) {
                this.loop();
            }
            ;
        }).start();
    }

    @Override
    public void stop() {
        this.running = false;
    }

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

    public Result getResult() {
        return this.result;
    }

    private void rotateCurrentPlayer() {
        this.currentPlayer = players[(Arrays.asList(players).indexOf(currentPlayer) + 1) % this.players.length];
    }

    private void loop() throws ArrayIndexOutOfBoundsException {

        // check if current player can make a move if not pass turn
        if (ruleSet.isPass(currentPlayer)) {
            this.rotateCurrentPlayer();
            notifyObservers("het is " + currentPlayer.getName() + " z'n beurd");
            return;
        }

        // Get current player move and check if within bounds of board
        Vector2D move = this.currentPlayer.getMove(this.board);
        if (move.x >= board.getWidth() || move.y >= board.getHeight()) {
            throw new ArrayIndexOutOfBoundsException(
                    String.format("Player %s went out of bounds", this.currentPlayer.getName()));
        }

        // Check if set was legal by rules
        if (!this.ruleSet.isLegal(getCurrentPlayer(), move)) {
            notifyObservers("illegale zet");
            return; // Starts loop again
        }

        ruleSet.handleMove(move, currentPlayer);

        // If win or draw: set result, fire event and stop game
        if (this.ruleSet.isWon()) {
            this.result = new Result(EResult.WIN);
            this.result.setWinningPlayer(this.ruleSet.getWinningPlayer());

            notifyObservers(this.ruleSet.getWinningPlayer() + " heeft gewonnen");
            this.stop();
            return;
        }
        if (this.ruleSet.isDraw()) {
            this.result = new Result(EResult.DRAW);

            notifyObservers("gelijk gespeeld");
            this.stop();
            return;
        }

        // If game not ended, we continue on
        this.rotateCurrentPlayer();
        notifyObservers("het is " + currentPlayer.getName() + " z'n beurd");
    }

    public Vector2D[] getValidMoves(Board board) {
        return ruleSet.getValidMoves(getCurrentPlayer());
    }

    @Override
    public void registerObserver(IObserver o) {
        observers.add(o);
    }

    @Override
    public void removeObserver(IObserver o) {
        observers.remove(o);
    }

    @Override
    public void notifyObservers(String msg) {
        for (IObserver observer : observers) {
            observer.update(msg);
        }
    }
}
