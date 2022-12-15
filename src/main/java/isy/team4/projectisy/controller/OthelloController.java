package isy.team4.projectisy.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class OthelloController extends GameController {
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
}
