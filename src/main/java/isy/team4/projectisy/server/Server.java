package isy.team4.projectisy.server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Pattern;

import isy.team4.projectisy.observer.IObservable;
import isy.team4.projectisy.observer.IServerObserver;
import isy.team4.projectisy.util.EResult;

public class Server extends ServerIO implements IObservable<IServerObserver> {
    private final ServerProperties serverProperties;

    private final ArrayList<IServerObserver> observers = new ArrayList<>();
    private final HashMap<String, Runnable> responseMapper = new HashMap<>();
    private String response;
    private boolean running = false;

    public Server(ServerProperties serverProperties) {
        super(serverProperties.ip, serverProperties.port);
        this.serverProperties = serverProperties;
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

    public void login() {
        System.out.println(this.serverProperties.userName);
        sendMessage("login " + this.serverProperties.userName);
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
        this.response = this.onMessage();

        if (response == null) {
            return;
        }

        System.out.println(this.response);

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
            this.observers.forEach((observer) -> observer.onGameList(this.parseResponse()));
        });
        this.responseMapper.put("SVR PLAYERLIST", () -> {
            this.observers.forEach((observer) -> observer.onPlayerList(this.parseResponse()));
        });
        this.responseMapper.put("SVR GAME MATCH", () -> {
            // playerToMove = data[0]; gameType = data[1]; opponentName = data[2]
            String[] data = this.parseResponse();
            this.observers.forEach((observer) -> observer.onGameMatch(new GameMatch(data[0], data[1], data[2])));
        });
        this.responseMapper.put("SVR GAME MOVE", () -> {
            // playerName = data[0]; moveInt = data[1]; details = data[2]
            String[] data = this.parseResponse();
            this.observers.forEach((observer) ->
                    observer.onGameMove(new GameMove(data[0], Integer.parseInt(data[1]), data[2]))
            );
        });
        this.responseMapper.put("SVR GAME WIN", () -> {
            // score player1 = data[0]; score player2 = data[1]; comment = data[2]
            String[] data = this.parseResponse();
            this.observers.forEach((observer) -> observer.onFinished(new ServerResult(
                    EResult.WIN,
                    Integer.parseInt(data[0]),
                    Integer.parseInt(data[1]),
                    data[2]
            )));
        });
        this.responseMapper.put("SVR GAME LOSS", () -> {
            String[] data = this.parseResponse();
            this.observers.forEach((observer) -> observer.onFinished(new ServerResult(
                    EResult.LOSS,
                    Integer.parseInt(data[0]),
                    Integer.parseInt(data[1]),
                    data[2]
            )));
        });
        this.responseMapper.put("SVR GAME DRAW", () -> {
            String[] data = this.parseResponse();
            this.observers.forEach((observer) -> observer.onFinished(new ServerResult(
                    EResult.DRAW,
                    Integer.parseInt(data[0]),
                    Integer.parseInt(data[1]),
                    data[2]
            )));
        });
        this.responseMapper.put("SVR GAME YOURTURN", () -> {
            this.observers.forEach(IServerObserver::onMove);
        });
        this.responseMapper.put("SVR GAME CHALLENGE", () -> {});  // TODO
    }

    private String[] parseResponse() {
        return Pattern.compile("\"(.*?)\"")
                .matcher(this.response)
                .results()
                .map((matchResult) -> {
                    String result = matchResult.group();
                    return result.substring(1, result.length() - 1);
                }).toArray(String[]::new);
    }
}
