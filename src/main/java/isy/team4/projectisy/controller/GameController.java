package isy.team4.projectisy.controller;
import isy.team4.projectisy.MainApplication;
import isy.team4.projectisy.model.game.IGame;
import isy.team4.projectisy.model.game.IGameHandler;
import isy.team4.projectisy.model.player.IPlayer;
import isy.team4.projectisy.model.player.IPlayerTurnHandler;
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
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class GameController implements IGameHandler, IPlayerTurnHandler {
    @FXML
    public Text player1Text;
    @FXML
    public Text player2Text;
    public Text gameinfo;
    public GridPane grid;
    protected Board board;
    public String p1 = "Player 1";
    public String p2 = "Player 2";
    protected String gametype;
    protected String currentPlayer;
    protected boolean boardDisabled = true;

    protected IGame game;

    protected volatile int playermove = -1;

    public void setP1name(String p1) {
        player1Text.setText(p1);
    }

    public void setP2name(String p2) {
        player2Text.setText(p2);
    }

    /**
     * Checks if gametype is valid and sets it. Depending on the gametype, a
     * different gamemanager will be used.
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
        if (boardDisabled) {
            return;
        }
        int idx = Integer.parseInt(btn.getId());
        playermove = idx;
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
    public void onUpdate() {
        updateCurrentPlayer();
    }

    @Override
    public Vector2D getPlayerMove() {
        updateCurrentPlayer();
        redrawBoard();

        // wait until move has been made. TODO: promise resolve / eventbus instead of
        // this?
        while (this.playermove == -1) {
            try {
                Thread.sleep(50);
            } catch (Exception e) {
                System.out.println("error: " + e);
            }
        }

        // board is null at this point. // TODO: is the grid reliable enough?
        int x = this.playermove % grid.getColumnCount();
        int y = this.playermove / grid.getRowCount();

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

    /**
     * Gets the updates board from the game and redraws it.
     */
    public void redrawBoard() {
        board = game.getBoard(); // set new board

        // draw new board. runlater because called from another thread
        Platform.runLater(() -> {
            for (int i = 0; i < board.getWidth() * board.getHeight(); i++) {
                int x = i % board.getWidth();
                int y = i / board.getHeight();
                IPlayer currentplayer = board.getData()[y][x];

                if (currentplayer != null) {
                    Button btn = (Button) grid.getChildren().get(i);
                    btn.setText(Character.toString(currentplayer.getInitial()));
                }
            }
        });
    }
}
