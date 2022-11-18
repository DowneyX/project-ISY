package isy.team4.projectisy.model.game;

import isy.team4.projectisy.model.player.IPlayer;
import isy.team4.projectisy.model.rule.IRuleSet;
import isy.team4.projectisy.util.Board;
import isy.team4.projectisy.util.EResult;
import isy.team4.projectisy.util.Result;
import isy.team4.projectisy.util.Vector2D;

public class LocalGame implements IGame {
    private final IPlayer[] players;
    private final IRuleSet ruleSet;
    private IGameHandler gameHandler;
    private IPlayer currentPlayer;
    private int currentTurn;
    private Board board;
    private boolean running;
    private Result result;

    public LocalGame(IPlayer[] players, IRuleSet ruleSet) {
        this.players = players;
        this.ruleSet = ruleSet;
        this.board = ruleSet.getStartingBoard();
        this.running = false;
        this.ruleSet.setTurn(this.board, null); // set so board is available on init
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

        // Initializing turn
        this.currentTurn = 0;
        this.rotateCurrentPlayer(); // Initially sets player

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

    public void setGameHandler(IGameHandler gameHandler) {
        this.gameHandler = gameHandler;
    }

    private void rotateCurrentPlayer() {
        this.currentPlayer = this.players[this.currentTurn % this.players.length];
    }

    private void loop() throws ArrayIndexOutOfBoundsException {
        // Get current player move and check if within bounds of board
        Vector2D move = this.currentPlayer.getMove(this.board);
        if (move.x >= board.getWidth() || move.y >= board.getHeight()) {
            throw new ArrayIndexOutOfBoundsException(
                    String.format("Player %s went out of bounds", this.currentPlayer.getName()));
        }

        // Make copy of board to temp set the move (this is to check it with the
        // ruleset)
        Board newBoard = new Board(board); // Using deep copy now

        newBoard.setElement(this.currentPlayer, move.x, move.y);

        this.ruleSet.setTurn(this.board, newBoard);

        // Check if set was legal by rules
        if (!this.ruleSet.isLegal()) {
            this.gameHandler.onIllegal();
            return; // Starts loop again
        }

        // Set the new board with potential changes that came from the move of the
        // player
        Board handledBoard = this.ruleSet.handleBoard(newBoard);
        this.board = (handledBoard != null) ? handledBoard : newBoard;
        this.gameHandler.onUpdate();

        // If win or draw: set result, fire event and stop game
        if (this.ruleSet.isWon()) {
            this.result = new Result(EResult.WIN);
            this.result.setWinningPlayer(this.ruleSet.getWinningPlayer());

            this.gameHandler.onFinished();
            this.stop();
            return;
        }
        if (this.ruleSet.isDraw()) {
            this.result = new Result(EResult.DRAW);

            this.gameHandler.onFinished();
            this.stop();
            return;
        }

        // If game not ended, we continue on
        this.currentTurn++;
        this.rotateCurrentPlayer();
    }

    public int[] getValidMoves() {
        return ruleSet.getValidMoves();
    }
}
