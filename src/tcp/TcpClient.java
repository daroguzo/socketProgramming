package tcp;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class TcpClient {
    private static final String SERVER_IP = "192.168.1.200";
    private static final int SERVER_PORT = 20010;
    private static final int FIXED_LENGTH = 10;

    public static void main(String[] args) {
        Socket socket = null;

        try {
            socket = new Socket();
            System.out.println("[연결 요청]");
            socket.connect(new InetSocketAddress(SERVER_IP, SERVER_PORT));
            System.out.println("[연결 성공]");

            DataOutputStream dos = new DataOutputStream(socket.getOutputStream());

            String interfaceId = "0100";
            String message = "Java Socket!";
            String data = interfaceId + message;
            // 데이터 자릿수
            int dataLength = (int)(Math.log10(data.length()) + 1);

            // 길이부 0 채우기
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < (FIXED_LENGTH - dataLength); i++) {
                sb.append("0");
            }
            sb.append(data.length());

            System.out.println(sb + data);

            byte[] lengthBytes = sb.toString().getBytes(StandardCharsets.UTF_8);
            byte[] interfaceBytes = interfaceId.getBytes(StandardCharsets.UTF_8);
            byte[] sendMessageBytes = message.getBytes(StandardCharsets.UTF_8);

            dos.write(lengthBytes);
            dos.write(interfaceBytes);
            dos.write(sendMessageBytes);
            dos.flush();

            Thread.sleep(1000);

            DataInputStream dis = new DataInputStream(socket.getInputStream());

            byte[] responseLengthBytes = new byte[FIXED_LENGTH];
            dis.readFully(responseLengthBytes);
            String lengthBytesString = new String(responseLengthBytes);

            byte[] responseBytes = new byte[Integer.parseInt(lengthBytesString)];
            int read = dis.read(responseBytes);
            String responseData = new String(responseBytes, 0, read, StandardCharsets.UTF_8);

            String responseInterfaceId = responseData.substring(0, 4);
            String responseMessage = responseData.substring(4);

            System.out.println("length/" + read +
                    "/interfaceId/" + responseInterfaceId +
                    "/data/" + responseMessage);
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
