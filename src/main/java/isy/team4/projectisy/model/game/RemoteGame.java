package isy.team4.projectisy.model.game;

import java.util.Arrays;

import isy.team4.projectisy.model.player.IPlayer;
import isy.team4.projectisy.model.player.RemotePlayer;
import isy.team4.projectisy.model.rule.IRuleSet;
import isy.team4.projectisy.server.Server;
import isy.team4.projectisy.util.Board;
import isy.team4.projectisy.util.Result;
import isy.team4.projectisy.util.Vector2D;

public class RemoteGame implements IGame {
    private final IPlayer[] players;
    private final IRuleSet ruleSet;
    private IGameHandler gameHandler;
    private IPlayer currentPlayer;
    private Board board;
    private boolean running;
    private Result result;
    private Server server;

    /*
     * TODO: See what info we need already, because we might already have a match at
     * this point.
     */
    public RemoteGame(IPlayer[] players, IRuleSet ruleSet, Server server) {
        this.players = players;
        this.ruleSet = ruleSet;
        this.board = ruleSet.getStartingBoard();
        this.running = false;
        this.server = server;
    }

    @Override
    public void start() {
        // TODO: start game
        this.running = true;
    }

    @Override
    public void stop() {
        // TODO: stop game
        this.running = false;
    }

    @Override
    public Board getBoard() {
        return board;
    }

    @Override
    public IPlayer getCurrentPlayer() {
        return currentPlayer;
    }

    @Override
    public IPlayer[] getPlayers() {
        return players;
    }

    @Override
    public Result getResult() {
        return result;
    }

    public void setGameHandler(IGameHandler gameHandler) {
        this.gameHandler = gameHandler;
    }

    public void setCurrentPlayer(IPlayer currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    public void step() {

        Vector2D move = getCurrentPlayer().getMove(board);
        board.setElement(getCurrentPlayer(), move.x, move.y);
        if (!(currentPlayer instanceof RemotePlayer)) {
            server.sendMove(move);
        }
        this.rotateCurrentPlayer();
    }

    private void rotateCurrentPlayer() {
        this.currentPlayer = players[(Arrays.asList(players).indexOf(currentPlayer) + 1) % this.players.length];
    }

}
