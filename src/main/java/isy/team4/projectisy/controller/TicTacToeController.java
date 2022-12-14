package isy.team4.projectisy.controller;

import isy.team4.projectisy.model.rule.TicTacToeRuleSet;
import isy.team4.projectisy.util.Board;

public class TicTacToeController extends GameController {
    public TicTacToeController() {
        this.ruleSet = new TicTacToeRuleSet();
        this.board = new Board(3, 3);
    }
}
