package isy.team4.projectisy.server;

import java.io.IOException;
import java.util.Arrays;

import isy.team4.projectisy.player.*;
import isy.team4.projectisy.util.Board;

public class Server extends ServerIO {
    private char symbol;
    private Board board;
    private String userName;
    private String gameType; // reversi or tic-tac-toe
    public boolean isPlaying = false;
    private Player playerLocal;
    private PlayerRemote PlayerRemote; // technically dont need a remote player but its nice to have for the name
    private static Thread serverThread;
    private String response;
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
                    continue;
                }

                // ERR
                if (responseParts[0].equals("ERR")) {
                    try {
                        throw new Exception(response);
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
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
                            gameType = tempArr[1].replace("GAMETYPE: ", "");
                            String opponent = tempArr[2].replace("OPPONENT: ", "");

                            // signal that a game has been started
                            if (gameType.equals("Tic-tac-toe")) {

                                if (playerToMove.equals(userName)) {
                                    playerLocal = new PlayerSmartTictactoe(userName, 'X');
                                    PlayerRemote = new PlayerRemote(opponent, 'O');
                                } else {
                                    playerLocal = new PlayerSmartTictactoe(userName, 'O');
                                    PlayerRemote = new PlayerRemote(opponent, 'X');
                                }
                                symbol = 'X';
                                isPlaying = true;
                                board = new Board(3, 3);
                                playerLocal.setBoard(board);

                            }
                            continue;
                        }

                        if (responseParts[2].equals("YOURTURN")) {
                            // signal ai to make a turn

                            if (gameType.equals("Tic-tac-toe")) {
                                int[] move = playerLocal.getMove();
                                int moveInt = convertXYToInt(move, 3);
                                requestMove(moveInt);
                            }
                            continue;
                        }
                        if (responseParts[2].equals("MOVE")) {
                            // signal game that there as been a move
                            String temp = response.replace("SVR GAME MOVE {", "");
                            temp = temp.replace("}", "");
                            temp = temp.replace("\"", "");
                            temp = temp.replace("PLAYER: ", "");
                            temp = temp.replace("DETAILS: ", "");
                            temp = temp.replace("MOVE: ", "");

                            String[] tempArr = temp.split(", ");
                            // String playerName = tempArr[0];
                            int moveInt = Integer.parseInt(tempArr[1]);

                            if (gameType.equals("Tic-tac-toe")) {
                                int[] move = convertIntToXY(moveInt, 3);
                                System.out.println(Arrays.toString(move));
                                board.setElement(move[0], move[1], symbol);
                                symbol = symbol == 'X' ? 'O' : 'X';
                                System.out.println(board.toString());
                            }

                            continue;
                        }
                        if (responseParts[2].equals("WIN") || responseParts[2].equals("DRAW")
                                || responseParts[2].equals("LOSS")) {

                            isPlaying = false;
                            gameType = null;
                            playerLocal = null;
                            PlayerRemote = null;
                            board = null;

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

    public void Requestlogin(String username) {
        sendMessage("login " + username);
        this.userName = username;
    }

    public String[] getPlayerlist() throws Exception {
        playerList = null;
        sendMessage("get playerlist");
        while (playerList == null) {
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return playerList;
    }

    public String[] getGamelist() throws Exception {
        gameList = null;
        sendMessage("get gamelist");

        while (gameList == null) {
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return gameList;
    }

    public void requestSubscribeTictactoe() {
        sendMessage("subscribe tic-tac-toe");
    }

    public void requestSubscribeReversi() {
        sendMessage("subscribe reversi");
    }

    public void RequestAcceptChallenge(int challengeNr) {
        sendMessage("challenge accept " + challengeNr);
    }

    public void requestChallenge(String username, String gameType) {
        sendMessage("challenge " + username + " " + gameType);
    }

    public void requestMove(int move) {
        sendMessage("move " + move);
    }

    public int convertXYToInt(int[] move, int lenght) {
        return move[0] * lenght + move[1];
    }

    public int[] convertIntToXY(int incommingMove, int lenght) {
        int y = (incommingMove) % lenght;
        int x = (incommingMove) / lenght;
        int[] move = { x, y };
        return move;
    }
}
