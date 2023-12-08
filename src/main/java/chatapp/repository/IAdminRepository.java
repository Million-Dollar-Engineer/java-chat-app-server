package chatapp.repository;

import chatapp.entity.UserEntity;

import java.sql.SQLException;

public interface IAdminRepository {
    public void setAccountInActive(int id) throws Exception;
    public void setAccountActive(int id) throws Exception;
    public String getUserData(String fullname, String username, String status) throws Exception;
    public void createUser(UserEntity user) throws SQLException;
    public void updateUser(UserEntity user) throws SQLException;
    public void deleteUser(int id) throws SQLException;
}
