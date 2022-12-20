package isy.team4.projectisy.controller;

import isy.team4.projectisy.MainApplication;
import isy.team4.projectisy.model.game.IGame;
import isy.team4.projectisy.model.game.IGameObserver;
import isy.team4.projectisy.model.game.LocalGame;
import isy.team4.projectisy.model.game.RemoteGame;
import isy.team4.projectisy.model.player.*;
import isy.team4.projectisy.model.rule.IRuleSet;
import isy.team4.projectisy.server.ServerProperties;
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
import java.util.Random;
import java.util.stream.IntStream;

public abstract class GameController implements IPlayerTurnHandler, IGameObserver {
    @FXML
    public Text player1Text;  // TODO: Could better be a list of names related to players known by the so list
    @FXML
    public Text player2Text;  // TODO: Same for this one
    public Text gameinfo;
    public GridPane grid;
    protected IRuleSet ruleSet;  // Only use for init!
    protected boolean boardDisabled = true;
    protected IGame game;
    protected Integer playerMove = null;

    /**
     * Initializes game with players
     */
    public void initGame(EGame gameType, EPlayer[] playersAsType) {
        player1Text.setText("?");
        player2Text.setText("?");

        // Map through playersAsType and convert to IPlayer objects
        IPlayer[] players = IntStream.range(0, playersAsType.length).mapToObj((i) -> switch (playersAsType[i]) {
            case LOCAL -> new LocalPlayer("Player " + (i + 1), this);
            case AI -> new AIPlayer("Player " + (i + 1));
            case REMOTE -> new RemotePlayer("Player " + (i + 1));
        }).toArray(IPlayer[]::new);

        if (gameType == EGame.REMOTE) {
            players[0].setName(String.format("ITV2A%dGROEP4", (int)(Math.random() * 100)));  // TODO: Remove, just for testing
        }

        // Initializing game
        this.game = (gameType == EGame.LOCAL)
                ? new LocalGame(players, this.ruleSet)
                : new RemoteGame(players[0], this.ruleSet,
                        new ServerProperties("localhost", 7789, players[0].getName())  // TODO: 145.33.225.170
                );

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
        this.setGameInfo("Aan het wachten..");
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
        if (this.boardDisabled) {
            return;
        }
        playerMove = Integer.parseInt(btn.getId());
    }

    @Override
    public void onStarted() {
        this.player1Text.setText(this.game.getPlayers()[0].getName());
        this.player2Text.setText(this.game.getPlayers()[1].getName());

        this.onUpdate();
    }

    @Override
    public void onUpdate() {
        this.setGameInfo(this.game.getCurrentPlayer().getName() + " is aan de beurt.");
        this.redrawBoard();
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
    public void onFinished() {
        this.setGameInfo(this.game.getResult().toString());
        this.stopGame();
    }

    @Override
    public Vector2D getPlayerMove() {
        this.boardDisabled = false;
        // wait until move has been made. TODO: promise resolve / eventbus instead of
        // this?
        while (this.playerMove == null) {
            try {
                Thread.sleep(50);
            } catch (Exception e) {
                System.out.println("error: " + e);
            }
        }
        this.boardDisabled = true;

        // board is null at this point. // TODO: is the grid reliable enough?
        int x = this.playerMove % grid.getColumnCount();
        int y = this.playerMove / grid.getRowCount();

        this.playerMove = null;

        return new Vector2D(x, y);
    }

    public void emptyBoard() {
        Platform.runLater(() -> {
            for (int i = 0; i < this.game.getBoard().getWidth() * this.game.getBoard().getHeight(); i++) {
                Button btn = (Button) grid.getChildren().get(i);
                btn.setText("");
            }
        });
    }

    public void stopGame() {
        this.boardDisabled = true;
//        this.game = null;  TODO: set same as initGame, not null as it breaks
    }

    public void setGameInfo(String text) {
        this.gameinfo.setText(text);
    }

    /**
     * Gets the updates board from the game and redraws it.
     */
    public void redrawBoard() {
        Board board = game.getBoard(); // set new board
        Vector2D[] validMoves = this.game.getValidMoves();

        // draw new board. runlater because called from another thread
        Platform.runLater(() -> {
            for (int i = 0; i < board.getWidth() * board.getHeight(); i++) {
                int x = i % board.getWidth();
                int y = i / board.getHeight();
                IPlayer playerCell = board.getData()[y][x];
                Button btn = (Button) grid.getChildren().get(i);

                // Draw players
                if (playerCell != null) {
                    btn.setText(Character.toString(playerCell.getInitial()));
                } else {
                    btn.setText("");
                }
            }

            // Draw validMoves
            for (Vector2D move : validMoves) {
                Button btn = (Button) grid.getChildren().get(move.toInt(board));
                btn.setText("﹖");  // ◌, ⚬, ﹖
            }
        });
    }
}
