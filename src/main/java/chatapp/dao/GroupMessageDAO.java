package chatapp.dao;

import chatapp.dto.MessageHistoryResponse;
import chatapp.entity.GroupMessageEntity;
import chatapp.entity.MessageEntity;
import chatapp.internal.result.Result;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class GroupMessageDAO implements MessageDAO {
    private final Connection connection;

    public GroupMessageDAO(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Result<MessageEntity> save(MessageEntity messageEntity) {
        String query = "INSERT INTO group_messages (id, sender_id, group_id, message, created_at) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            messageEntity.setPreparedStatementParameters(preparedStatement);
            preparedStatement.executeUpdate();
            return Result.success(messageEntity);
        } catch (SQLException e) {
            return Result.failure(e);
        }
    }

    @Override
    public Result<List<MessageHistoryResponse>> findMessages(MessageEntity messageEntity) {
        // get user full name
        String query = "";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, messageEntity.getRecipientId());
            preparedStatement.executeQuery();
            ResultSet resultSet = preparedStatement.getResultSet();

            List<MessageEntity> messages = GroupMessageEntity.mapRSToListEntity(resultSet);
            return null;
        } catch (SQLException e) {
            return Result.failure(e);
        }
    }
}
