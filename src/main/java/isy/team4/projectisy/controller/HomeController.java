package isy.team4.projectisy.controller;

import isy.team4.projectisy.MainApplication;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;

public class HomeController extends Controller {
    @FXML
    private Button tictactoe;
    @FXML
    private Label welcomeText;

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }

    /**
     * Simple method to facilitate navigating between scenes
     * @param actionEvent
     */
    @FXML
    public void navigate(ActionEvent actionEvent) throws IOException {
        Stage stage;
        Parent root;

        if(actionEvent.getSource() == tictactoe) {
            stage = (Stage) tictactoe.getScene().getWindow();
            root = FXMLLoader.load(getClass().getResource("tictactoe-view.fxml"));
        } else {
            return;
        }

        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
