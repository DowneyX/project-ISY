package isy.team4.projectisy.server;

import isy.team4.projectisy.util.Vector2D;

public class GameMove {
    public String playerName;
    public Integer move;
    public String details;  // TODO: what is it?

    public GameMove(String playerName, Integer move, String details) {
        this.playerName = playerName;
        this.move = move;
        this.details = details;
    }
}
