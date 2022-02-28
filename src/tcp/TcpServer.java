package tcp;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class TcpServer {
    private static final int SERVER_PORT = 8000;

    public static void main(String[] args) {
        ServerSocket serverSocket = null;

        try {
            serverSocket = new ServerSocket(SERVER_PORT);

            while (true) {
                System.out.println("[연결 대기]");

                Socket socket = serverSocket.accept();
                System.out.println("[접속된 IP: " + socket.getInetAddress() + "]");

                DataInputStream dis = new DataInputStream(socket.getInputStream());
                int responseMessageLength = dis.readInt();

                byte[] responseBytes = new byte[responseMessageLength];
                dis.readFully(responseBytes, 0, responseMessageLength);

                String responseMessage = new String(responseBytes, 0, responseMessageLength, StandardCharsets.UTF_8);
                System.out.println("[Message: " + responseMessage + "]");

                DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
                byte[] sendBytes = responseMessage.getBytes(StandardCharsets.UTF_8);

                dos.writeInt(sendBytes.length);
                dos.write(sendBytes);
                dos.flush();

                dos.close();
                dis.close();

                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (!serverSocket.isClosed()) {
            try {
                serverSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
