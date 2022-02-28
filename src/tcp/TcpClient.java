package tcp;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class TcpClient {
    private static final int SERVER_PORT = 8000;

    public static void main(String[] args) {
        Socket socket = null;

        try {
            socket = new Socket();
            System.out.println("[연결 요청]");
            socket.connect(new InetSocketAddress("localhost", SERVER_PORT));
            System.out.println("[연결 성공]");

            DataOutputStream dos = new DataOutputStream(socket.getOutputStream());

            String message = "Java Socket!";
            byte[] sendBytes = message.getBytes(StandardCharsets.UTF_8);

            dos.writeInt(sendBytes.length);
            dos.write(sendBytes);
            dos.flush();

            Thread.sleep(1000);

            DataInputStream dis = new DataInputStream(socket.getInputStream());
            int responseMessageLength = dis.readInt();

            byte[] responseBytes = new byte[responseMessageLength];
            dis.readFully(responseBytes, 0, responseMessageLength);

            String responseMessage = new String(responseBytes, 0, responseMessageLength, StandardCharsets.UTF_8);
            System.out.println("[Message: " + responseMessage + "]");

            dos.close();
            dis.close();

            socket.close();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        if (!socket.isClosed()) {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
