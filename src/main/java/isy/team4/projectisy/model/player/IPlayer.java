package isy.team4.projectisy.model.player;

import isy.team4.projectisy.util.Board;
import isy.team4.projectisy.util.Vector2D;

public interface IPlayer {
    String getName();

    String getInitial();

    Vector2D getMove(Board board);

    void setInitial(String initial);
}
