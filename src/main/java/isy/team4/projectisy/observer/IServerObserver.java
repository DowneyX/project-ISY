package isy.team4.projectisy.observer;

import isy.team4.projectisy.server.GameMatch;
import isy.team4.projectisy.server.GameMove;
import isy.team4.projectisy.util.Result;

public interface IServerObserver {
    public void setGameList(String[] games);
    public void setPlayerList(String[] players);
    public void onGameMatch(GameMatch gameMatch);
    public void onGameMove(GameMove gameMove);
    public void onFinished(Result result);
    public void onMove();
}
