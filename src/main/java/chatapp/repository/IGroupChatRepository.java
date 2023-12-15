package chatapp.repository;

import java.sql.SQLException;

public interface IGroupChatRepository {
    public String getGroupChatListByAdmin(String sortBy) throws SQLException;

}
