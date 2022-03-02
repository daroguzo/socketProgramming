package tcp;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class TcpClient {
    private static final String SERVER_IP = "192.168.1.200";
    private static final int SERVER_PORT = 8000;
    private static final int FIXED_LENGTH = 10;

    public static void main(String[] args) {
        Socket socket = null;

        try {
            socket = new Socket();
            System.out.println("[연결 요청]");
            socket.connect(new InetSocketAddress(SERVER_IP, SERVER_PORT));
            System.out.println("[연결 성공]");

            DataOutputStream dos = new DataOutputStream(socket.getOutputStream());

            StringBuilder sb = new StringBuilder();
            String message = "Java Socket!";
            int messageLength = (int)(Math.log10(message.length()) + 1);

            for (int i = 0; i < (10 - messageLength); i++) {
                sb.append("0");
            }
            sb.append(message.length());

            byte[] lengthBytes = sb.toString().getBytes(StandardCharsets.UTF_8);
            byte[] sendMessageBytes = message.getBytes(StandardCharsets.UTF_8);

            dos.write(lengthBytes);
            dos.write(sendMessageBytes);
            dos.flush();

            Thread.sleep(1000);

            DataInputStream dis = new DataInputStream(socket.getInputStream());

            byte[] responseLengthBytes = new byte[FIXED_LENGTH];
            dis.readFully(responseLengthBytes,0 , FIXED_LENGTH);
            String lengthBytesString = new String(responseLengthBytes);

            byte[] responseBytes = new byte[Integer.parseInt(lengthBytesString)];
            int read = dis.read(responseBytes);

            String responseMessage = new String(responseBytes, 0, read, StandardCharsets.UTF_8);
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
