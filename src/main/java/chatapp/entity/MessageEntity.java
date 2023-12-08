package chatapp.entity;


import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class MessageEntity {
    private final LocalDateTime timestamp = LocalDateTime.now();

    private final String sender; // username of the sender

    private final String receiver; // username of the receiver

    private final String message; // message content

    public MessageEntity(String sender, String receiver, String message) {
        this.sender = sender;
        this.receiver = receiver;
        this.message = message;
    }
}