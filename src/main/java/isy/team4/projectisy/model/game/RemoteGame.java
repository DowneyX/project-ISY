package isy.team4.projectisy.model.game;

import java.util.ArrayList;

import isy.team4.projectisy.model.player.IPlayer;
import isy.team4.projectisy.model.player.RemotePlayer;
import isy.team4.projectisy.model.rule.IRuleSet;
import isy.team4.projectisy.model.rule.TicTacToeRuleSet;
import isy.team4.projectisy.observer.IObservable;
import isy.team4.projectisy.server.Server;
import isy.team4.projectisy.util.Board;
import isy.team4.projectisy.util.Result;
import isy.team4.projectisy.util.Vector2D;

public class RemoteGame implements IGame {
    private final IPlayer player;
    private final ArrayList<IGameObserver> observers = new ArrayList<>();
    private final IRuleSet ruleSet;
    private IPlayer opponent;
    private IPlayer currentPlayer;
    private Board board;
    private boolean running = false;
    private Result result;
    private Server server;

    /*
     * TODO: See what info we need already, because we might already have a match at
     * this point.
     */
    public RemoteGame(IPlayer player, IRuleSet ruleSet) {
        this.player = player;
        this.ruleSet = ruleSet;
        this.board = ruleSet.getStartingBoard();
        this.server = server;
    }

    @Override
    public void start() {
        // TODO: start game
        this.running = true;
    }

    public void startTournament() {
        this.server.start();
        this.server.subscribe(this.ruleSet instanceof TicTacToeRuleSet ? "tic-tac-toe" : "reversi");  // TODO: of othello?
    }

    @Override
    public void stop() {
        this.running = false;
        this.server.stop();
    }

    @Override
    public boolean isRunning() {
        return this.running;
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
        return new IPlayer[]{this.player, this.opponent};
    }

    @Override
    public Result getResult() {
        return result;
    }

    public void loop() {
        Vector2D move = getCurrentPlayer().getMove(board);
        board.setElement(getCurrentPlayer(), move.x, move.y);
        if (!(currentPlayer instanceof RemotePlayer)) {
            server.sendMove(move);
        }
    }

    public Vector2D[] getValidMoves(Board board) {
        return ruleSet.getValidMoves(getCurrentPlayer());
    }

    @Override
    public void addObserver(IGameObserver o) {
        this.observers.add(o);
    }

    @Override
    public void removeObserver(IGameObserver o) {
        this.observers.remove(o);
    }
}
