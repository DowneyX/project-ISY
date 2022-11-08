package isy.team4.projectisy.model.player;

import isy.team4.projectisy.util.Board;
import isy.team4.projectisy.util.Vector2D;

public class LocalPlayer implements IPlayer {
    private final String name;
    private char initial;
    private final IPlayerTurnHandler playerTurnHandler;

    public LocalPlayer(String name, IPlayerTurnHandler localPlayerHandler) {
        this.name = name;
        this.playerTurnHandler = localPlayerHandler;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public char getInitial() {
        return this.initial;
    }

    @Override
    public Vector2D getMove(Board board) {
        return this.playerTurnHandler.getPlayerMove();
    }

    @Override
    public void setInitial(char initial) {
        this.initial = initial;
    }

    public String toString() {
        if (this.name != null) {
            return this.name;
        }
        return "Localplayer";
    }
}
