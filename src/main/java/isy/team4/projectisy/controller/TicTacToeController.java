package isy.team4.projectisy.controller;

import isy.team4.projectisy.model.game.LocalGame;
import isy.team4.projectisy.model.player.AIPlayer;
import isy.team4.projectisy.model.player.IPlayer;
import isy.team4.projectisy.model.player.LocalPlayer;
import isy.team4.projectisy.model.rule.IRuleSet;
import isy.team4.projectisy.model.rule.TicTacToeRuleSet;
import isy.team4.projectisy.util.Board;
import javafx.event.ActionEvent;
import java.util.Objects;

public class TicTacToeController extends GameController {
    public TicTacToeController() {
        this.board = new Board(3, 3);
    }

    /**
     * Game is started using the start button.
     *
     * @param actionEvent
     */
    public void startGame(ActionEvent actionEvent) {
        if (this.game != null) {
            setGameInfo("Game is al begonnen!");
            return;
        }

        IRuleSet ruleset = new TicTacToeRuleSet();
        IPlayer[] players = new IPlayer[2];

        if (Objects.equals(gametype, "local_pvai")) {

            players[0] = new LocalPlayer(p1, this);
            players[1] = new AIPlayer(p2, players[0], ruleset);
            LocalGame localgame = new LocalGame(players, ruleset);
            localgame.registerObserver(this);
            game = localgame;
            game.start();
        } else if (Objects.equals(gametype, "local_pvp")) {
            players[0] = new LocalPlayer(p1, this);
            players[1] = new LocalPlayer(p2, this);
            LocalGame localgame = new LocalGame(players, ruleset);
            localgame.registerObserver(this);
            game = localgame;
            game.start();
        } else {
            return;
        }

        // enable board
        boardDisabled = false;
    }
}
