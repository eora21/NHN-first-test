package socket;

import handler.Handler;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class Server {
    private static final Set<String> BLACK_LIST = new HashSet<>();
    static {
        BLACK_LIST.add("all");
        BLACK_LIST.add(Handler.SYSTEM);
    }

    private int port = 10000;

    public void addBlackList(String arg) {
        BLACK_LIST.addAll(Arrays.asList(arg.split(",")));
    }

    public void setPort(int port) {
        this.port = port;
    }

    public void start() {
        while (!Thread.interrupted()) {
            try (ServerSocket serverSocket = new ServerSocket(port)){
                Client.connect(serverSocket.accept());
            } catch (IOException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public static boolean isBlackList(String id) {
        return BLACK_LIST.contains(id);
    }
}
