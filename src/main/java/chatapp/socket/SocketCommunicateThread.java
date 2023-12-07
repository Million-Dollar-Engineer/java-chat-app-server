package chatapp.socket;

import chatapp.controller.SocketServerController;
import chatapp.entity.ConnectionEntity;
import chatapp.internal.logger.AppLogger;

import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SocketCommunicateThread extends Thread {
    private static final Logger logger = AppLogger.getLogger();
    ConnectionEntity connectionEntity;

    public SocketCommunicateThread(Socket clientSocket) {
        try {
            this.connectionEntity = new ConnectionEntity(clientSocket);

        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error creating the connection entity.", e);
        }
    }

    @Override
    public void run() {
        SocketServerController socketController = new SocketServerController(connectionEntity);
        socketController.healthCheck();
    }
}
