package isy.team4.projectisy.model.player;

import isy.team4.projectisy.util.Board;
import isy.team4.projectisy.util.Vector2D;

public class RemotePlayer implements IPlayer {
    private final String name;
    private char initial;

    public RemotePlayer(String name) {
        this.name = name;
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
        return null;  // TODO: Remove from player, as it removes complexity because of all the dependencies
    }

    @Override
    public void setInitial(char initial) {
        this.initial = initial;
    }

    @Override
    public String toString() {
        return "Remoteplayer";
    }
}
