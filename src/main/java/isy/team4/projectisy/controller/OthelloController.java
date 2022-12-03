package isy.team4.projectisy.controller;

import isy.team4.projectisy.model.game.LocalGame;
import isy.team4.projectisy.model.player.AIPlayer;
import isy.team4.projectisy.model.player.IPlayer;
import isy.team4.projectisy.model.player.LocalPlayer;
import isy.team4.projectisy.model.rule.IRuleSet;
import isy.team4.projectisy.model.rule.OthelloRuleSet;
import isy.team4.projectisy.model.rule.TicTacToeRuleSet;
import isy.team4.projectisy.util.Vector2D;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.util.Objects;

public class OthelloController extends GameController {
    @FXML
    private GridPane grid;

    @FXML
    public void initialize() {
        /**
         * Create board buttons
         */
        int wh = 8;
        for (int i = 0; i < Math.pow(wh, 2); i++) {
            Button button = new Button();
            button.setId(Integer.toString(i));
            button.getStyleClass().add("board-button");
            button.setOnAction(this::doMove);
            grid.add(button, i % wh, i / wh);
        }
    }

    public void startGame(ActionEvent actionEvent) {
        if (this.game != null) {
            setGameInfo("Game is al begonnen!");
            return;
        }

        IRuleSet ruleset;
        IPlayer[] players = new IPlayer[2];

        if (Objects.equals(gametype, "local_pvai")) {
            ruleset = new OthelloRuleSet(players);
            players[0] = new LocalPlayer(p1, this);
            players[1] = new AIPlayer(p2, players[0], ruleset);
            game = new LocalGame(players, ruleset);
            game.setGameHandler(this);
            game.start();
        } else if (Objects.equals(gametype, "local_pvp")) {
            ruleset = new OthelloRuleSet(players);
            players[0] = new LocalPlayer(p1, this);
            players[1] = new LocalPlayer(p2, this);
            game = new LocalGame(players, ruleset);
            game.setGameHandler(this);
            game.start();
        } else {
            return;
        }

        // othello starts with a board that is not empty.
        redrawBoard();

        // enable board
        boardDisabled = false;
    }

    /**
     * TODO: refactoring. Othello needs validmoves view.
     */
    @Override
    public void redrawBoard() {
        board = game.getBoard(); // set new board
        this.emptyBoard(); // important, because redraw does not clear previous possiblemoves by default

        Vector2D[] validmoves = this.game.getValidMoves(board);

        // draw new board. runlater because called from another thread
        Platform.runLater(() -> {
            for (int i = 0; i < board.getWidth() * board.getHeight(); i++) {
                int x = i % board.getWidth();
                int y = i / board.getHeight();
                IPlayer currentplayer = board.getData()[y][x];
                Button btn = (Button) grid.getChildren().get(i);

                if (currentplayer != null) {
                    btn.setText(Character.toString(currentplayer.getInitial()));
                } else {
                    for (Vector2D move : validmoves) {
                        if (move.toInt(board) == i) {
                            // add possible moves
                            btn.setText("﹖"); // ◌, ⚬, ﹖
                        }
                    }
                }
            }
        });
    }
}
