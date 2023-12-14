package chatapp.entity;


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

    public MessageEntity(String id, String senderId, String message, LocalDateTime createdAt) {
        this.id = (id != null && !id.isEmpty()) ? id : java.util.UUID.randomUUID().toString();
        this.senderId = Objects.requireNonNull(senderId, "senderId must not be null");
        this.message = Objects.requireNonNull(message, "message must not be null");
        this.createdAt = (createdAt != null) ? createdAt : LocalDateTime.now();
    }

    public abstract void setPreparedStatementParameters(PreparedStatement preparedStatement) throws SQLException;

    public abstract String getRecipientId();

    public abstract MessageEntity mapRowToEntity(ResultSet rs);

    public abstract List<MessageEntity> mapRSToListEntity(ResultSet rs) throws SQLException;
}