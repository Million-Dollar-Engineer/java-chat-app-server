package chatapp.repository;

import chatapp.entity.UserEntity;

import java.sql.SQLException;

public interface IAdminRepository {
    public void setAccountStatus(String id, String status) throws Exception;
    public String getUserData(String fullname, String username, String status, String sortBy, String order)
            throws SQLException;
    public void updateUser(UserEntity user) throws SQLException;
    public void deleteUser(String id) throws SQLException;
    public String getLoginHistories(String order) throws SQLException;
    public String getSpamReportList(String sortBy, String startTime, String endTime, String username)
            throws SQLException;
    public String getNumberOfUserEachMonth(int year)
            throws SQLException;
    public String getFriendAndFriendOfFriends(String sortBy, String order, String name, String greaterThan,
                                              String lowerThan, String equal) throws SQLException;

    public String getNumberOfUserActiveEachMonth(int year)
            throws SQLException;
    public String getActiveUsersAndRelevantInfo(String sortBy, String order, String startTime,
                                                String endTime, String username, String equal, String greaterThan, String lowerThan
    ) throws SQLException;
}
