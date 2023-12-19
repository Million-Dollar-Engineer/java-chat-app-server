package chatapp.repository.impl;


import chatapp.entity.AdminEntity;
import chatapp.entity.SpamReportEntity;
import chatapp.entity.UserEntity;
import chatapp.internal.database.Postgres;
import chatapp.repository.IAdminRepository;
import org.apache.catalina.User;

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
    public String getUserData(String fullname, String username, String status, String sortBy, String order)
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
            query += " status ILIKE" + "'%" + status + "%' ";
        }
        if(sortBy != null){
            query += " ORDER BY " + sortBy + " " + order + " ";
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
        String query = "UPDATE users SET username=?, full_name=?, addr=?, dob=?, sex=CAST(? AS GENDER), email=?," +
                "role= CAST(? AS USERROLE), status=CAST(? AS USERSTATUS) WHERE id=?";
        try (PreparedStatement preparedStatement = conn.prepareStatement(query)) {
            preparedStatement.setString(1, user.getUsername());
            preparedStatement.setString(2, user.getFullname());
            preparedStatement.setString(3, user.getAddress());

            user.setDateOfBirth(user.getDateOfBirth() + " 00:00:00");
            Timestamp timestamp= Timestamp.valueOf(user.getDateOfBirth());

            preparedStatement.setTimestamp(4, timestamp);
            preparedStatement.setString(5, user.getSex());
            preparedStatement.setString(6, user.getEmail());
            preparedStatement.setString(7, user.getRole());
            preparedStatement.setString(8, user.getStatus());
            preparedStatement.setString(9, user.getId());

            preparedStatement.executeUpdate();
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
    public String getLoginHistories(String order) throws SQLException{
        String res = "";
        System.out.println(order);
        String query = "SELECT username, full_name, ip_addr, login_time" +
                " FROM login_histories lg, users u" +
                " WHERE lg.user_id = u.id" +
                " ORDER BY login_time " + order;
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
    public String getSpamReportList(String sortBy, String startTime, String endTime, String username)
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
                query += (" ORDER BY " + sortBy);
            }
            else if(sortBy.equals("username")){
                sortBy = "u.username";
                query += (" ORDER BY " + sortBy);
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
        if(name != null){
            query += (" AND u1.username = '" +name + "' ");
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
            System.out.println("BBB");
            res = AdminEntity.FriendAndFOFResultSetToJSON(resultSet, greaterThan, lowerThan, equal);
        } catch (Exception e) {
            System.out.println(e);
            throw e;
        }
        return res;
    }


}
