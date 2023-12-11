package chatapp.repository.impl;


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
    public String getUserData(String fullname, String username, String status) throws Exception {
        String query = "SELECT * FROM users";
        int count = 0;
        if (fullname != null || username != null || status != null) {
            query += " WHERE ";
        }
        if (fullname != null) {
            query += String.format(" fullname='%s' ", fullname);
            count += 1;
        }
        if (username != null) {
            if (count > 0) query += " and ";
            count += 1;
            query += String.format(" username='%s' ", username);
        }
        if (status != null) {
            if (count > 0) query += " and ";
            count += 1;
            query += String.format(" status='%s' ", status);
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
        String query = "UPDATE users SET username=?, fullname=?, address=?, dateofbirth=?, sex=?, email=?," +
                "status=?, isban=? WHERE id=?";
        try (PreparedStatement preparedStatement = conn.prepareStatement(query)) {
            preparedStatement.setString(1, user.getUsername());
            preparedStatement.setString(2, user.getFullname());
            preparedStatement.setString(3, user.getAddress());
            preparedStatement.setString(4, user.getDateOfBirth());
            preparedStatement.setString(5, user.getSex());
            preparedStatement.setString(6, user.getEmail());
            preparedStatement.setString(7, user.getStatus());
            preparedStatement.setBoolean(8, user.isIsban());
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
    public void getLoginHistories(String order) throws SQLException{
        String query = "SELECT * FROM login_histories ORDER BY login_time ?";
        try (PreparedStatement preparedStatement = conn.prepareStatement(query)) {
            preparedStatement.setString(1, order);
            ResultSet res = preparedStatement.executeQuery();
            while (res.next()){

            }
        } catch (Exception e) {
            System.out.println(e);
            throw e;
        }
    }
}
