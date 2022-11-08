package isy.team4.projectisy.util;

import isy.team4.projectisy.model.player.IPlayer;

public class Result {
    private final EResult result;
    private IPlayer winningPlayer;

    public Result(EResult result) {
        this.result = result;
    }

    public EResult getResult() {
        return result;
    }

    // Nullable
    public IPlayer getWinningPlayer() {
        return winningPlayer;
    }

    public void setWinningPlayer(IPlayer winningPlayer) {
        this.winningPlayer = winningPlayer;
    }

    public String toString() {
        if(getResult() == EResult.DRAW) {
            return "Gelijkspel";
        } else {
            return getWinningPlayer() + " heeft gewonnen";
        }
    }
}
