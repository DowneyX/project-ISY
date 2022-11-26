package isy.team4.projectisy.server;

import java.io.IOException;
import isy.team4.projectisy.util.Board;
import isy.team4.projectisy.util.Vector2D;
import isy.team4.projectisy.model.game.RemoteGame;
import isy.team4.projectisy.model.player.*;
import isy.team4.projectisy.model.rule.*;

public class Server extends ServerIO implements IPlayerTurnHandler {
    private String userName;
    public RemoteGame game;
    private static Thread serverThread;
    private String response;
    private String[] playerList;
    private String[] gameList;
    private Vector2D RemoteMove;

    public Server(String ip, int port) throws IOException {
        super(ip, port);
        openSocket();

        // Thread for the server
        serverThread = new Thread(() -> {
            while (true) {
                response = onMessage(); // wait for a message
                System.out.println(response); // we have recieved a message

                if (response.contains("OK")) { // OK all is good
                    continue;
                }

                if (response.contains("ERR")) { // ERR something bad happened
                    try {
                        throw new Exception(response);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                if (response.contains("SVR GAMELIST")) { // update game list
                    // extract usefull data
                    String temp = response.replace("SVR GAMELIST [", "");
                    temp = temp.replace("]", "");
                    temp = temp.replace("\"", "");

                    // usefull data
                    String[] gameList = temp.split(", ");

                    // send gamelist somewhere
                    this.gameList = gameList;
                    continue;
                }

                if (response.contains("SVR PLAYERLIST")) { // update playerlist
                    // extract usefull data
                    String temp = response.replace("SVR PLAYERLIST [", "");
                    temp = temp.replace("]", "");
                    temp = temp.replace("\"", "");

                    // usefull data
                    String[] playerList = temp.split(", ");

                    // send playerlist somewhere
                    this.playerList = playerList;
                    continue;
                }

                if (response.contains("SVR GAME MATCH")) { // a match has started
                    // extract usefull data
                    String[] stringsToRemove = { "SVR GAME MATCH {", "}", "\"", "PLAYERTOMOVE: ", "GAMETYPE: ",
                            "OPPONENT: " };
                    String temp = response;
                    for (String txt : stringsToRemove) {
                        temp = temp.replace(txt, "");
                    }
                    String[] data = temp.split(", ");
                    // playerToMove = data[0]; gameType = data[1]; opponentName = data[2]

                    // setup game
                    setupGame(data[1], data[2], data[0]);
                    continue;
                }

                if (response.contains("SVR GAME MOVE")) { // a move has been made (us or them)
                    // extract usefull data
                    String[] stringsToRemove = { "SVR GAME MOVE {", "}", "\"", "PLAYER: ", "DETAILS: ",
                            "MOVE: " };
                    String temp = response;
                    for (String txt : stringsToRemove) {
                        temp = temp.replace(txt, "");
                    }
                    String[] data = temp.split(", ");
                    // playerName = data[0]; moveInt = data[1]; details = data[2]

                    // insert move
                    if (!data[0].equals(userName)) {
                        RemoteMove = new Vector2D(Integer.parseInt(data[1]), game.getBoard());
                        game.step();
                    }

                    continue;
                }

                if (response.contains("SVR GAME WIN")) { // won a game
                    cleanUp();
                    continue;
                }

                if (response.contains("SVR GAME LOSS")) { // lost a game
                    cleanUp();
                    continue;
                }

                if (response.contains("SVR GAME DRAW")) { // draw a game
                    cleanUp();
                    continue;
                }

                if (response.contains("SVR GAME YOURTURN")) { // its our turn make a move
                    game.step();
                    continue;
                }

                if (response.contains("SVR GAME CHALLENGE")) { // we have a challenge request

                    continue;
                }

            }
        });
        serverThread.start();
    }

    public void login(String username) {
        sendMessage("login " + username);
        this.userName = username;
    }

    public String[] getPlayerlist() {
        sendMessage("get playerlist");
        return playerList;
    }

    public String[] getGamelist() {
        return gameList;
    }

    public void updatePlayerList() {
        sendMessage("get playerlist");
    }

    public void updateGameList() {
        sendMessage("get gamelist");
    }

    public void subscribeTictactoe() {
        sendMessage("subscribe tic-tac-toe");
    }

    public void subscribeReversi() {
        sendMessage("subscribe reversi");
    }

    public void acceptChallenge(int challengeNr) {
        sendMessage("challenge accept " + challengeNr);
    }

    public void sendChallenge(String username, String gameType) {
        sendMessage("challenge " + username + " " + gameType);
    }

    private void setupGame(String gametype, String opponentName, String playerToMove) {
        switch (gametype) {
            case "Tic-tac-toe":
                IRuleSet ruleset = new TicTacToeRuleSet();
                IPlayer[] players = new IPlayer[2];
                players[0] = new RemotePlayer(opponentName, this);
                players[1] = new AIPlayer(userName, players[0], ruleset);
                ruleset = new TicTacToeRuleSet();
                game = new RemoteGame(players, ruleset, this);
                IPlayer currentPlayer = playerToMove.equals(opponentName) ? players[0] : players[1];
                game.setCurrentPlayer(currentPlayer);
                game.start();

                break;
            case "Reversi":

                break;

            default:
                break;
        }
    }

    private void cleanUp() {
        game.stop();
        game = null;
    }

    public void sendMove(Vector2D move) {
        sendMessage("move " + move.toInt(game.getBoard()));
    }

    @Override
    public Vector2D getPlayerMove() {
        return RemoteMove;
    }

}