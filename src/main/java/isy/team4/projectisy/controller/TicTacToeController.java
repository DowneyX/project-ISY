package isy.team4.projectisy.controller;

import isy.team4.projectisy.MainApplication;
import isy.team4.projectisy.model.game.IGame;
import isy.team4.projectisy.model.game.IGameHandler;
import isy.team4.projectisy.model.game.LocalGame;
import isy.team4.projectisy.model.player.AIPlayer;
import isy.team4.projectisy.model.player.IPlayer;
import isy.team4.projectisy.model.player.IPlayerTurnHandler;
import isy.team4.projectisy.model.player.LocalPlayer;
import isy.team4.projectisy.model.rule.IRuleSet;
import isy.team4.projectisy.model.rule.TicTacToeRuleSet;
import isy.team4.projectisy.util.Board;
import isy.team4.projectisy.util.Vector2D;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;
import java.util.Random;

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

        IRuleSet ruleset;
        IPlayer[] players = new IPlayer[2];

        if (Objects.equals(gametype, "local_pvai")) {

            ruleset = new TicTacToeRuleSet();
            players[0] = new LocalPlayer(p1, this);
            players[1] = new AIPlayer(p2, players[0], ruleset);
            game = new LocalGame(players, ruleset);
            game.setGameHandler(this);
            game.start();
        } else if (Objects.equals(gametype, "local_pvp")) {
            players[0] = new LocalPlayer(p1, this);
            players[1] = new LocalPlayer(p2, this);
            ruleset = new TicTacToeRuleSet();
            game = new LocalGame(players, ruleset);
            game.setGameHandler(this);
            game.start();
        } else {
            return;
        }

        // enable board
        boardDisabled = false;
    }
}
