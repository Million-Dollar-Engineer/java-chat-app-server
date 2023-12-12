package chatapp.repository;


import chatapp.entity.UserEntity;

import javax.swing.text.html.parser.Entity;
import java.sql.SQLException;

public interface IUserRepository {
    public void createUser(UserEntity user) throws SQLException, Exception;
    public String findUserByUsernamePassword(UserEntity user, String IPaddr)throws SQLException, Exception;
    public void resetPassword(UserEntity user, String password) throws Exception;
    public String takeEmailByUsername(String username) throws Exception;
    public void saveFriendRequest(String user_id, String friend_id) throws SQLException;
    public void acceptFriendRequest(String user_id, String friend_id) throws SQLException;
}
