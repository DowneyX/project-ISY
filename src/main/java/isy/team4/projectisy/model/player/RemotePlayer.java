package isy.team4.projectisy.model.player;

import isy.team4.projectisy.util.Board;
import isy.team4.projectisy.util.Vector2D;

public class RemotePlayer implements IPlayer {
    private final String name;
    private char initial;
    private final IPlayerTurnHandler playerTurnHandler;

    public RemotePlayer(String name, IPlayerTurnHandler localPlayerHandler) {
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
    public Vector2D getMove(Board board, IPlayer opponent) {
        return this.playerTurnHandler.getPlayerMove();
    }

    @Override
    public void setInitial(char initial) {
        this.initial = initial;
    }

    public String toString() {
        return "Remoteplayer";
    }
}
