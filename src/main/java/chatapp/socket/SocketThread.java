package chatapp.socket;

import chatapp.controller.MessageController;
import chatapp.controller.UserController;
import chatapp.entity.ConnectionEntity;
import chatapp.entity.GroupMessageEntity;
import chatapp.entity.MessageEntity;
import chatapp.entity.PersonalMessageEntity;
import chatapp.internal.logger.AppLogger;

import java.net.Socket;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SocketThread extends Thread {
    private static final Logger logger = AppLogger.getLogger();
    ConnectionEntity client;

    public SocketThread(Socket clientSocket) {
        try {
            this.client = new ConnectionEntity(clientSocket);
            SocketComponent.connections.add(this.client);
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

                if (header == null) {
                    for (int i = 0; i < SocketComponent.connections.size(); i++) {
                        if (SocketComponent.connections.get(i).getUserId().equals(client.getUserId())) {
                            SocketComponent.connections.remove(i);
                            break;
                        }
                    }
                    client.close();
                    break;
                }

                switch (Objects.requireNonNull(header)) {
                    case "msg to user": {
                        String recipient = UserController.getUserIdByUsername(client.readMessage());
                        String message = client.readMessage();

                        System.out.println("recipient: " + recipient);
                        System.out.println("message: " + message);
                        MessageEntity messageEntity = new PersonalMessageEntity("",
                                client.getUserId(), recipient, message, null);

                        messageController.saveMessage(messageEntity);

                        for (ConnectionEntity connection : SocketComponent.connections) {
                            if (connection.getUserId().equals(recipient)) {
                                connection.sendMessage(header);
                                connection.sendMessage(UserController.getUsernameById(client.getUserId()));
                                connection.sendMessage(message);

                                System.out.println("sent message to user" + connection.getUserId());
                                break;
                            }
                        }
                        break;
                    }
                    case "msg to group": {
                        String group = client.readMessage();
                        String message = client.readMessage();

                        MessageEntity messageEntity = new GroupMessageEntity("",
                                client.getUserId(), group, message, null);

                        messageController.saveMessage(messageEntity);

                        for (ConnectionEntity connection : SocketComponent.connections) {
                            if (UserController.isUserInGroup(connection.getUserId(), group)) {
                                connection.sendMessage(header);
                                connection.sendMessage(group);
                                connection.sendMessage(UserController.getUsernameById(client.getUserId()));
                                connection.sendMessage(message);
                            }
                        }
                        break;
                    }
                }
            } catch (Exception e) {
                for (int i = 0; i < SocketComponent.connections.size(); i++) {
                    if (SocketComponent.connections.get(i).getUserId().equals(client.getUserId())) {
                        SocketComponent.connections.remove(i);
                        break;
                    }
                }
                client.close();
                logger.log(Level.SEVERE, "Error reading message from client.", e);
                break;
            }
        } while (!client.isClosed());
    }
}
