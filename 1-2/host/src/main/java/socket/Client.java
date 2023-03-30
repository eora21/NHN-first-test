package socket;

import handler.Handler;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class Client {
    private Client() {
    }

    public static void connect(Socket socket) throws IOException {
        BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        BufferedWriter output = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        try {
            String connectionMessage = input.readLine().trim();

            if (connectionMessage.contains("@@ connect ")) {
                String clientId = connectionMessage.substring(11);

                if (!Server.isBlackList(clientId) && !Handler.isDuplicateId(clientId)) {
                    output.write("채팅방에 입장하셨습니다.");
                    output.newLine();
                    output.flush();
                    System.out.println(clientId + " 접속");
                    new Thread(new Handler(socket, clientId, input, output)).start();
                    return;
                }

                output.write("접속이 거부되었습니다. Id를 수정하십시오.");
                output.newLine();
                output.flush();
                System.out.println("접속 거부 " + clientId);
                socket.close();
            }
        } catch (NullPointerException e) {
            System.err.println("NPE");
        }

    }
}
