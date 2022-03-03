package tcp;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class TcpServer {
    private static final int FIXED_LENGTH = 10;
    private static int SERVER_PORT;
    private static final String LOG_PATH = "./log/socketProgramming.log";

    public static void main(String[] args) {
        Logger logger = Logger.getLogger(TcpServer.class.getName());
        FileHandler fileHandler;
        ServerSocket serverSocket = null;

        // check args options
        if (args.length != 1) {
            System.out.println("옵션을 입력하세요");
            System.exit(1);
        }
        SERVER_PORT = Integer.parseInt(args[0]);

        try {
            // logger setting
            fileHandler = new FileHandler(LOG_PATH, 1024*1024, 10, true);
            logger.addHandler(fileHandler);
            SimpleFormatter simpleFormatter = new SimpleFormatter();
            fileHandler.setFormatter(simpleFormatter);

            serverSocket = new ServerSocket(SERVER_PORT);
            logger.log(Level.INFO, "Server open");

            while (true) {
                System.out.println("[연결 대기]");

                Socket socket = serverSocket.accept();
                System.out.println("[접속된 IP: " + socket.getInetAddress() + "]");

                DataInputStream dis = new DataInputStream(socket.getInputStream());

                byte[] lengthBytes = new byte[FIXED_LENGTH];
                dis.readFully(lengthBytes);
                String lengthBytesString = new String(lengthBytes);

                byte[] responseBytes = new byte[Integer.parseInt(lengthBytesString)];
                int read = dis.read(responseBytes);
                String responseData = new String(responseBytes, 0, read, StandardCharsets.UTF_8);

                String interfaceId = responseData.substring(0, 4);
                String data = responseData.substring(4);

                System.out.println("[Message: " + data + "]");

                logger.log(Level.INFO, "Message Receive");
                logger.log(Level.INFO, "length/" + read +
                                "/interfaceId/" + interfaceId +
                                "/data/" + responseData
                        );

                DataOutputStream dos = new DataOutputStream(socket.getOutputStream());

                byte[] sendBytes = responseData.getBytes(StandardCharsets.UTF_8);

                dos.write(lengthBytes);
                dos.write(sendBytes);
                dos.flush();

                dos.close();
                dis.close();

                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
            logger.log(Level.WARNING, "Running IOException");
        }

        if (!serverSocket.isClosed()) {
            try {
                serverSocket.close();
                logger.log(Level.INFO, "Server close");
            } catch (IOException e) {
                e.printStackTrace();
                logger.log(Level.WARNING, "Closing IOException");
            }
        }
    }
}
