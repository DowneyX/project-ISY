package isy.team4.projectisy.server;

public class GameMatch {
    public String playerToMove;
    public String gameType;
    public String opponent;

    public GameMatch(String playerToMove, String gameType, String opponent) {
        this.playerToMove = playerToMove;
        this.gameType = gameType;
        this.opponent = opponent;
    }
}
