package socket;

import util.Handler;

import java.io.IOException;
import java.net.Socket;
import java.util.UUID;

public class Client {
    private String host;
    private String userId;
    private int port;

    public Client() {
        this.host = "localhost";
        this.userId = UUID.randomUUID().toString();
        this.port = 10000;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public void start() throws IOException {
        Socket socket = new Socket(this.host, this.port);
        new Handler(socket).start(userId);
    }
}