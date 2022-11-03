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
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class TicTacToeController extends Controller {

    private Board board;
    public TicTacToeController() {
        this.board = new Board(3, 3);
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

        // Check if empty
        if(!Objects.equals(btn.getText(), "")) {
            return;
        }

        btn.setText("X"); // set btn text

        int id = Integer.parseInt(btn.getId());
        int row = id % 3;
        int col = id / 3;

        this.board.setElement('X', row, col);

        System.out.println(this.board.toString());

    }
}
