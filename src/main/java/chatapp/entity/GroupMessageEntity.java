package chatapp.entity;

import lombok.Getter;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
public class GroupMessageEntity extends MessageEntity {
    private final String groupId;

    public GroupMessageEntity(String id, String sender, String groupId, String message, LocalDateTime createdAt) {
        super(id, sender, message, createdAt);
        this.groupId = groupId;
    }

    public GroupMessageEntity(String groupId) {
        super("", "", "", null);
        this.groupId = groupId;
    }

    public static List<MessageEntity> mapRSToListEntity(ResultSet rs) throws SQLException {
        List<MessageEntity> list = new ArrayList<>();

        while (rs.next()) {
            MessageEntity entity = mapRowToEntity(rs);
            list.add(entity);
        }

        return list;
    }

    public static MessageEntity mapRowToEntity(ResultSet rs) {
        try {
            return new GroupMessageEntity(
                    rs.getString("id"),
                    rs.getString("sender_id"),
                    rs.getString("group_id"),
                    rs.getString("message"),
                    rs.getTimestamp("created_at").toLocalDateTime()
            );
        } catch (SQLException e) {
            return null;
        }
    }

    @Override
    public String getRecipientId() {
        return this.groupId;
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
