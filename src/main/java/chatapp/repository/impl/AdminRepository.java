package chatapp.repository.impl;


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
}
