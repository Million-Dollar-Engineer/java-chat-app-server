package chatapp.controller;

import chatapp.entity.MessageEntity;
import chatapp.entity.PersonalMessageEntity;
import chatapp.internal.result.Result;
import chatapp.repository.IMessageRepository;
import chatapp.repository.impl.MessageRepository;
import chatapp.service.MessageService;

public class MessageController {
    private final MessageService service;

    public MessageController() {
        IMessageRepository repository = new MessageRepository();
        this.service = new MessageService(repository);
    }

    public static void main(String[] args) {
        MessageController controller = new MessageController();
        controller.testSaveMessage();
    }

    void testSaveMessage() {
        MessageEntity messageEntity = new PersonalMessageEntity(
                null,
                "user1",
                "user2",
                "Hello, user2!",
                null
        );
        
        Result<MessageEntity> result = service.save(messageEntity);
        if (result.getError() != null) {
            System.out.println("Error occurred: " + result.getError().getMessage());
        } else {
            System.out.println("Message saved: " + result.getObject().getCreatedAt());
        }
    }
}
