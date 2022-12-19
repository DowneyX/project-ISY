package isy.team4.projectisy.server;

import isy.team4.projectisy.util.EResult;

public class ServerResult {
    public EResult result;
    public int scoreP1;
    public int scoreP2;
    public String comment;

    public ServerResult(EResult result, int scoreP1, int scoreP2, String comment) {
        this.result = result;
        this.scoreP1 = scoreP1;
        this.scoreP2 = scoreP2;
        this.comment = comment;
    }
}
