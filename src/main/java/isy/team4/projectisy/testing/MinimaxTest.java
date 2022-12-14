package isy.team4.projectisy.testing;

import isy.team4.projectisy.model.game.LocalGame;
import isy.team4.projectisy.model.player.AIPlayer;
import isy.team4.projectisy.model.player.IPlayer;
import isy.team4.projectisy.model.player.LocalPlayer;
import isy.team4.projectisy.model.rule.IRuleSet;
import isy.team4.projectisy.model.rule.OthelloRuleSet;
import isy.team4.projectisy.util.Board;

import java.util.Arrays;

public class MinimaxTest {
    public static void main(String[] args) {
        AIPlayer[] players = new AIPlayer[2];
        IRuleSet ruleset = new OthelloRuleSet(players);

        players[0] = new AIPlayer("player1", null, ruleset);
        players[1] = new AIPlayer("player2", null, ruleset);
        players[0].setOpponent(players[1]);
        players[1].setOpponent(players[0]);

        Board board = ruleset.getStartingBoard();
        ruleset.setBoard(board);
        System.out.println(board.toString());

        System.out.println(Arrays.toString(ruleset.getValidMoves(players[0])));

        LocalGame localgame = new LocalGame(players, ruleset);

        localgame.start();
        System.out.println(Arrays.toString(localgame.getValidMoves(board)));
        System.out.println(ruleset.getValidMoves(players[0]));
    }
}
