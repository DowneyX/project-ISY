package isy.team4.projectisy.model.player;

import isy.team4.projectisy.util.Board;
import isy.team4.projectisy.util.Vector2D;

public class RemotePlayer implements IPlayer {
    private final String name;
    private String initial;
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
    public String getInitial() {
        return this.initial;
    }

    @Override
    public Vector2D getMove(Board board) {
        return this.playerTurnHandler.getPlayerMove();
    }

    @Override
    public void setInitial(String initial) {
        this.initial = initial;
    }
}
