package chatapp.service;

import chatapp.entity.MessageEntity;
import chatapp.internal.result.Result;
import chatapp.repository.IMessageRepository;

public class MessageService {
    private final IMessageRepository repo;

    public MessageService(IMessageRepository repo) {
        this.repo = repo;
    }

    public Result<MessageEntity> save(MessageEntity messageEntity) {
        if (messageEntity == null) {
            return Result.failure(new IllegalArgumentException("Message entity cannot be null"));
        }

        if (isNullOrEmpty(messageEntity.getSenderId())) {
            return Result.failure(new IllegalArgumentException("Message sender id cannot be null or empty"));
        }

        if (isNullOrEmpty(messageEntity.getRecipientId())) {
            return Result.failure(new IllegalArgumentException("Message recipient id cannot be null or empty"));
        }

        if (isNullOrEmpty(messageEntity.getMessage())) {
            return Result.failure(new IllegalArgumentException("Message content cannot be null or empty"));
        }

        return repo.save(messageEntity);
    }

    private boolean isNullOrEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }
}
