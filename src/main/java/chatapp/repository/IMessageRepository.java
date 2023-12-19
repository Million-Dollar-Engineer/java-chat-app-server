package chatapp.repository;

import chatapp.dto.MessageHistoryResponse;
import chatapp.entity.MessageEntity;
import chatapp.internal.result.Result;

import java.util.List;

public interface IMessageRepository {

    Result<MessageEntity> save(MessageEntity messageEntity);

    Result<List<MessageHistoryResponse>> findMessages(MessageEntity messageEntity);
}
