package isy.team4.projectisy;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import isy.team4.projectisy.server.Server;

import java.io.IOException;
import java.util.Scanner;

public class MainApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        // launch();
        MainApplication app = new MainApplication();
        try {
            app.testServer();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void testServer() throws IOException {
        Server server = new Server("localhost", 7789);
        Scanner scanner = new Scanner(System.in);

        String username = scanner.nextLine();
        System.out.println("request login");
        if (server.Requestlogin(username)) {
            System.out.println("request success");
        }

        System.out.println("getting game list");
        try {
            server.getGamelist();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        System.out.println("request success");

        System.out.println("getting player list");
        try {
            server.getPlayerlist();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        System.out.println("request success");

        System.out.println("request find game tic-tac-toe");
        if (server.RequestSubscribeTictactoe()) {
            System.out.println("request success");
        }
    }
}
