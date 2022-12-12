package isy.team4.projectisy.controller;

import isy.team4.projectisy.MainApplication;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.Objects;
import javafx.scene.Node;

public class HomeController extends Controller {
    private static String selectedGame;
    private static boolean is_cpu;

    private String p1 = "Player 1";
    private String p2 = "Player 2";

    public void navigateOnline(ActionEvent actionEvent) throws IOException {
        navigate(actionEvent, "online-menu-view.fxml");
    }

    public void navigateOffline(ActionEvent actionEvent) throws IOException {
        navigate(actionEvent, "offline-menu-view.fxml");
    }

    public void navigateHome(ActionEvent actionEvent) throws IOException {
        navigate(actionEvent, "main-menu-view.fxml");
    }

    public void navigateOptions(ActionEvent actionEvent) throws IOException {
        navigate(actionEvent, "main-menu-view.fxml");
    }

    public void setTictactoe(ActionEvent actionEvent) throws IOException {
        selectedGame = "Tic-tac-toe";
        navigate(actionEvent, "opponent-menu-view.fxml");
        System.out.println(this);
    }

    public void setOthello(ActionEvent actionEvent) throws IOException {
        selectedGame = "Othello";
        navigate(actionEvent, "opponent-menu-view.fxml");
        System.out.println(this);
    }

    public void setComputer(ActionEvent actionEvent) throws IOException {
        is_cpu = true;
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        initGame(stage);
    }

    public void setPlayer(ActionEvent actionEvent) throws IOException {
        is_cpu = false;
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        initGame(stage);
    }

    public void initGame(Stage stage) {

        if (is_cpu && selectedGame.equals("Tic-tac-toe")) {
            navigateOut(stage, "tictactoe-view.fxml", "local_pvai");
        }
        if (!is_cpu && selectedGame.equals("Tic-tac-toe")) {
            navigateOut(stage, "tictactoe-view.fxml", "local_pvp");
        }
        if (is_cpu && selectedGame.equals("Othello")) {
            navigateOut(stage, "othello-view.fxml", "local_pvai");
        }
        if (!is_cpu && selectedGame.equals("Othello")) {
            navigateOut(stage, "othello-view.fxml", "local_pvp");
        }

    }

    @FXML
    public void navigate(ActionEvent actionEvent, String File) throws IOException {
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        Parent root = FXMLLoader.load(MainApplication.class.getResource(File));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    public void navigateOut(Stage stage, String view, String gametype) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource(view)); // TODO: relative instead
                                                                                             // of absolute gathering of
                                                                                             // resources
            Parent root = fxmlLoader.load();

            GameController controller = fxmlLoader.getController();
            controller.setP1name(p1);
            controller.setP2name(p2);
            controller.setGameType(gametype);

            Scene scene = new Scene(root);
            scene.getStylesheets()
                    .add(Objects.requireNonNull(MainApplication.class.getResource("style.css")).toExternalForm()); // main
                                                                                                                   // stylesheet
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
