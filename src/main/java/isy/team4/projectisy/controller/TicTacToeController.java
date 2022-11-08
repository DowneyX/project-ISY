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

public class TicTacToeController extends Controller implements IGameHandler, IPlayerTurnHandler {

    @FXML
    public Text player1Text;
    @FXML
    public Text player2Text;
    @FXML
    public Label test;
    public Text gameinfo;
    public GridPane grid;
    private Board board;
    public String p1 = "Player 1";
    public String p2 = "Player 2";
    private String gametype;
    private String currentPlayer;
    private boolean boardDisabled = true;

    private IGame game;

    private volatile int playermove = -1;

    public TicTacToeController() {
        this.board = new Board(3, 3);
    }

    public void setP1name(String p1) {
        player1Text.setText(p1);
    }

    public void setP2name(String p2) {
        player2Text.setText(p2);
    }

    /**
     * Checks if gametype is valid and sets it. Depending on the gametype, a different gamemanager will be used.
     *
     * @param gametype player vs local ai: 'local_pvai'
     *                 player vs local player: 'local_pvp'
     *                 ai vs remote ai: 'remote_aivai
     */
    public void setGameType(String gametype) {
        switch (gametype) {
            case "local_pvai":
            case "local_pvp":
                this.gametype = gametype;
                break;
            case "remote_aivai":
            default:
                throw new UnsupportedOperationException(gametype + " is not implemented.");
        }
    }

    @FXML
    public void initialize() {
    }

    // TODO: refactoring?
    @FXML
    public void navigateToHome(ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        Parent root = FXMLLoader.load(MainApplication.class.getResource("home-view.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void doMove(ActionEvent actionEvent) {
        Button btn = (Button) actionEvent.getSource(); // get the clicked element and cast it to a button

        // Don't do anything if not empty or disabled
        if (!Objects.equals(btn.getText(), "") || boardDisabled) {
            return;
        }


        int idx = Integer.parseInt(btn.getId());
        btn.setText(currentPlayer); // set btn text

        playermove = idx;

    }

    /**
     * Game is stared using the start button.
     *
     * @param actionEvent
     */
    public void startGame(ActionEvent actionEvent) {
        if(this.game != null) {
            setGameInfo("Game is al begonnen!");
            return;
        }

        IRuleSet ruleset = new TicTacToeRuleSet();
        IPlayer[] players = new IPlayer[2];

        if (Objects.equals(gametype, "local_pvai")) {
            players[0] = new LocalPlayer(p1, this);
            players[1] = new AIPlayer(p2);
            ruleset = new TicTacToeRuleSet();
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
        }else {
            return;
        }

        // enable board
        boardDisabled = false;
    }

    @Override
    public void onUpdate() {
        updateCurrentPlayer();
        board = game.getBoard(); // set new board

        // draw new board. runlater because called from another thread
        Platform.runLater(() -> {
            for (int i = 0; i < board.getWidth() * board.getHeight(); i++) {
                int x = i % 3;
                int y = i / 3;
                IPlayer currentplayer = board.getData()[y][x];

                if (currentplayer != null) {
                    Button btn = (Button) grid.getChildren().get(i);
                    btn.setText(Character.toString(currentplayer.getInitial()));
                }
            }
        });

    }

    @Override
    public void onFinished() {
        gameinfo.setText(game.getResult().toString());
        stopGame();
    }

    @Override
    public void onIllegal() {
        gameinfo.setText("Illegale zet!");
    }

    @Override
    public Vector2D getPlayerMove() {
        updateCurrentPlayer();

        // wait until move has been made. TODO: promise resolve / eventbus instead of this?
        while (this.playermove == -1) {
            try {
                Thread.sleep(50);
            } catch (Exception e) {
                System.out.println("error: " + e);
            }
        }

        int x = this.playermove % 3;
        int y = this.playermove / 3;

        this.playermove = -1;

        return new Vector2D(x, y);
    }

    public void emptyBoard() {
        Platform.runLater(() -> {
            for (int i = 0; i < board.getWidth() * board.getHeight(); i++) {
                Button btn = (Button) grid.getChildren().get(i);
                btn.setText("");
            }
        });
    }

    public void stopGame() {
        emptyBoard();
        boardDisabled = true;
        game = null;
    }

    public void setGameInfo(String text) {
        this.gameinfo.setText(text);
    }

    public void updateCurrentPlayer() {
        currentPlayer = this.game.getCurrentPlayer().getName();
        setGameInfo(currentPlayer + " is aan de beurt.");
    }
}
