package isy.team4.projectisy.controller;

import isy.team4.projectisy.MainApplication;
import isy.team4.projectisy.util.Board;
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

public class TicTacToeController extends Controller {

    @FXML
    public Text player1Text;
    @FXML
    public Text player2Text;
    @FXML
    public Label test;
    public Text gameinfo;
    public GridPane grid;
    private Board board;
    public String p1;
    public String p2;
    private String gametype;
    private String currentPlayer = "X";
    private boolean boardDisabled = false;

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
        gameinfo.setText(currentPlayer + " is aan de beurt");
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
        int row = idx % 3;
        int col = idx / 3;
        this.turn(col, row); // set an actual move

        // TODO: check if won
        if(winner()) {
            gameinfo.setText(currentPlayer + " heeft gewonnen!");
            boardDisabled = true;
            return;
        }

        // change current player
        switchPlayer();

        boardDisabled = true;

        // set info text
        gameinfo.setText(currentPlayer + " is aan de beurt");

        handleOppositeTurn();

        boardDisabled = false;

        // TODO: check if won
        if(winner()) {
            gameinfo.setText(currentPlayer + " heeft gewonnen!");
            return;
        }
    }

    // TODO: Alles hieronder is tijdelijk als vervanging van een nog te schrijven code

    public void turn(int row, int col) {
        this.board.setElement(currentPlayer, col, row);
    }

    public String getCurrentPlayer() {
        return currentPlayer;
    }

    public boolean winner() {
        return board.board[0][0] == board.board[0][1] && board.board[0][1] == board.board[0][2] && board.board[0][0] != null ||
                board.board[1][0] == board.board[1][1] && board.board[1][1] == board.board[1][2] && board.board[1][0] != null ||
                board.board[2][0] == board.board[2][1] && board.board[2][1] == board.board[2][2] && board.board[2][0] != null ||

                board.board[0][0] == board.board[1][0] && board.board[1][0] == board.board[2][0] && board.board[0][0] != null ||
                board.board[0][1] == board.board[1][1] && board.board[1][1] == board.board[2][1] && board.board[0][1] != null ||
                board.board[0][2] == board.board[1][2] && board.board[1][2] == board.board[2][2] && board.board[0][2] != null ||

                board.board[0][0] == board.board[1][1] && board.board[1][1] == board.board[2][2] && board.board[0][0] != null ||
                board.board[0][2] == board.board[1][1] && board.board[1][1] == board.board[2][0] && board.board[0][2] != null;
    }

    public void switchPlayer() {
        if (Objects.equals(currentPlayer, "X")) {
            currentPlayer = "O";
        } else {
            currentPlayer = "X";
        }
    }

    public void handleOppositeTurn() {
        System.out.println(gametype);
        if(Objects.equals(gametype, "local_pvp")) {
            boardDisabled = false;
        } else if(Objects.equals(gametype, "local_pvai")) {
            // let the ai make a turn
            Random rand = new Random();

            // very stupid ai TODO: refactor
            while(true) {
                int row = rand.nextInt(3);
                int col = rand.nextInt(3);
                System.out.println(row + " " + col);
                if(board.board[row][col] == null) {

                    turn(row, col);
                    int idx = 3*row + col;
                    Button btn = (Button) grid.getChildren().get(idx);
                    System.out.println(row + " - " + col + " = " + idx);
                    btn.setText(currentPlayer);
                    System.out.println(board.toString());
                    switchPlayer();
                    return;
                }
            }
        }
    }
}
