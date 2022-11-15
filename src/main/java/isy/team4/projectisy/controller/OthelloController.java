package isy.team4.projectisy.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;

public class OthelloController extends GameController {
    @FXML
    private GridPane grid;
    @FXML
    public void initialize() {
        int wh = 8;

        for(int i = 0; i < wh*wh; i++) {
            Button button = new Button();
            button.setId(Integer.toString(i));
            button.getStyleClass().add("board-button");
            button.setOnAction(e -> {
                System.out.println("TEST");
            });
            grid.add(button, i % wh, i / wh);
        }

    }

    public void startGame(ActionEvent actionEvent) {
    }
}
