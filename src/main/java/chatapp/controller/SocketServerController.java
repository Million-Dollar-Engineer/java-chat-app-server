package chatapp.controller;

import chatapp.entity.ConnectionEntity;
import chatapp.service.SocketService;

public class SocketServerController {
    SocketService service;

    public SocketServerController(ConnectionEntity connectionEntity) {
        this.service = new SocketService(connectionEntity);
    }

    public void healthCheck() {
        service.healthCheck();
    }
}
