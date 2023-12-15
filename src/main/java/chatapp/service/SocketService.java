package chatapp.service;

import chatapp.entity.ConnectionEntity;

public class SocketService {
    ConnectionEntity connectionEntity;

    public SocketService(ConnectionEntity connectionEntity) {
        this.connectionEntity = connectionEntity;
    }

    public void healthCheck() {
        connectionEntity.sendMessage("hello world");
    }
}
