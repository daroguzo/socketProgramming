package tcp;

import jdk.swing.interop.SwingInterOpUtils;

import javax.xml.crypto.Data;
import java.io.*;
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
                byte[] bytes = new byte[dis.available()];

                int readByteCount = dis.read(bytes);
                String message = new String(bytes, 0, readByteCount, StandardCharsets.UTF_8);

                System.out.println("[Message: " + message + "]");

                DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
                bytes = message.getBytes(StandardCharsets.UTF_8);

                dos.write(bytes);
                dos.flush();

                dis.close();
                dos.close();
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
