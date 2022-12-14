package isy.team4.projectisy.controller;

import isy.team4.projectisy.MainApplication;
import isy.team4.projectisy.util.EGame;
import isy.team4.projectisy.util.EPlayer;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Objects;
import javafx.scene.Node;

public class HomeController extends Controller {
    private static String selectedGame;
    private static boolean is_cpu;
    private boolean isLocal = true;
    private EPlayer player1;
    private EPlayer player2;

    public void navigateOnline(ActionEvent actionEvent) throws IOException {
        this.isLocal = false;
        navigate(actionEvent, "online-menu-view.fxml");
    }

    public void navigateOffline(ActionEvent actionEvent) throws IOException {
        this.isLocal = true;
        navigate(actionEvent, "offline-menu-view.fxml");
    }

    public void navigateHome(ActionEvent actionEvent) throws IOException {
        navigate(actionEvent, "main-menu-view.fxml");
    }

    public void navigateOptions(ActionEvent actionEvent) throws IOException {
        navigate(actionEvent, "main-menu-view.fxml");
    }

    public void setTicTacToe(ActionEvent actionEvent) throws IOException {
        selectedGame = "tic-tac-toe";
        navigate(actionEvent, "opponent-menu-view.fxml");
        System.out.println(this);
    }

    public void setOthello(ActionEvent actionEvent) throws IOException {
        selectedGame = "othello";
        navigate(actionEvent, "opponent-menu-view.fxml");
        System.out.println(this);
    }

    // TODO: Remove, will be replaced by a list of choices, see initGame
    public void setComputer(ActionEvent actionEvent) throws IOException {
        is_cpu = true;
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        initGame(stage);
    }

    // TODO: Remove, will be replaced by a list of choices, see initGame
    public void setPlayer(ActionEvent actionEvent) throws IOException {
        is_cpu = false;
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        initGame(stage);
    }

    public void initGame(Stage stage) {
        HashMap<String, String> gameViewMapper = new HashMap<String, String>();
        gameViewMapper.put("tic-tac-toe", "tictactoe-view.fxml");
        gameViewMapper.put("othello", "othello-view.fxml");
        // TODO: Pas aan zodat selectie op frontend zo werkt:
        //  Als eerst selectie local of remote -> daarna:
        //  als local: selectie voor zowel player1 als player2 (keuze uit: LOCAL, AI)
        //  als remote: selectie voor 1 player (keuze uit: LOCAL, AI).
        //  Wanneer dit gedaan is krijg je iets zoals dit:
        //
        //  EPlayer[] players = new EPlayer[]{this.player1};
        //  if (this.isLocal) {
        //      players = Arrays.copyOf(players, players.length + 1);  // Could refactor to ArrayList?
        //      players[1] = this.player2;
        //  }
        //  navigateOut(
        //          stage,
        //          gameViewMapper.get(selectedGame),
        //          this.isLocal ? EGame.LOCAL : EGame.REMOTE,
        //          players
        //  );
        //  Hierbij is het makkelijk om een extra speler te implementeren in de "eventuele" toekomst

        navigateOut(
                stage,
                gameViewMapper.get(selectedGame),
                this.isLocal ? EGame.LOCAL : EGame.REMOTE,
                new EPlayer[]{EPlayer.LOCAL, is_cpu ? EPlayer.AI : EPlayer.LOCAL}
        );
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
    public void navigateOut(Stage stage, String view, EGame gameType, EPlayer[] players) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource(view)); // TODO: relative instead
                                                                                             // of absolute gathering of
                                                                                             // resources
            Parent root = fxmlLoader.load();

            GameController controller = fxmlLoader.getController();
            controller.initGame(gameType, players);

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
