package chatapp.entity;


import lombok.Getter;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
public abstract class MessageEntity {
    protected final String id = UUID.randomUUID().toString();
    protected final LocalDateTime createdAt = LocalDateTime.now();
    protected final String senderId; // username of the sender
    protected final String message; // message content

    public MessageEntity(String senderId, String message) {
        this.senderId = senderId;
        this.message = message;
    }

    public abstract void setPreparedStatementParameters(PreparedStatement preparedStatement) throws SQLException;
}