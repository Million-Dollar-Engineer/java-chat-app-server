package chatapp.dao;

import chatapp.dto.MessageHistoryResponse;
import chatapp.entity.MessageEntity;
import chatapp.internal.result.Result;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class PersonalMessageDAO implements MessageDAO {
    private final Connection connection;

    public PersonalMessageDAO(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Result<MessageEntity> save(MessageEntity messageEntity) {
        String query = "INSERT INTO personal_messages (id, sender_id, receiver_id, message, created_at) VALUES (?, ?, ?, ?, ?)";
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
        // Get user full name of sender and receiver, message and created_at
        String query = "SELECT pm.id, pm.sender_id, pm.receiver_id, pm.message, pm.created_at, u1.full_name" +
                " AS sender_full_name, u2.full_name AS receiver_full_name FROM personal_messages pm" +
                " INNER JOIN users u1 ON pm.sender_id = u1.id INNER JOIN users u2 ON pm.receiver_id = u2.id " +
                "WHERE (pm.sender_id = ? AND pm.receiver_id = ?) OR (pm.sender_id = ? AND pm.receiver_id = ?) " +
                "ORDER BY pm.created_at ASC";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, messageEntity.getSenderId());
            preparedStatement.setString(2, messageEntity.getRecipientId());
            preparedStatement.setString(3, messageEntity.getRecipientId());
            preparedStatement.setString(4, messageEntity.getSenderId());

            ResultSet resultSet = preparedStatement.executeQuery();

            List<MessageHistoryResponse> messageHistoryResponses = MessageHistoryResponse.fromResultSet(resultSet);

            return Result.success(messageHistoryResponses);
        } catch (Exception e) {
            return Result.failure(e);
        }
    }
}
