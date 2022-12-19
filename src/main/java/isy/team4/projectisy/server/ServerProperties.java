package isy.team4.projectisy.server;

public class ServerProperties {
    public String ip;
    public int port;
    public String userName;

    public ServerProperties(String ip, int port, String userName) {
        this.ip = ip;
        this.port = port;
        this.userName = userName;
    }
}
