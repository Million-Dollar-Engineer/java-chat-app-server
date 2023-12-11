package chatapp.repository;

import chatapp.entity.UserEntity;

import java.sql.SQLException;

public interface IAdminRepository {
    public void setAccountStatus(String id, String status) throws Exception;
    public String getUserData(String fullname, String username, String status) throws Exception;
    public void updateUser(UserEntity user) throws SQLException;
    public void deleteUser(String id) throws SQLException;
    public void getLoginHistories(String order) throws SQLException;
}
