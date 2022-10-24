package src.main;

import java.io.IOException;

public class Server {
    final static String ip = "localhost";
    final static int port = 7789;

    private String gameMode; // reversi of tic-tac-toe
    private boolean isPlaying = false;
    private String opponentName;
    private Player localPlayer;
    private Player remotePlayer;
    private static Thread serverThread;
    private static ServerIO connection;
    private String response;
    private String[] playerList;

    Server() {
        try {
            Server.connection = new ServerIO(ip, port);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        connection.openSocket();

        // Thread for the server
        serverThread = new Thread(() -> {
            while (true) // TODO: check if still connected
            {
                response = connection.onMessage();
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
                String[] responseParts = response.split(" ");

            }
        });
    }

    public boolean login(String Username) {
        connection.sendMessage("login " + Username);
        String reply = connection.onMessage();
        if (reply.equals("OK")) {
            serverThread.start();
            return true;
        }
        return false;
    }

    public void subscribeTictactoe() {
        connection.sendMessage("subscribe tic-tac-toe");
        gameMode = "tic-tac-toe";
    }

    public void subscribeReversi() {

    }

    public void showPlayers() {
        connection.sendMessage("get playerlist");
    }
}
