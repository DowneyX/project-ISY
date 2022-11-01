package src.main;

import java.io.IOException;

/**
 * Main
 */
public class ApplicationManager {
    public Server server;
    public MenuView menu;
    public TictactoeManager game;

    // private Server server;
    public static void main(String[] args) throws IOException {
        ApplicationManager application = new ApplicationManager();
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

        // local tic-tac-toe gameloop
        while (true) {
            System.out.println(tictactoe.getBoard().toString());
            System.out.println("it's [" + tictactoe.getPlayerWithTurn().getName() + "] their turn");
            tictactoe.player_move();
            if (tictactoe.has_won(tictactoe.getTurn())) {
                System.out.println(tictactoe.getBoard().toString());
                System.out.println("[" + tictactoe.getPlayerWithTurn().getName() + "] won!");
                break;
            }
            if (tictactoe.getBoard().isFull()) {
                System.out.println(tictactoe.getBoard().toString());
                System.out.println("it's a draw!");
                break;
            }
            tictactoe.increment_turn();
        }
    }
}