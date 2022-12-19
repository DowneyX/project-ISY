package isy.team4.projectisy.model.game;

import isy.team4.projectisy.model.player.IPlayer;
import isy.team4.projectisy.model.rule.IRuleSet;
import isy.team4.projectisy.util.EResult;
import isy.team4.projectisy.util.Result;
import isy.team4.projectisy.util.Vector2D;

public class LocalGame extends AGame implements IGame {

    public LocalGame(IPlayer[] players, IRuleSet ruleSet) {
        super(players, ruleSet);
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

        this.setupPlayers();

        // set current player
        this.currentPlayer = players[0];

        // Requesting starting board from RuleSet
        this.board = this.ruleSet.getStartingBoard();
        this.ruleSet.setBoard(this.getBoard());

        this.observers.forEach(IGameObserver::onStarted);

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

    private void loop() throws ArrayIndexOutOfBoundsException {
        // check if current player can make a move if not pass turn
        if (ruleSet.isPass(this.currentPlayer)) {
            this.rotateCurrentPlayer();
            this.observers.forEach(IGameObserver::onUpdate);
            return;
        }

        // Get current player move and check if within bounds of board
        Vector2D move = this.currentPlayer.getMove(this.board);
        if (move.x >= board.getWidth() || move.y >= board.getHeight()) {
            throw new ArrayIndexOutOfBoundsException(
                    String.format("Player %s went out of bounds", this.currentPlayer.getName()));
        }

        // Check if set was legal by rules
        if (!this.ruleSet.isLegal(this.getCurrentPlayer(), move)) {
            this.observers.forEach(IGameObserver::onIllegal);
            return; // Starts loop again
        }

        this.board = ruleSet.handleMove(move, currentPlayer);

        // Set the new board with potential changes that came from the move of the
        // player
        this.observers.forEach(IGameObserver::onUpdate);

        // If win or draw: set result, fire event and stop game
        if (this.ruleSet.isWon()) {
            this.result = new Result(EResult.WIN);
            this.result.setWinningPlayer(this.ruleSet.getWinningPlayer());

            this.observers.forEach(IGameObserver::onFinished);
            this.stop();
            return;
        }
        if (this.ruleSet.isDraw()) {
            this.result = new Result(EResult.DRAW);

            this.observers.forEach(IGameObserver::onFinished);
            this.stop();
            return;
        }

        // If game not ended, we continue on
        this.rotateCurrentPlayer();
        this.observers.forEach(IGameObserver::onUpdate);
    }
}
