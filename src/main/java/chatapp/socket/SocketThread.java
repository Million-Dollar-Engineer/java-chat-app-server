package chatapp.socket;

import chatapp.controller.MessageController;
import chatapp.entity.ConnectionEntity;
import chatapp.entity.GroupMessageEntity;
import chatapp.entity.MessageEntity;
import chatapp.entity.PersonalMessageEntity;
import chatapp.internal.logger.AppLogger;

import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SocketThread extends Thread {
    private static final Logger logger = AppLogger.getLogger();
    ConnectionEntity client;

    public SocketThread(Socket clientSocket) {
        try {
            this.client = new ConnectionEntity(clientSocket);

        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error creating the connection entity.", e);
        }
    }

    @Override
    public void run() {
        MessageController messageController = new MessageController();
        do {
            try {
                String header = client.readMessage();
                switch (header) {
                    case "msg to user": {
                        String recipient = client.readMessage();
                        String message = client.readMessage();
                        MessageEntity messageEntity = new PersonalMessageEntity("",
                                client.getUsername(), recipient, message, null);

                        messageController.saveMessage(messageEntity);
                        break;
                    }
                    case "msg to group": {
                        String group = client.readMessage();
                        String message = client.readMessage();

                        MessageEntity messageEntity = new GroupMessageEntity("",
                                client.getUsername(), group, message, null);

                        messageController.saveMessage(messageEntity);
                        break;
                    }
                }
            } catch (Exception e) {
                client.close();
                logger.log(Level.SEVERE, "Error reading message from client.", e);
            }
        } while (!client.isClosed());
    }
}
