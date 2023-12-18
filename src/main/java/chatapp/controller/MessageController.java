package chatapp.controller;

import chatapp.entity.MessageEntity;
import chatapp.repository.IMessageRepository;
import chatapp.repository.impl.MessageRepository;
import chatapp.service.MessageService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

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
    public ResponseEntity<Map<String, String>> personalHistory(@RequestParam String senderId,
                                                               @RequestParam String receiverUsername) {
        System.out.println();
        return null;
    }

    @GetMapping("/group-history")
    public ResponseEntity<Map<String, String>> groupHistory(@RequestParam String id) {

        return null;
    }
}
