package isy.team4.projectisy.server;

import java.io.IOException;
import isy.team4.projectisy.util.Board;
import isy.team4.projectisy.util.Vector2D;
import isy.team4.projectisy.model.player.*;;

public class Server extends ServerIO {
    private IPlayer playerWithTurn;
    private Board board;
    private String userName;
    private String gameType; // reversi or tic-tac-toe
    public boolean isPlaying = false;
    private AIPlayer localPlayer;
    private AIPlayer remotePlayer; // technically dont need a remotePlayer
    private static Thread serverThread;
    private String response;
    private String[] playerList;
    private String[] gameList;
    private String lastGameResult;

    public Server(String ip, int port) throws IOException {
        super(ip, port);
        openSocket();

        // Thread for the server
        serverThread = new Thread(() -> {
            while (true) {
                response = onMessage();
                if (response == null) {
                    // Nothing yet, sleep 0.2 sec and continue
                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        // Sleep interrupted, ignore
                    }
                    continue;
                }
                // We got a response! Parse and handle
                System.out.println(response);
                String[] responseParts = response.split(" ");

                if (responseParts[0].equals("OK")) { // OK all is good
                    continue;
                }

                if (responseParts[0].equals("ERR")) { // ERR something bad happened
                    try {
                        throw new Exception(response);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                if (responseParts[0].equals("SVR")) { // SVR server signal

                    if (responseParts[1].equals("PLAYERLIST")) { // playerlist has been requested

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
                    if (responseParts[1].equals("GAMELIST")) { // gamelist has been requested

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
                    if (responseParts[1].equals("GAME")) { // something game related happend
                        if (responseParts[2].equals("MATCH")) { // game started

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
                            if (data[1].equals("Tic-tac-toe")) {
                                setupTictactoe(data[2], data[0]);
                            }
                            continue;
                        }
                        if (responseParts[2].equals("YOURTURN")) { // its our turn

                            // make a move
                            if (gameType.equals("Tic-tac-toe")) {
                                makeMoveTictacToe();
                            }
                            continue;
                        }
                        if (responseParts[2].equals("MOVE")) { // a move has been made (both us or them)

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
                            insertMove(Integer.parseInt(data[1]));
                            continue;
                        }
                        if (responseParts[2].equals("WIN") || responseParts[2].equals("DRAW")
                                || responseParts[2].equals("LOSS")) { // the game has ended

                            lastGameResult = responseParts[2];
                            cleanUp();
                            continue;
                        }
                        if (responseParts[2].equals("CHALLENGE")) { // a player has challenged us
                            // signal user that they have been challenged
                            continue;
                        }

                    }
                }
            }
        });
        serverThread.start();
    }

    public void Requestlogin(String username) {
        sendMessage("login " + username);
        this.userName = username;
    }

    public String[] getPlayerlist() {
        playerList = null;
        sendMessage("get playerlist");
        while (playerList == null) {
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return playerList;
    }

    public String[] getGamelist() {
        gameList = null;
        sendMessage("get gamelist");

        while (gameList == null) {
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return gameList;
    }

    public Board getBoard() {
        return board;
    }

    public String getLastGameResult() {
        return lastGameResult;
    }

    public void requestSubscribeTictactoe() {
        sendMessage("subscribe tic-tac-toe");
    }

    public void requestSubscribeReversi() {
        sendMessage("subscribe reversi");
    }

    public void requestAcceptChallenge(int challengeNr) {
        sendMessage("challenge accept " + challengeNr);
    }

    public void requestChallenge(String username, String gameType) {
        sendMessage("challenge " + username + " " + gameType);
    }

    public void requestMove(int move) {
        sendMessage("move " + move);
    }

    public int convertVector2DToInt(Vector2D move, int lenght) {
        return move.y * lenght + move.x;
    }

    public Vector2D convertIntToVector2D(int incommingMove, int length) {
        int y = (incommingMove) / length;
        int x = (incommingMove) % length;
        Vector2D move = new Vector2D(x, y);
        return move;
    }

    private void setupTictactoe(String opponentName, String playerToMove) {
        gameType = "Tic-tac-toe";
        localPlayer = new AIPlayer(userName);
        remotePlayer = new AIPlayer(opponentName);
        playerWithTurn = playerToMove.equals(userName) ? localPlayer : remotePlayer;
        isPlaying = true;
        board = new Board(3, 3);
    }

    private void cleanUp() {
        isPlaying = false;
        gameType = null;
        localPlayer = null;
        remotePlayer = null;
        board = null;
        playerWithTurn = null;
    }

    private void makeMoveTictacToe() {
        Vector2D move = localPlayer.getMove(board, remotePlayer);
        int moveInt = convertVector2DToInt(move, 3);
        requestMove(moveInt);
    }

    private void insertMove(int moveInt) {
        Vector2D move = convertIntToVector2D(moveInt, board.getWidth());
        board.setElement(playerWithTurn, move.x, move.y);
        playerWithTurn = playerWithTurn == localPlayer ? remotePlayer : localPlayer;
    }
}