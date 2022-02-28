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

            byte[] bytes = null;
            String message = null;

            DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
            message = "Hello, World!";
            bytes = message.getBytes(StandardCharsets.UTF_8);

            dos.write(bytes);
            dos.flush();

            DataInputStream dis = new DataInputStream(socket.getInputStream());

            Thread.sleep(2000);

            System.out.println(dis.available());
            byte[] response = new byte[dis.available()];

            int readByteCount = dis.read(response);
            String responseMessage = new String(response, 0, readByteCount, StandardCharsets.UTF_8);
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
