package tcp;

import jdk.swing.interop.SwingInterOpUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

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

                byte[] bytes = null;
                String message = null;

                InputStream is = socket.getInputStream();
                bytes = new byte[is.available()];

                int readByteCount = is.read(bytes);
                message = new String(bytes, 0, readByteCount, "UTF-8");

                System.out.println("[Message: " + message + "]");

                OutputStream os = socket.getOutputStream();
                bytes = message.getBytes("UTF-8");

                os.write(bytes);
                os.flush();

                is.close();
                os.close();
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
