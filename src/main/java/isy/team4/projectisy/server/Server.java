package isy.team4.projectisy.server;

import java.io.IOException;

public class Server extends ServerIO {
    // private String gameType; // reversi of tic-tac-toe
    // private boolean isPlaying = false;
    // private Player localPlayer;
    // private Player remotePlayer;
    private static Thread serverThread;
    private String response;
    private String ack = "";
    private String[] playerList;
    private String[] gameList;

    public Server(String ip, int port) throws IOException {
        super(ip, port);
        // TODO Auto-generated constructor stub
        openSocket();

        // Thread for the server
        serverThread = new Thread(() -> {
            while (true) // TODO: check if still connected
            {
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

                // OK
                if (responseParts[0].equals("OK")) {
                    ack = "OK";
                    continue;
                }

                // ERR
                if (responseParts[0].equals("ERR")) {
                    ack = "ERR";
                    continue;
                }

                // SRV
                if (responseParts[0].equals("SVR")) {
                    if (responseParts[1].equals("PLAYERLIST")) {

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

                    if (responseParts[1].equals("GAMELIST")) {

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

                    if (responseParts[1].equals("GAME")) {
                        if (responseParts[2].equals("MATCH")) {

                            // extract usefull data
                            String temp = response.replace("SVR GAME MATCH {", "");
                            temp = temp.replace("}", "");
                            temp = temp.replace("\"", "");
                            String[] tempArr = temp.split(", ");

                            // usefull data
                            String playerToMove = tempArr[0].replace("PLAYERTOMOVE: ", "");
                            String gameType = tempArr[1].replace("GAMETYPE: ", "");
                            String opponent = tempArr[2].replace("OPPONENT: ", "");

                            // signal that a game has been started

                            continue;
                        }

                        if (responseParts[2].equals("YOURTURN")) {
                            // signal ai to make a turn
                            continue;
                        }
                        if (responseParts[2].equals("MOVE")) {
                            // signal game that there as been a move

                            continue;
                        }
                        if (responseParts[2].equals("WIN") || responseParts[2].equals("DRAW")
                                || responseParts[2].equals("LOSS")) {

                            // signal the result of a game
                            continue;
                        }
                        if (responseParts[2].equals("CHALLENGE")) {
                            // signal user that they have been challenged
                            continue;
                        }

                    }
                }

            }
        });
        serverThread.start();
    }

    public boolean Requestlogin(String Username) {
        sendMessage("login " + Username);
        return handleAck();
    }

    public String[] getPlayerlist() throws Exception {
        playerList = null;
        sendMessage("get playerlist");
        if (handleAck()) {
            while (playerList == null) {
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            return playerList;
        } else {
            throw new Exception("acknowledgement getPlayerlist failed");
        }
    }

    public String[] getGamelist() throws Exception {
        gameList = null;
        sendMessage("get gamelist");
        if (handleAck()) {
            while (gameList == null) {
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            return gameList;
        } else {
            throw new Exception("acknowledgement getGameList failed");
        }
    }

    public boolean RequestSubscribeTictactoe() {
        sendMessage("subscribe tic-tac-toe");
        return handleAck();
    }

    public boolean RequestSubscribeReversi() {
        sendMessage("subscribe reversi");
        return handleAck();
    }

    public boolean RequestAcceptChallenge(int challengeNr) {
        sendMessage("challenge accept " + challengeNr);
        return handleAck();
    }

    public boolean RequestChallenge(String username, String gameType) {
        sendMessage("challenge " + username + " " + gameType);
        return handleAck();
    }

    private Boolean handleAck() {
        while (true) {
            if (ack.equals("OK")) {
                ack = "";
                return true;
            } else if (ack.equals("ERR")) {
                ack = "";
                return false;
            } else {
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    }
}
