package isy.team4.projectisy.testing;

import isy.team4.projectisy.model.game.LocalGame;
import isy.team4.projectisy.model.player.AIPlayer;
import isy.team4.projectisy.model.player.IPlayer;
import isy.team4.projectisy.model.player.LocalPlayer;
import isy.team4.projectisy.model.rule.IRuleSet;
import isy.team4.projectisy.model.rule.OthelloRuleSet;
import isy.team4.projectisy.observer.IObserver;
import isy.team4.projectisy.util.Board;

import java.util.Arrays;
import java.util.SortedMap;

public class MinimaxTest implements IObserver {
    public static void main(String[] args) {
        MinimaxTest minimaxTest = new MinimaxTest();
        minimaxTest.setup();
        minimaxTest.start();
    }

    LocalGame localgame;
    Integer movecount = 1;

    double start;

    public void setup() {
        AIPlayer[] players = new AIPlayer[2];
        IRuleSet ruleset = new OthelloRuleSet(players);

        players[0] = new AIPlayer("player1", null, ruleset);
        players[1] = new AIPlayer("player2", null, ruleset);
        players[0].setOpponent(players[1]);
        players[1].setOpponent(players[0]);

        Board board = ruleset.getStartingBoard();
        ruleset.setBoard(board);

        localgame = new LocalGame(players, ruleset);
        localgame.registerObserver(this);
    }

    public void start() {
        localgame.start();
        start = System.currentTimeMillis();
    }

    @Override
    public void update(String msg) {
        double current =  System.currentTimeMillis();
        double timeTook = current - start;
        start = current;

        System.out.println("Move " + movecount + " by " + localgame.getCurrentPlayer() + " took " + timeTook + " millis");
        movecount++;
    }
}
