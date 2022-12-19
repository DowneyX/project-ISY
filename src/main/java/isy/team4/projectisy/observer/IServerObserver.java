package isy.team4.projectisy.observer;

import isy.team4.projectisy.server.GameMatch;
import isy.team4.projectisy.server.GameMove;
import isy.team4.projectisy.server.ServerResult;

public interface IServerObserver {
    public void onGameList(String[] games);
    public void onPlayerList(String[] players);
    public void onGameMatch(GameMatch gameMatch);
    public void onGameMove(GameMove gameMove);
    public void onFinished(ServerResult result);
    public void onMove();
}
