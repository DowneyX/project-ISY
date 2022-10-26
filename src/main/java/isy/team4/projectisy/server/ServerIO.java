package isy.team4.projectisy.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class ServerIO {
    String ip;
    int port;

    Socket socket;
    PrintWriter out;
    BufferedReader in;

    public ServerIO(String ip, int port) throws IOException {
        this.ip = ip;
        this.port = port;
    }

    public void openSocket() {
        try {
            this.socket = new Socket(this.ip, this.port);
            this.out = new PrintWriter(socket.getOutputStream(), true);
            this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendMessage(String message) {
        out.println(message);
        out.flush();
    }

    public String onMessage() {
        while (true) {
            if (this.socket == null) {
                continue;
            }
            String message = null;
            try {
                message = in.readLine();
                return message;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}