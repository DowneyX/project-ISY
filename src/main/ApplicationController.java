package src.main;

/**
 * Main
 */
public class ApplicationController {
    // private Server server;
    public static void main(String[] args) {
        ApplicationController application = new ApplicationController();
        Server server = new Server();
        MenuView menu = new MenuView(application, server);

        menu.selectionMainMenu();
    }

    public void startTicTacToeLocal(boolean isCpuMatch) {
        Player player1 = new PlayerHuman("player1", 'X');
        Player player2 = isCpuMatch ? new PlayerSmartTictactoe("CPU", 'O') : new PlayerHuman("player2", 'O');

        TictactoeController tictactoe = new TictactoeController(player1, player2);

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