package chatapp.dao;

import chatapp.dto.MessageHistoryResponse;
import chatapp.entity.MessageEntity;
import chatapp.internal.result.Result;

import java.util.List;

public interface MessageDAO {

    Result<MessageEntity> save(MessageEntity messageEntity);

    Result<List<MessageHistoryResponse>> findMessages(MessageEntity messageEntity);
}
