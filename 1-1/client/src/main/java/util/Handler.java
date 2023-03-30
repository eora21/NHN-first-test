package util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.HashSet;
import java.util.Set;

public class Handler {
    private final Set<String> blackList;
    private final BufferedReader input;
    private final BufferedWriter output;
    private final BufferedReader messageFromSocket;
    private final BufferedWriter messageToSocket;

    public Handler(Socket socket) throws IOException {
        blackList = new HashSet<>();
        input = new BufferedReader(new InputStreamReader(System.in));
        output = new BufferedWriter(new OutputStreamWriter(System.out));
        messageFromSocket = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        messageToSocket = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
    }

    public void start(String userId) throws IOException {
        send(messageToSocket, "@@ connect " + userId);

        Thread thread = new Thread(
                new Messageable(input, messageToSocket, this::checkClientOpt, this::doClientOpt));
        thread.setDaemon(true);
        thread.start();

        new Messageable(messageFromSocket, output, this::checkUserId, this::ignoreMessage).run();
    }

    private static void send(BufferedWriter bufferedWriter, String message) throws IOException {
        bufferedWriter.write(message);
        bufferedWriter.newLine();
        bufferedWriter.flush();
    }

    private boolean checkClientOpt(String message) {
        return message.length() > 2 && message.charAt(0) == '!';
    }

    private void doClientOpt(String message) throws IOException {
        String opt = message.substring(1);

        if (opt.length() > 3 && "차단".equals(opt.substring(0, 2))) {
            String bannUser = opt.substring(3);
            blackList.add(bannUser);
            send(output,bannUser + "을(를) 차단합니다.");
            return;
        }

        send(output,"허용되지 않은 명령어입니다.");
    }

    private boolean checkUserId(String message) {
        String userId = message.substring(1, message.indexOf(" "));
        return blackList.contains(userId);
    }

    private void ignoreMessage(String message) {

    }
}
