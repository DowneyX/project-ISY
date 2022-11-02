package isy.team4.projectisy;

import java.io.IOException;

import isy.team4.projectisy.game.TictactoeManager;
import isy.team4.projectisy.player.Player;
import isy.team4.projectisy.player.PlayerHuman;
import isy.team4.projectisy.player.PlayerSmartTictactoe;
import isy.team4.projectisy.server.Server;

/**
 * Main
 */
public class MainApplication {
    public Server server;
    public MenuView menu;
    public TictactoeManager game;

    // private Server server;
    public static void main(String[] args) throws IOException {
        MainApplication application = new MainApplication();
        application.server = new Server("localhost", 7789);
        application.menu = new MenuView(application, application.server);
        application.startMenu();
    }

    public void startMenu() {
        menu.selectionMainMenu();
    }

    public void startTicTacToeLocal(boolean isCpuMatch) {
        Player player1 = new PlayerHuman("player1", 'X');
        Player player2 = isCpuMatch ? new PlayerSmartTictactoe("CPU", 'O') : new PlayerHuman("player2", 'O');

        TictactoeManager tictactoe = new TictactoeManager(player1, player2);
        player2.setBoard(tictactoe.getBoard());

        // local tic-tac-toe gameloop
        while (true) {
            System.out.println(tictactoe.getBoard().toString());
            System.out.println("it's [" + tictactoe.getPlayerWithTurn().getName() + "] their turn");
            tictactoe.playerMove();
            if (tictactoe.hasWon(tictactoe.getTurn())) {
                System.out.println(tictactoe.getBoard().toString());
                System.out.println("[" + tictactoe.getPlayerWithTurn().getName() + "] won!");
                break;
            }
            if (tictactoe.getBoard().isFull()) {
                System.out.println(tictactoe.getBoard().toString());
                System.out.println("it's a draw!");
                break;
            }
            tictactoe.incrementTurn();
        }

        startMenu();
    }
}
