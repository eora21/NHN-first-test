package handler;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.net.Socket;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class Handler implements Runnable {
    private static final Map<String, Handler> HANDLERS = new HashMap<>();
    public static final String SYSTEM = "@";
    private final LocalDateTime enterTime;
    private final Socket socket;
    private final String clientId;
    private final BufferedReader input;
    private final BufferedWriter output;
    private String chatOption;

    public Handler(Socket socket, String clientId, BufferedReader input, BufferedWriter output) {
        this.enterTime = LocalDateTime.now();
        this.socket = socket;
        this.clientId = clientId;
        this.input = input;
        this.output = output;
        this.chatOption = "all";

        synchronized (HANDLERS) {
            HANDLERS.put(clientId, this);
        }
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                String clientMessage = input.readLine().trim();

                if (clientMessage.equals("!끝")) {
                    socket.close();
                    break;
                }

                if (clientMessage.length() > 3 && "@@".equals(clientMessage.substring(0, 2))) {
                    getInformation(clientMessage.substring(3));
                    continue;
                }

                if (clientMessage.length() > 3 && "@".equals(clientMessage.substring(0, 1))) {
                    int blankIdx = clientMessage.indexOf(" ");
                    this.chatOption = clientMessage.substring(1, blankIdx);
                    clientMessage = clientMessage.substring(blankIdx + 1);
                }

                sendMessage(this, clientMessage);
            } catch (IOException | NullPointerException e) {
                Thread.currentThread().interrupt();
            }
        }
        HANDLERS.remove(clientId);
    }

    private void getInformation(String opt) throws IOException {
        if ("userlist".equals(opt)) {
            getMessage(SYSTEM, "userlist " + String.join(", ", HANDLERS.keySet()));
            return;
        }

        if ("time".equals(opt)) {
            getMessage(SYSTEM, String.valueOf(LocalDateTime.now()));
            return;
        }

        if ("accesstime".equals(opt)) {
            getMessage(SYSTEM, String.valueOf(this.enterTime));
            return;
        }

        getMessage(SYSTEM, "명령어가 잘못되었습니다.");
    }

    private static void sendMessage(Handler handler, String message) throws IOException {
        if ("all".equals(handler.chatOption)) {
            broadCast(handler.clientId, message);
            return;
        }

        whispering(handler, handler.chatOption, message);
    }

    private static void broadCast(String clientId, String message) throws IOException {
        synchronized (HANDLERS) {
            for (Handler handler : HANDLERS.values()) {
                handler.getMessage(clientId, message);
            }
        }
    }

    private static void whispering(Handler fromClient, String toClientId, String message) throws IOException {
        Handler handler = HANDLERS.get(toClientId);

        if (handler == null) {
            fromClient.getMessage(SYSTEM, "존재하지 않는 유저입니다.");
            return;
        }
        message = "(귓속말) " + message;
        fromClient.getMessage(fromClient.clientId, message);
        handler.getMessage(fromClient.clientId, message);
    }

    private void getMessage(String clientId, String message) throws IOException {
        output.write(String.format("#%s %s", clientId, message));
        output.newLine();
        output.flush();
    }

    public static boolean isDuplicateId(String clientId) {
        return HANDLERS.containsKey(clientId);
    }
}

