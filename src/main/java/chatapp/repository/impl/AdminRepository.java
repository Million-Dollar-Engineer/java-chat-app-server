package chatapp.repository.impl;


import chatapp.entity.AdminEntity;
import chatapp.entity.SpamReportEntity;
import chatapp.entity.UserEntity;
import chatapp.internal.database.Postgres;
import chatapp.repository.IAdminRepository;
import chatapp.utils.Utils;
import org.apache.catalina.User;
import org.springframework.web.bind.annotation.RequestParam;

import java.sql.*;

public class AdminRepository implements IAdminRepository {
    Postgres db;
    Connection conn;

    public AdminRepository() {
        db = Postgres.getInstance();
        conn = db.getConnection();
    }

    @Override
    public void setAccountStatus(String id, String status) throws Exception {
        String query = "UPDATE users SET status=CAST(? AS USERSTATUS) WHERE id=?";
        try (PreparedStatement preparedStatement = conn.prepareStatement(query)) {
            preparedStatement.setString(1, status);
            preparedStatement.setString(2, id);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e);
            throw e;
        }
    }


    @Override
    public String getUserData(String fullname, String username, String status, String sortBy, String order,
                              String startTime, String endTime)
            throws SQLException {
        String query = "SELECT * FROM users";
        int count = 0;
        if (fullname != null || username != null || status != null || sortBy != null || order != null) {
            query += " WHERE ";
        }
        if (fullname != null) {
            query += " full_name ILIKE " + "'%" + fullname + "%' ";
            count += 1;
        }
        if (username != null) {
            if (count > 0) query += " and ";
            count += 1;
            query += " username ILIKE " + "'%" + username + "%'";
        }
        if (status != null) {
            if (count > 0) query += " and ";
            count += 1;
            query += " status = " + "'" + status + "' ";
        }
        if (startTime != null) {
            if (count > 0) query += " and ";
            count += 1;
            query += " created_at >= '" +  startTime  + "' ";
        }
        if (endTime != null) {
            if (count > 0) query += " and ";
            count += 1;
            query += " created_at <= '" +  endTime  + "' ";
        }
        if(sortBy != null){
            if(order != null){
                query += " ORDER BY " + sortBy + " " + order + " ";

            }
        }

        System.out.println(query);
        String res = "";
        try {
            Statement statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            res = UserEntity.resultSetToJSON(resultSet);
        } catch (SQLException e) {
            System.out.println(e);
            throw e;
        }

        return res;
    }


    @Override
    public void updateUser(UserEntity user) throws SQLException {
        String query = "UPDATE users ";

        int count = 0;
        if(user.getFullname() != null || user.getAddress() != null || user.getDateOfBirth() != null ||
                user.getSex() != null || user.getEmail() != null || user.getRole() != null
                || user.getId() != null
        ){
            query += " SET ";
            if(user.getFullname() != null ){
                query += " full_name= '" +  user.getFullname() + "' ";
                count++;
            }
            if(user.getAddress() != null ){
                if(count >= 1) query += ",";
                query += " addr= '" +  user.getAddress() + "' ";
                count++;
            }
            if(user.getDateOfBirth() != null ){
                if(count >= 1) query += ",";
                query += " dob= '" +  user.getDateOfBirth() + "' ";
                count++;
            }
            if(user.getSex() != null ){
                if(count >= 1) query += ",";
                query += " sex= '" +  user.getSex() + "' ";
                count++;
            }
            if(user.getEmail() != null ){
                if(count >= 1) query += ",";
                query += " email= '" +  user.getEmail() + "' ";
                count++;
            }
            if(user.getRole() != null ){
                if(count >= 1) query += ",";
                query += " role= '" +  user.getRole() + "' ";
                count++;
            }
            if(user.getStatus() != null ){
                if(count >= 1) query += ",";
                query += " status= '" +  user.getStatus() + "' ";
                count++;
            }
            if(user.getPassword() != null && user.getPassword().length() >= 6 ){
                if(count >= 1) query += ",";
                query += " password= '" + Utils.hashString(user.getPassword()) + "' ";
                count++;
            }
            query +=" WHERE id= '" + user.getId() + "' ";
        }

        try {
            Statement statement = conn.createStatement();
            statement.executeUpdate(query);
            System.out.println("Update a row");
        } catch (Exception e) {
            System.out.println(e);
            throw e;
        }
    }

    @Override
    public void deleteUser(String id) throws SQLException {
        String query = "DELETE FROM users WHERE id=?";
        try (PreparedStatement preparedStatement = conn.prepareStatement(query)) {
            preparedStatement.setString(1, id);
            preparedStatement.executeUpdate();
            System.out.println("Delete a row");
        } catch (Exception e) {
            System.out.println(e);
            throw e;
        }
    }

    @Override
    public String getLoginHistories(String order, String username) throws SQLException{
        String res = "";
        System.out.println(order);
        String query = "SELECT username, full_name, ip_addr, login_time" +
                " FROM login_histories lg, users u" +
                " WHERE lg.user_id = u.id ";
        if(username != null) query += " and u.username ='" + username + "' ";
        query += " ORDER BY login_time " + order;
        try (PreparedStatement preparedStatement = conn.prepareStatement(query)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            res = UserEntity.loginHistoriesResultSetToJSON(resultSet);
        } catch (Exception e) {
            System.out.println(e);
            throw e;
        }
        return res;
    }

    @Override
    public String getSpamReportList(String sortBy, String order, String startTime, String endTime, String username)
            throws SQLException{
        String res = "";

        String query = "SELECT s.* FROM spam_reports s, users u WHERE s.accused_id=u.id  ";
        if(username != null){
            query += (" AND u.username ILIKE '%" + username + "%' ");
        }
        if(startTime != null && endTime != null){
            query += String.format(" AND s.created_at >= '%s' AND s.created_at <= '%s' ", startTime, endTime);
        }
        if(sortBy != null){

            if(sortBy.equals("time")){
                sortBy = "s.created_at";
                query += (" ORDER BY " + sortBy + " ");
                if(order != null){
                    query += order;
                }
            }
            else if(sortBy.equals("username")){
                sortBy = "u.username";
                query += (" ORDER BY " + sortBy + " ");
                if(order != null){
                    query += order;
                }
            }

        }
        try (PreparedStatement preparedStatement = conn.prepareStatement(query)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            res = SpamReportEntity.resultSetToJSON(resultSet);
        } catch (Exception e) {
            System.out.println(e);
            throw e;
        }
        return res;
    }

    @Override
    public String getNumberOfUserEachMonth(int year)
            throws SQLException{
        String res = "";

        String query = "SELECT EXTRACT (MONTH FROM u.created_at) as month, COUNT(*) as number_of_user FROM users u " +
                " WHERE EXTRACT(YEAR FROM u.created_at) = ? " +
                " GROUP BY EXTRACT (MONTH FROM u.created_at) ";

        try (PreparedStatement preparedStatement = conn.prepareStatement(query)) {
            preparedStatement.setInt(1, year);
            ResultSet resultSet = preparedStatement.executeQuery();
            res = AdminEntity.numberOfUserEachMonthResultSetToJSON(resultSet);
        } catch (Exception e) {
            System.out.println(e);
            throw e;
        }
        return res;
    }

    public String getFriendAndFriendOfFriends(String sortBy, String order, String name, String greaterThan,
                                              String lowerThan, String equal) throws SQLException{
        String res = "";

        String query = "SELECT u1.id u_id, u1.username,(SELECT COUNT(*) \n" +
                "\t\t\tFROM user_friends uf JOIN users u ON ( uf.is_accepted= true AND (uf.user_id = u.id OR uf.friend_id = u.id))\n" +
                "\t\t\tWHERE u.id = u1.id\n" +
                "\t\t\tGROUP BY u.id) friend, (SELECT COUNT(*) \n" +
                "\t\t\t\t\t\t\tFROM (SELECT u2.id, u3.id friend_id\n" +
                "\t\t\t\t\t\t\t\t\tFROM users u3, user_friends uf JOIN users u2 ON ( uf.is_accepted= true AND (uf.user_id = u2.id OR uf.friend_id = u2.id))\n" +
                "\t\t\t\t\t\t\t\t\tWHERE (u3.id = uf.friend_id OR u3.id = uf.user_id) AND u3.id != u2.id) TMP, user_friends uf3\n" +
                "\t\t\t\t\t\t\tWHERE (TMP.friend_id = uf3.user_id OR TMP.friend_id = uf3.friend_id) AND uf3.is_accepted = true AND TMP.id = u1.id\n" +
                "\t\t\t\t\t\t\tGROUP BY TMP.id) fof \n" +
                "FROM users u1 ";

        query += " WHERE true ";
        if(name != null && !name.isEmpty()){
            query += (" AND u1.full_name ILIKE '%" +name + "%' ");
        }

        if(sortBy != null){
            query += (" ORDER BY u1." + sortBy + " ");
            if(order != null){
                query += (" " + order + " ");
            }
        }

        try {
            Statement statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            res = AdminEntity.FriendAndFOFResultSetToJSON(resultSet, greaterThan, lowerThan, equal);
        } catch (Exception e) {
            System.out.println(e);
            throw e;
        }
        return res;
    }

    public String getNumberOfUserActiveEachMonth(int year)
            throws SQLException{
        String res = "";

        String query = "SELECT EXTRACT (MONTH FROM u.last_active) as month, COUNT(*) as number_of_user FROM users u " +
                " WHERE EXTRACT(YEAR FROM u.last_active) = ? " +
                " GROUP BY EXTRACT (MONTH FROM u.last_active) ";

        try (PreparedStatement preparedStatement = conn.prepareStatement(query)) {
            preparedStatement.setInt(1, year);
            ResultSet resultSet = preparedStatement.executeQuery();
            res = AdminEntity.numberOfUserEachMonthResultSetToJSON(resultSet);
        } catch (Exception e) {
            System.out.println(e);
            throw e;
        }
        return res;
    }

    public String getActiveUsersAndRelevantInfo(String sortBy, String order, String startTime,
         String endTime, String username, String equal, String greaterThan, String lowerThan
    ) throws SQLException {
        String res = "";
        String query = "SELECT u.id, u.username ,(SELECT COUNT(*) FROM access_histories ah\n" +
                "\t\t\tWHERE ah.access_at >= ? AND ah.access_at <= ? AND ah.user_id = u.id\n" +
                "\t\t\tGROUP BY ah.user_id) access_times, (SELECT COUNT(*) FROM personal_messages pm\n" +
                "\t\t\t\t\t\t\t\t\t\t\t   WHERE pm.sender_id = u.id \n" +
                "\t\t\t\t\t\t\t\t\t\t\t   AND pm.created_at >= ? AND pm.created_at <= ?) personal_num,\n" +
                "\t\t\t\t\t\t\t\t\t\t\t   \t\t(SELECT COUNT(*) FROM group_messages gm\n" +
                "\t\t\t\t\t\t\t\t\t\t\t\t\t   WHERE gm.sender_id = u.id \n" +
                "\t\t\t\t\t\t\t\t\t\t\t\t\t   AND gm.created_at >= ? AND gm.created_at <= ?) group_num\n" +
                "FROM users u\n" +
                "WHERE true";
        if(username != null){
            query += (" AND u.username ILIKE '%" + username + "%' ");
        }
        if(startTime != null && endTime != null){
            query += String.format(" AND u.created_at >= '%s' AND u.created_at <= '%s' ", startTime, endTime);
        }
        if(sortBy != null && !sortBy.isEmpty()){
            query += (" ORDER BY u." + sortBy);
            if(order != null) {
                query += (" " + order + " ");
            }
        }
        try (PreparedStatement preparedStatement = conn.prepareStatement(query)){
            startTime += " 00:00:00";
            Timestamp timestampStart= Timestamp.valueOf(startTime);
            endTime += " 00:00:00";
            Timestamp timestampEnd= Timestamp.valueOf(endTime);
            preparedStatement.setTimestamp(1, timestampStart);
            preparedStatement.setTimestamp(2, timestampEnd);
            preparedStatement.setTimestamp(3, timestampStart);
            preparedStatement.setTimestamp(4, timestampEnd);
            preparedStatement.setTimestamp(5, timestampStart);
            preparedStatement.setTimestamp(6, timestampEnd);
            ResultSet resultSet = preparedStatement.executeQuery();
            res = AdminEntity.ActiveUsersAndRelevantInfoResultSetToJSON(resultSet, greaterThan, lowerThan, equal);
        } catch (Exception e) {
            System.out.println(e);
            throw e;
        }

        return res;
    }

}
