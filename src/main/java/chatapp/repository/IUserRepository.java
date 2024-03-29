package chatapp.repository;


import chatapp.dto.GroupMember;
import chatapp.dto.User;
import chatapp.entity.GroupChatEntity;
import chatapp.entity.UserEntity;

import java.sql.SQLException;
import java.util.List;

public interface IUserRepository {
    public void createUser(UserEntity user) throws SQLException, Exception;

    public String findUserByUsernamePassword(UserEntity user, String IPaddr) throws SQLException, Exception;

    public void resetPassword(UserEntity user, String password) throws Exception;

    public boolean checkExistFriendRequest(String user_id, String friend_id) throws SQLException;

    public String takeEmailByUsername(String username) throws Exception;

    public void saveFriendRequest(String user_id, String friend_id) throws SQLException;

    public void acceptFriendRequest(String user_id, String friend_id) throws SQLException;

    public void deleteFriend(String user_id, String friend_id) throws SQLException;

    public List<User> getFriendList(String user_id) throws SQLException;

    public List<User> getFriendRequestList(String user_id) throws SQLException;

    public User getUserByUsername(String username) throws SQLException;

    public String getOnlineFriend(String user_id) throws Exception;

    public boolean isUserInGroup(String user_id, String group_id) throws SQLException;

    public String getUserIdByUsername(String username) throws SQLException;

    public String getUsernameByUserId(String username) throws SQLException;

    public void blockUser(String user_id, String block_id) throws SQLException;

    Boolean isBlocked(String userId, String userIdByUsername);

    void reportSpam(String userId, String userIdByUsername, String reason);

    String createGroup(String userId, String groupName);

    void addUserToGroup(String groupId, String userIdByUsername);

    List<GroupChatEntity> listMyGroup(String userId);

    List<GroupMember> listGroupMember(String groupId);

    void removeUserFromGroup(String groupId, String userIdByUsername);

    void renameGroup(String groupId, String groupName);

    void giveAdminRole(String groupId, String userIdByUsername);
}
