package chatapp.repository;

import chatapp.entity.UserEntity;

import java.sql.SQLException;

public interface IAdminRepository {
    public void setAccountStatus(String id, String status) throws Exception;
    public void setAccountActive(String id) throws Exception;
    public String getUserData(String fullname, String username, String status) throws Exception;
    public void createUser(UserEntity user) throws SQLException;
    public void updateUser(UserEntity user) throws SQLException;
    public void deleteUser(String id) throws SQLException;
}
