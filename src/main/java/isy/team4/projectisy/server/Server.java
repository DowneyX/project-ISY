package isy.team4.projectisy.server;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import isy.team4.projectisy.observer.IObservable;
import isy.team4.projectisy.observer.IServerObserver;
import isy.team4.projectisy.util.EResult;
import isy.team4.projectisy.util.Result;

public class Server extends ServerIO implements IObservable<IServerObserver> {
    private final ArrayList<IServerObserver> observers = new ArrayList<>();
    private final HashMap<String, Runnable> responseMapper = new HashMap<>();
    private String response;
    private boolean running = false;

    public Server(String ip, int port) throws IOException {
        super(ip, port);
        this.populateResponseMapper();
        this.openSocket();
    }

    public void start() {
        // Start thread and run the loop
        this.running = true;
        new Thread(() -> {
            while (this.running) {
                this.loop();
            };
        }).start();
    }

    public void stop() {
        this.running = false;
    }

    public void login(String username) {
        sendMessage("login " + username);
    }

    public void updatePlayerList() {
        sendMessage("get playerlist");
    }

    public void updateGameList() {
        sendMessage("get gamelist");
    }

    public void subscribe(String gameType) {
        sendMessage("subscribe " + gameType);
    }

    public void acceptChallenge(int challengeNr) {
        sendMessage("challenge accept " + challengeNr);
    }

    public void sendChallenge(String username, String gameType) {
        sendMessage("challenge " + username + " " + gameType);
    }

    public void sendMove(Integer move) {
        sendMessage("move " + move);
    }

    @Override
    public void addObserver(IServerObserver o) {
        observers.add(o);
    }

    @Override
    public void removeObserver(IServerObserver o) {
        observers.remove(o);
    }

    private void loop() {
        response = onMessage();

        if (response == null) {
            return;
        }

        for (String key : responseMapper.keySet()) {
            if (response.startsWith(key)) {
                responseMapper.get(key).run();
                return;
            }
        }
        // TODO: Throw or not?
//        throw new IllegalArgumentException("No matching case found");
    }

    private void populateResponseMapper() {
        this.responseMapper.put("OK", () -> {});
        this.responseMapper.put("ERR", () -> {  // TODO: Be sure userinput cannot kill us here. Maybe use this as tactic
            System.out.println(this.response);
            this.stop();
        });
        this.responseMapper.put("SVR GAMELIST", () -> {
            this.observers.forEach((observer) -> observer.setGameList(this.parseGameList()));
        });
        this.responseMapper.put("SVR PLAYERLIST", () -> {
            this.observers.forEach((observer) -> observer.setPlayerList(this.parsePlayerList()));
        });
        this.responseMapper.put("SVR GAME MATCH", () -> {
            this.observers.forEach((observer) -> observer.onGameMatch(this.parseGameMatch()));
        });
        this.responseMapper.put("SVR GAME MOVE", () -> {
            this.observers.forEach((observer) -> observer.onGameMove(this.parseGameMove()));
        });
        this.responseMapper.put("SVR GAME WIN", () -> {
            this.observers.forEach((observer) -> observer.onFinished(new Result(EResult.WIN)));
        });this.responseMapper.put("SVR GAME LOSS", () -> {
            this.observers.forEach((observer) -> observer.onFinished(new Result(EResult.LOSS)));
        });
        this.responseMapper.put("SVR GAME DRAW", () -> {
            this.observers.forEach((observer) -> observer.onFinished(new Result(EResult.DRAW)));
        });
        this.responseMapper.put("SVR GAME YOURTURN", () -> {
            this.observers.forEach(IServerObserver::onMove);
        });
        this.responseMapper.put("SVR GAME CHALLENGE", () -> {});  // TODO
    }

    private String[] parseGameList() {
        String temp = response.replace("SVR GAMELIST [", "");
        temp = temp.replace("]", "");
        temp = temp.replace("\"", "");

        return temp.split(", ");
    }

    private String[] parsePlayerList() {
        String temp = response.replace("SVR PLAYERLIST [", "");
        temp = temp.replace("]", "");
        temp = temp.replace("\"", "");

        return temp.split(", ");
    }

    private GameMatch parseGameMatch() {
        String[] stringsToRemove = { "SVR GAME MATCH {", "}", "\"", "PLAYERTOMOVE: ", "GAMETYPE: ",
                "OPPONENT: " };
        String temp = response;
        for (String txt : stringsToRemove) {
            temp = temp.replace(txt, "");
        }
        String[] data = temp.split(", ");
        // playerToMove = data[0]; gameType = data[1]; opponentName = data[2]

        return new GameMatch(data[0], data[1], data[2]);
    }

    private GameMove parseGameMove() {
        String[] stringsToRemove = { "SVR GAME MOVE {", "}", "\"", "PLAYER: ", "DETAILS: ",
                "MOVE: " };
        String temp = response;
        for (String txt : stringsToRemove) {
            temp = temp.replace(txt, "");
        }
        String[] data = temp.split(", ");
        // playerName = data[0]; moveInt = data[1]; details = data[2]

        return new GameMove(data[0], Integer.parseInt(data[1]), data[2]);
    }
}
