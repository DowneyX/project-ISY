package isy.team4.projectisy;

import isy.team4.projectisy.server.Server;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Scanner;

public class MainApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("home-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        stage.setTitle("Home");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) throws IOException {
//        launch();
        tournament();
    }

    /**
     * Tournament for tic tac toe using the CLI because remotegame is not ready yet.
     */
    public static void tournament() {
        Server server;
        try {
            server = new Server("145.33.225.170", 7789);
        } catch(Exception e) {
            System.out.println("Kon de server niet starten: " + e);
            return;
        }

        server.Requestlogin("ITV1ATeam4");
        server.requestSubscribeTictactoe();
    }
}
