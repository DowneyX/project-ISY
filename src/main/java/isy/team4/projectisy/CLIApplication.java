package isy.team4.projectisy;

import isy.team4.projectisy.controller.CLIController;
import isy.team4.projectisy.model.rule.OthelloRuleSet;
import isy.team4.projectisy.server.ServerProperties;

public class CLIApplication {
    public static void main(String[] args) {
        tournament();
    }

    /**
     * Tournament for tic tac toe using the CLI because remotegame is not ready yet.
     */
    public static void tournament() {
        CLIController controller = new CLIController(new OthelloRuleSet(), new ServerProperties(
                "145.33.225.170",  // 145.33.225.170
                7789,
                String.format("ITV2A%dGROEP4", (int)(Math.random() * 100))
        ));
        controller.startTournament();
//        Server server;
//        try {
//            server = new Server("145.33.225.170", 7789);
//        } catch (Exception e) {
//            System.out.println("Kon niet met de server verbinden: " + e);
//            return;
//        }
//
//        server.login("ITV2ATEAM4"); // no subscribe needed
    }
}
