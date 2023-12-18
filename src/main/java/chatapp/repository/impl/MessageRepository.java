package chatapp.repository.impl;

import chatapp.dao.GroupMessageDAO;
import chatapp.dao.MessageDAO;
import chatapp.dao.PersonalMessageDAO;
import chatapp.dto.MessageHistoryResponse;
import chatapp.entity.GroupMessageEntity;
import chatapp.entity.MessageEntity;
import chatapp.entity.PersonalMessageEntity;
import chatapp.internal.database.Postgres;
import chatapp.internal.result.Result;
import chatapp.repository.IMessageRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MessageRepository implements IMessageRepository {
    private final Map<Class<? extends MessageEntity>, MessageDAO> daoMap = new HashMap<>();

    public MessageRepository() {
        Postgres database = Postgres.getInstance();
        this.daoMap.put(PersonalMessageEntity.class, new PersonalMessageDAO(database.getConnection()));
        this.daoMap.put(GroupMessageEntity.class, new GroupMessageDAO(database.getConnection()));
    }

    @Override
    public Result<MessageEntity> save(MessageEntity messageEntity) {
        MessageDAO messageDAO = createMessageDAO(messageEntity);
        return messageDAO.save(messageEntity);
    }

    @Override
    public Result<List<MessageHistoryResponse>> findMessages(MessageEntity messageEntity) {
        MessageDAO messageDAO = createMessageDAO(messageEntity);
        return messageDAO.findMessages(messageEntity);
    }

    private MessageDAO createMessageDAO(MessageEntity messageEntity) {
        MessageDAO messageDAO = daoMap.get(messageEntity.getClass());
        if (messageDAO == null) {
            throw new IllegalArgumentException("Unsupported message entity type: " + messageEntity.getClass().getName());
        }
        return messageDAO;
    }
}
