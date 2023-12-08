package chatapp.entity;

import lombok.Getter;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;

@Getter
public class PersonalMessageEntity extends MessageEntity {
    private final String receiverId;

    public PersonalMessageEntity(String sender, String receiverId, String message) {
        super(sender, message);
        this.receiverId = receiverId;
    }

    @Override
    public void setPreparedStatementParameters(PreparedStatement preparedStatement) throws SQLException {
        preparedStatement.setString(1, this.id);
        preparedStatement.setString(2, this.senderId);
        preparedStatement.setString(3, this.receiverId);
        preparedStatement.setString(4, this.message);
        preparedStatement.setTimestamp(5, Timestamp.valueOf(this.createdAt));
    }

    @Override
    public String getRecipientId() {
        return this.receiverId;
    }
}
