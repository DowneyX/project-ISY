package isy.team4.projectisy.model.game;

import java.util.Objects;

import isy.team4.projectisy.model.player.IPlayer;
import isy.team4.projectisy.model.player.RemotePlayer;
import isy.team4.projectisy.model.rule.IRuleSet;
import isy.team4.projectisy.model.rule.TicTacToeRuleSet;
import isy.team4.projectisy.observer.IServerObserver;
import isy.team4.projectisy.server.*;
import isy.team4.projectisy.util.EResult;
import isy.team4.projectisy.util.Result;
import isy.team4.projectisy.util.Vector2D;

public class RemoteGame extends AGame implements IGame, IServerObserver {
    private final Server server;

    public RemoteGame(IPlayer player, IRuleSet ruleSet, ServerProperties serverProperties) {
        super(new IPlayer[]{player}, ruleSet);
        this.server = new Server(serverProperties);
        this.server.addObserver(this);
    }

    @Override
    public void start() {
        // Start server and subscribe
        this.server.start();
        this.running = true;
        this.server.login();
        this.server.subscribe(this.ruleSet instanceof TicTacToeRuleSet ? "tic-tac-toe" : "reversi");  // TODO: of othello?
    }

    public void startTournament() {
        // Start server
        this.server.start();
        this.running = true;
        this.server.login();
    }

    @Override
    public void stop() {
        this.running = false;
        this.server.stop();
    }

    @Override
    public void onGameList(String[] games) {
        // pass
    }

    @Override
    public void onPlayerList(String[] players) {
        // pass
    }

    @Override
    public void onGameMatch(GameMatch gameMatch) {
        // Set opponent(s)
        IPlayer opponent = new RemotePlayer(gameMatch.opponent);
        IPlayer[] newPlayers = new IPlayer[2];

        if(Objects.equals(gameMatch.playerToMove, gameMatch.opponent)) {
            newPlayers[0] = opponent;
            newPlayers[1] = getLocalPlayer();
        } else {
            newPlayers[0] = getLocalPlayer();
            newPlayers[1] = opponent;
        }

        this.players = newPlayers;

        // Setup players
        this.setupPlayers();

        // not needed?
        // this.currentPlayer = this.players[0];

        // Requesting starting board from RuleSet
        this.board = this.ruleSet.getStartingBoard();
        this.ruleSet.setBoard(this.getBoard());

        this.observers.forEach(IGameObserver::onStarted);
    }

    @Override
    public void onGameMove(GameMove gameMove) {
        // Making sure that assumptions were correct, so setting current player again first
        this.currentPlayer = this.getPlayerFromName(gameMove.playerName);

        // Own handling, because no way to request board from server
        this.ruleSet.setBoard(this.getBoard());
        this.board = this.ruleSet.handleMove(new Vector2D(gameMove.move, this.getBoard()), this.getCurrentPlayer());

        this.rotateCurrentPlayer();  // Assumption

        this.observers.forEach(IGameObserver::onUpdate);
    }

    @Override
    public void onFinished(ServerResult result) {
        this.result = new Result(result.result);

        if (result.scoreP1 > result.scoreP2) {  // If p1 won, p1
            this.result.setWinningPlayer(this.players[0]);
        } else if (result.scoreP1 < result.scoreP2) {  // If p2 won, assert it's any but p1
            this.result.setWinningPlayer(this.players[1]);
        } else if (result.result != EResult.DRAW) {  // If no draw, illegal move has been done
            IPlayer tempPlayer = this.getCurrentPlayer();
            this.rotateCurrentPlayer();  // Assuming other player won
            this.result.setWinningPlayer(this.getCurrentPlayer());
            this.currentPlayer = tempPlayer;  // Set back to default
        }

        this.observers.forEach(IGameObserver::onFinished);
    }

    @Override
    public void onMove() {
        // Could be getCurrentPlayer(), but we assume it, so could better hardcode it
//        Vector2D move = this.getLocalPlayer().getMove(this.getBoard());
        Vector2D move = this.getLocalPlayer().getMove(this.getBoard());
        this.server.sendMove(move.toInt(this.getBoard()));
    }

    private IPlayer getPlayerFromName(String name) {
        for (IPlayer player : this.getPlayers()) {
            if (Objects.equals(player.getName(), name)) {
                return player;
            }
        }

        return null;
    }

    // "Local" == not RemotePlayer
    private IPlayer getLocalPlayer() {
        for (IPlayer player : this.getPlayers()) {
            if (!(player instanceof RemotePlayer)) {
                return player;
            }
        }

        throw new RuntimeException("Only remote players exist");
    }
}
