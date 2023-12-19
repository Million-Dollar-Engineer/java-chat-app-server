package chatapp.dto;

import chatapp.security.AESEncryption;
import lombok.Getter;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

@Getter
public class MessageHistoryResponse {

    private final String senderId;
    private final String senderName;
    private final String receiverId;
    private final String receiverName;
    private final String message;
    private final String createdAt;

    public MessageHistoryResponse(String senderId, String senderName, String receiverId, String receiverName, String message, String createdAt) {
        this.senderId = senderId;
        this.senderName = senderName;
        this.receiverId = receiverId;
        this.receiverName = receiverName;
        this.message = message;
        this.createdAt = createdAt;
    }

    public static List<MessageHistoryResponse> fromResultSet(ResultSet resultSet) throws Exception {
        List<MessageHistoryResponse> messageHistoryResponses = new ArrayList<>();
        while (resultSet.next()) {
            String senderId = resultSet.getString("sender_id");
            String senderName = resultSet.getString("sender_full_name");
            String receiverId = resultSet.getString("receiver_id");
            String receiverName = resultSet.getString("receiver_full_name");
            String message = AESEncryption.decrypt(resultSet.getString("message"));
            String createdAt = resultSet.getString("created_at");


            MessageHistoryResponse messageHistoryResponse = new MessageHistoryResponse(senderId, senderName,
                    receiverId, receiverName, message, createdAt);

            messageHistoryResponses.add(messageHistoryResponse);
        }

        return messageHistoryResponses;
    }
}
