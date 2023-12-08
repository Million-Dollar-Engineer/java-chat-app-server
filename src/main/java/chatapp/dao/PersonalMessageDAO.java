package chatapp.dao;

import chatapp.entity.MessageEntity;
import chatapp.internal.result.Result;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

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
}
