package isy.team4.projectisy.testing;

import isy.team4.projectisy.model.game.LocalGame;
import isy.team4.projectisy.model.player.AIPlayer;
import isy.team4.projectisy.model.player.IPlayer;
import isy.team4.projectisy.model.player.LocalPlayer;
import isy.team4.projectisy.model.rule.IRuleSet;
import isy.team4.projectisy.model.rule.OthelloRuleSet;
import isy.team4.projectisy.observer.IObserver;
import isy.team4.projectisy.util.Board;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Date;
import java.util.SortedMap;

public class MinimaxTest implements IObserver {
    public static void main(String[] args) {
        MinimaxTest minimaxTest = new MinimaxTest();
        minimaxTest.setup();
        minimaxTest.createFile();
        minimaxTest.start();
    }

    LocalGame localgame;
    Integer movecount = 1;

    double start;

    String filepath;

    FileWriter fileWriter;
    IRuleSet ruleset;

    public void setup() {
        AIPlayer[] players = new AIPlayer[2];
        ruleset = new OthelloRuleSet(players);

        players[0] = new AIPlayer("player1", null, ruleset);
        players[1] = new AIPlayer("player2", null, ruleset);
        players[0].setOpponent(players[1]);
        players[1].setOpponent(players[0]);

        Board board = ruleset.getStartingBoard();
        ruleset.setBoard(board);

        localgame = new LocalGame(players, ruleset);
        localgame.registerObserver(this);
    }

    public void start() {
        localgame.start();
        start = System.currentTimeMillis();
    }

    public void createFile() {
        try {
            Files.createDirectories(Paths.get("./out"));
        } catch(Exception e) {
            System.out.println("Could not create out folder");
            return;
        }

        filepath = new SimpleDateFormat("'./out/'yyyyMMddHHmm'.csv'").format(new Date());
        try {
            File myObj = new File(filepath);
            if (myObj.createNewFile()) {
                System.out.println("File created: " + myObj.getName());
            } else {
                System.out.println("File already exists.");
            }

            fileWriter = new FileWriter(filepath);
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

        writeLine("Player,RuntimeMillis,Movecount,Depth");

    }

    private void writeLine(String line) {
        try {
            fileWriter.write(line);
            fileWriter.write("\n");
        } catch (IOException e) {
            System.out.println("An error occurred while writing to the file.");
            e.printStackTrace();
        }
    }

    public void writeLog(String player, double runtime, int movecount, int depth) {
        writeLine(player + "," + runtime + "," + movecount + "," + depth);
    }

    @Override
    public void update(String msg) {
        double current =  System.currentTimeMillis();
        double timeTook = current - start;
        start = current;

        System.out.println("Move " + movecount + " by " + localgame.getCurrentPlayer() + " took " + timeTook + " millis");
        writeLog(localgame.getCurrentPlayer().getName(), timeTook, movecount, -1); // todo add depth
        movecount++;

        if(ruleset.isDraw() || ruleset.isDraw()) {
            try {
                fileWriter.close();
            } catch(Exception e) {

            }
        }
    }
}
