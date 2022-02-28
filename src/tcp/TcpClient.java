package tcp;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

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

            OutputStream os = socket.getOutputStream();
            message = "Hello, World!";
            bytes = message.getBytes("UTF-8");

            os.write(bytes);
            os.flush();

            InputStream is = socket.getInputStream();

            Thread.sleep(2000);

            System.out.println(is.available());
            byte[] response = new byte[is.available()];

            int readByteCount = is.read(response);
            String responseMessage = new String(response, 0, readByteCount, "UTF-8");
            System.out.println("[Message: " + responseMessage + "]");

            os.close();
            is.close();

            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
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
