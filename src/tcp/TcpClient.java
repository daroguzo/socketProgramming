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
            String message = "Hello, World!";

            byte[] lengthBytes = Data.LENGTH_HEADER.getBytes(StandardCharsets.UTF_8);
            byte[] bytes = message.getBytes(StandardCharsets.UTF_8);

            dos.write(lengthBytes);
            dos.write(bytes);
            dos.flush();

            Thread.sleep(2000);

            DataInputStream dis = new DataInputStream(socket.getInputStream());

            byte[] response = new byte[dis.available()];

            byte[] responseLengthBytes = dis.readNBytes(Integer.parseInt(Data.LENGTH_HEADER));
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
