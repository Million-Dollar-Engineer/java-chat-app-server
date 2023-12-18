package chatapp.entity;


import chatapp.security.AESEncryption;
import lombok.Getter;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Getter
public abstract class MessageEntity {
    protected final String senderId; // username of the sender
    protected final String message; // message content

    protected String id; // unique id of the message
    protected LocalDateTime createdAt; // time when the message was sent

    public MessageEntity(String id, String senderId, String message, LocalDateTime createdAt) throws Exception {
        this.id = (id != null && !id.isEmpty()) ? id : java.util.UUID.randomUUID().toString();
        this.senderId = Objects.requireNonNull(senderId, "senderId must not be null");
        this.message = Objects.requireNonNull(AESEncryption.encrypt(message), "message must not be null");
        this.createdAt = (createdAt != null) ? createdAt : LocalDateTime.now();
    }

    public static List<MessageEntity> mapRSToListEntity(ResultSet rs) throws SQLException {
        return null;
    }

    public static MessageEntity mapRowToEntity(ResultSet rs) {
        return null;
    }

    public abstract void setPreparedStatementParameters(PreparedStatement preparedStatement) throws SQLException;

    public abstract String getRecipientId();
}