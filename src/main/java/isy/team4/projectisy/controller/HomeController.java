package isy.team4.projectisy.controller;

import isy.team4.projectisy.MainApplication;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class HomeController extends Controller {
    public MenuButton dropdown;

    private String p1 = "Player 1";
    private String p2 = "Player 2";

    public void ticTacToePvAI(ActionEvent actionEvent) {
        p2 = "Local AI";
        navigateToTicTacToe("tictactoe-view.fxml", "local_pvai");
    }

    public void ticTacToePvP(ActionEvent actionEvent) {
        navigateToTicTacToe("tictactoe-view.fxml", "local_pvp");
    }

    @FXML
    public void navigateToTicTacToe(String view, String gametype) {
        try {
            Stage stage = (Stage) dropdown.getScene().getWindow(); // for dropdown using menubutton
            FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource(view));
            Parent root = fxmlLoader.load();

            GameController controller = fxmlLoader.getController();
            controller.setP1name(p1);
            controller.setP2name(p2);
            controller.setGameType(gametype);

            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }
}
