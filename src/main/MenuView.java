package src.main;

import java.util.Scanner;

public class MenuView {

    private ApplicationManager application;
    private Server server;
    private Scanner scanner;

    MenuView(ApplicationManager application, Server server) {
        this.application = application;
        this.server = server;
        scanner = new Scanner(System.in);
    }

    void selectionMainMenu() {

        System.out.println("--- main menu:");
        System.out.println("1. offline");
        System.out.println("2. online");
        System.out.println("3. end session");

        int choice = scanner.nextInt();

        switch (choice) {
            case 1:
                // go to offline menu
                selectionOfflineMenu();
                break;
            case 2:
                // go to online menu
                loginMenu();
                break;
            case 3:
                // end session
                System.out.println("goodbye :)");
                break;
            default:
                System.out.println("that is not an option.");
                selectionMainMenu();
                break;
        }

    }

    void selectionOfflineMenu() {

        System.out.println("--- offline menu:");
        System.out.println("1. play tictactoe");
        System.out.println("2. play reversi");
        System.out.println("3. back");

        int choice = scanner.nextInt();

        switch (choice) {
            case 1:
                // go to tictactoe settings
                selectionTictactoeMenu();
                break;
            case 2:
                // go to reversi settings
                System.out.println("comming soon");
                selectionOfflineMenu();
                break;
            case 3:
                // go back to main menu
                selectionMainMenu();
                break;

            default:
                System.out.println("that is not an option.");
                selectionOfflineMenu();
                break;
        }
    }

    void loginMenu() {
        System.out.println("please log in with your username");
        String username = scanner.next();
        System.out.println("loging in on server...");
        server.Requestlogin(username);
        System.out.println("succes!");
        selectionOnlineMenu();
    }

    void selectionOnlineMenu() {
        System.out.println("--- online menu");
        System.out.println("1. Show lobby");
        System.out.println("2. find game tic-tac-toe");
        System.out.println("3. find game reversi");
        System.out.println("4. challenge player tic-tac-toe");
        System.out.println("5. challenge player reversi");
        System.out.println("6. accept incomming challenge");

        int choice = scanner.nextInt();

        switch (choice) {
            case 1:
                try {
                    server.getPlayerlist();
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                selectionOnlineMenu();
            case 2:
                server.requestSubscribeTictactoe();
                System.out.println("please wait for an opponent...");
        }

    }

    void selectionTictactoeMenu() {
        System.out.println("1. player vs player");
        System.out.println("2. player vs cpu");
        System.out.println("3. back");

        int choice = scanner.nextInt();

        switch (choice) {
            case 1:
                // start offline pvp tictactoe
                application.startTicTacToeLocal(false);
                break;
            case 2:
                // start offline pvc tictactoe
                application.startTicTacToeLocal(true);
                break;
            case 3:
                selectionOfflineMenu();
                break;
            default:
                System.out.println("that is not an option");
                selectionTictactoeMenu();
        }
    }
}
