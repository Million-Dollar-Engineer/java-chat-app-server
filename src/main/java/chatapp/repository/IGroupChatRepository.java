package chatapp.repository;

import java.sql.SQLException;

public interface IGroupChatRepository {
    public String getGroupChatListByAdmin(String sortBy, String name) throws SQLException;
    public String getGroupChatMember(String group_id) throws SQLException;

}
