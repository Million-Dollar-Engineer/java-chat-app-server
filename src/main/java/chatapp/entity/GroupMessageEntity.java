package chatapp.entity;

import lombok.Getter;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;

@Getter
public class GroupMessageEntity extends MessageEntity {
    private final String groupId;

    public GroupMessageEntity(String sender, String groupId, String message) {
        super(sender, message);
        this.groupId = groupId;
    }

    @Override
    public void setPreparedStatementParameters(PreparedStatement preparedStatement) throws SQLException {
        preparedStatement.setString(1, this.id);
        preparedStatement.setString(2, this.senderId);
        preparedStatement.setString(3, this.groupId);
        preparedStatement.setString(4, this.message);
        preparedStatement.setTimestamp(5, Timestamp.valueOf(this.createdAt));
    }
}
