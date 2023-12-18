package chatapp.dao;

import chatapp.entity.MessageEntity;
import chatapp.entity.PersonalMessageEntity;
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
    public Result<List<MessageEntity>> findMessages(MessageEntity messageEntity) {
        String query = "SELECT * FROM personal_messages WHERE (sender_id = ? AND receiver_id = ?) OR (sender_id = ? AND receiver_id = ?) ORDER BY created_at DESC ";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, messageEntity.getSenderId());
            preparedStatement.setString(2, messageEntity.getRecipientId());
            preparedStatement.setString(3, messageEntity.getRecipientId());
            preparedStatement.setString(4, messageEntity.getSenderId());

            ResultSet resultSet = preparedStatement.executeQuery();
            return Result.success(PersonalMessageEntity.mapRSToListEntity(resultSet));
        } catch (SQLException e) {
            return Result.failure(e);
        }
    }
}
