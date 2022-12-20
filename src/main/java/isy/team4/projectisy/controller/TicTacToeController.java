package isy.team4.projectisy.controller;

import isy.team4.projectisy.model.rule.TicTacToeRuleSet;

public class TicTacToeController extends GameController {
    public TicTacToeController() {
        this.ruleSet = new TicTacToeRuleSet();
    }
}
