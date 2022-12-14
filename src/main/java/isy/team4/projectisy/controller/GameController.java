package isy.team4.projectisy.controller;

import isy.team4.projectisy.MainApplication;
import isy.team4.projectisy.model.game.IGame;
import isy.team4.projectisy.model.game.IGameObserver;
import isy.team4.projectisy.model.game.LocalGame;
import isy.team4.projectisy.model.game.RemoteGame;
import isy.team4.projectisy.model.player.*;
import isy.team4.projectisy.model.rule.IRuleSet;
import isy.team4.projectisy.util.Board;
import isy.team4.projectisy.util.EGame;
import isy.team4.projectisy.util.EPlayer;
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
import java.util.ArrayList;
import java.util.Arrays;

public abstract class GameController implements IPlayerTurnHandler, IGameObserver {

    @FXML
    public Text player1Text;  // TODO: Could better be a list of names related to players known by the so list
    @FXML
    public Text player2Text;  // TODO: Same for this one
    public Text gameinfo;
    public GridPane grid;
    protected Board board;
    protected IRuleSet ruleSet;
    protected boolean boardDisabled = true;
    protected IGame game;
    protected volatile int playerMove = -1;

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
     * @param gameType player vs local ai: 'local_pvai'
     *                 player vs local player: 'local_pvp'
     *                 ai vs remote ai: 'remote_aivai
     */
    public void initGame(EGame gameType, EPlayer[] playersAsType) {
        player1Text.setText("Player 1");  // TODO: actual values
        player2Text.setText("Player 2");

        ArrayList<IPlayer> players = new ArrayList<>();
        for (int i = 0; i < playersAsType.length; i++) {
            switch (playersAsType[i]) {
                case LOCAL -> players.add(new LocalPlayer("Player " + i, this));
                case AI -> players.add(new AIPlayer("Player " + i, this.ruleSet));
                case REMOTE -> players.add(new RemotePlayer("Player " + i));
            }
        }

        // Convert because of normal array usage
        IPlayer[] playersArr = new IPlayer[players.size()];
        playersArr = players.toArray(playersArr);

        // Assign opponent to AI, TODO: later more opponents, for multiple players
        for (int i = 0; i < playersArr.length; i++) {
            if (playersArr[i] instanceof AIPlayer) {
                ((AIPlayer) playersArr[i]).setOpponent(playersArr[(i + 1) % playersArr.length]);
            }
        }

        // Initializing game
        this.game = (gameType == EGame.LOCAL)
                ? new LocalGame(playersArr, this.ruleSet)
                : new RemoteGame(playersArr[0], this.ruleSet);

        this.game.addObserver(this);
    }

    /**
     * Game is started using the start button.
     *
     * @param actionEvent
     */
    public void startGame(ActionEvent actionEvent) {
        if (this.game.isRunning()) {
            this.setGameInfo("Game is al begonnen!");
            return;
        }

        game.start();
    }

    @FXML
    public void navigateToHome(ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        Parent root = FXMLLoader.load(MainApplication.class.getResource("main-menu-view.fxml"));
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
        playerMove = Integer.parseInt(btn.getId());
    }

    @Override
    public void onFinished() {
        this.setGameInfo(game.getResult().toString());
        this.stopGame();
    }

    @Override
    public void onIllegal() {
        this.setGameInfo("Illegale zet!");
        try {
            Thread.sleep(1000);
            boardDisabled = true;
        } catch (Exception e) {
            System.out.println("error: " + e);
        }

        boardDisabled = false;
    }

    @Override
    public void onUpdate() {
        setGameInfo(this.game.getCurrentPlayer().getName() + " is aan de beurt.");
        this.redrawBoard();
    }

    @Override
    public Vector2D getPlayerMove() {

        // wait until move has been made. TODO: promise resolve / eventbus instead of
        // this?
        while (this.playerMove == -1) {
            try {
                Thread.sleep(50);
            } catch (Exception e) {
                System.out.println("error: " + e);
            }
        }

        // board is null at this point. // TODO: is the grid reliable enough?
        int x = this.playerMove % grid.getColumnCount();
        int y = this.playerMove / grid.getRowCount();

        this.playerMove = -1;

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
        this.emptyBoard();
        boardDisabled = true;
        game = null;
    }

    public void setGameInfo(String text) {
        this.gameinfo.setText(text);
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
                IPlayer currentPlayer = board.getData()[y][x];

                if (currentPlayer != null) {
                    Button btn = (Button) grid.getChildren().get(i);
                    btn.setText(Character.toString(currentPlayer.getInitial()));
                }
            }
        });
    }
}
