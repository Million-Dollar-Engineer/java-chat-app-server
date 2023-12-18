package chatapp.controller;

import chatapp.entity.GroupMessageEntity;
import chatapp.entity.MessageEntity;
import chatapp.entity.PersonalMessageEntity;
import chatapp.internal.result.Result;
import chatapp.repository.IMessageRepository;
import chatapp.repository.impl.MessageRepository;
import chatapp.service.MessageService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/message")
public class MessageController {
    private final MessageService service;

    public MessageController() {
        IMessageRepository repository = new MessageRepository();
        this.service = new MessageService(repository);
    }

    public void saveMessage(MessageEntity messageEntity) {
        service.save(messageEntity);
    }

    @GetMapping("/personal-history")
    public List<MessageEntity> personalHistory(@RequestParam String senderId,
                                               @RequestParam String receiverUsername) throws Exception {

        String receiverId = UserController.getUserIdByUsername(receiverUsername);
        MessageEntity messageEntity = new PersonalMessageEntity("", senderId, receiverId, "", null);
        Result<List<MessageEntity>> result = service.findMessages(messageEntity);

        if (result.getError() != null) {
            throw result.getError();
        }

        for (MessageEntity message : result.getObject()) {
            PersonalMessageEntity personalMessage = (PersonalMessageEntity) message;
            System.out.println(personalMessage.getMessage());
        }

        return result.getObject();
    }

    @GetMapping("/group-history")
    public List<MessageEntity> groupHistory(@RequestParam String id) throws Exception {

        MessageEntity messageEntity = new GroupMessageEntity("", "", id, "", null);

        Result<List<MessageEntity>> result = service.findMessages(messageEntity);

        if (result.getError() != null) {
            throw result.getError();
        }

        return result.getObject();
    }
}
