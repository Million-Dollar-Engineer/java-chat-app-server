package chatapp.dao;

import chatapp.entity.MessageEntity;
import chatapp.internal.result.Result;

public interface MessageDAO {

    Result<MessageEntity> save(MessageEntity messageEntity);
}
