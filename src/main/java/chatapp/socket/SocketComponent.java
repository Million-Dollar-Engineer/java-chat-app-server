package chatapp.socket;

import chatapp.entity.ConnectionEntity;
import chatapp.internal.logger.AppLogger;
import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

@Component
public class SocketComponent implements Runnable {
    private static final Logger logger = AppLogger.getLogger();
    private static final Dotenv dotenv = Dotenv.load();
    private static final int portNumber = Integer.parseInt(Objects.requireNonNull(dotenv.get("SOCKET_PORT")));

    public static ArrayList<ConnectionEntity> connections = new ArrayList<>();
    private ServerSocket serverSocket;

    public void openServerSocket() {
        try {
            this.serverSocket = new ServerSocket(portNumber);
            logger.log(Level.INFO, "Server started. Listening on port " + portNumber);

            new Thread(() -> {
                try {
                    do {
                        Socket clientSocket = serverSocket.accept();
                        logger.log(Level.INFO, "Client connected: " + clientSocket);
                        SocketThread clientCommunicator = new SocketThread(clientSocket);
                        clientCommunicator.start();
                    } while (this.serverSocket != null && !this.serverSocket.isClosed());
                } catch (IOException e) {
                    logger.log(Level.SEVERE, "Error accepting client connection.", e);
                }
            }).start();

        } catch (Exception e) {
            closeServerSocket();
            logger.log(Level.SEVERE, "Error starting the server.", e);
        }
    }

    public void closeServerSocket() {
        try {
            if (this.serverSocket != null && !this.serverSocket.isClosed()) {
                this.serverSocket.close();
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error closing the server.", e);
        }
    }

    @Override
    public void run() {
        openServerSocket();
    }
}
