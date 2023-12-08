package chatapp.repository.impl;


import chatapp.entity.UserEntity;
import chatapp.internal.database.Postgres;
import chatapp.repository.IUserRepository;

import java.sql.*;

public class UserRepository implements IUserRepository {

    Postgres db;
    Connection conn;

    public UserRepository() {
        db = Postgres.getInstance();
        conn = db.getConnection();
    }

    @Override
    public void createUser(UserEntity user) throws SQLException {
        // Implement the logic to create a new user
        // Write sql
        String query = "INSERT INTO users (username, password, fullname, address, dateofbirth, sex, email," +
                " creationtime, status, lastest_access, isban)" +
                " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";

        String query1 = "INSERT INTO login_history (username)" +
                " VALUES (?);";

        try (PreparedStatement preparedStatement = conn.prepareStatement(query)) {
            conn.setAutoCommit(false);
            preparedStatement.setString(1, user.getUsername());
            preparedStatement.setString(2, user.getPassword());
            preparedStatement.setString(3, user.getFullname());
            preparedStatement.setString(4, user.getAddress());
            preparedStatement.setString(5, user.getDateOfBirth());
            preparedStatement.setString(6, user.getSex());
            preparedStatement.setString(7, user.getEmail());

            Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());
            preparedStatement.setTimestamp(8, currentTimestamp);

            preparedStatement.setString(9, "active");
            preparedStatement.setTimestamp(10, currentTimestamp);
            preparedStatement.setBoolean(11, false);

            preparedStatement.executeUpdate();



            conn.commit();
            System.out.println("Inserted a row");
        } catch (SQLException e) {
            System.out.println(e);
            conn.rollback();
            throw e;
        }
        conn.setAutoCommit(true);
    }

    @Override
    public int findUserByUsernamePassword(UserEntity user) throws Exception {
        String query = "SELECT * FROM users where username=? and password=?";
        try (PreparedStatement preparedStatement = conn.prepareStatement(query)) {
            preparedStatement.setString(1, user.getUsername());
            preparedStatement.setString(2, user.getPassword());

            ResultSet res = preparedStatement.executeQuery();
            while (res.next()) {
                System.out.println(res.getBoolean("isban"));
                if (res.getBoolean("isban")) {
                    return -2;
                }
                int id = res.getInt("id");
                String updateQuery = "UPDATE users SET lastest_access = ? WHERE id = ?";
                try (PreparedStatement statement = conn.prepareStatement(updateQuery)) {
                    Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());
                    statement.setTimestamp(1, currentTimestamp);
                    statement.setInt(2, id);
                    statement.executeUpdate();
                } catch (Exception e) {
                    throw e;
                }

                String updateQuery1 = "INSERT INTO login_history (id, access_time) VALUES(?, ?)";
                try(PreparedStatement statement = conn.prepareStatement(updateQuery1)){
                    Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());
                    statement.setInt(1, id);
                    statement.setTimestamp(2, currentTimestamp);
                    statement.executeUpdate();
                }
                catch (Exception e){
                    throw e;
                }

                return id;
            }
        } catch (SQLException e) {
            System.out.println(e);
            throw e;
        }
        return -1;
    }

    public String takeEmailByUsername(String username) throws Exception {
        String query = "SELECT * FROM users WHERE username=?";
        try (PreparedStatement preparedStatement = conn.prepareStatement(query)) {
            preparedStatement.setString(1, username);

            ResultSet res = preparedStatement.executeQuery();
            while (res.next()) {
                return res.getString("email");
            }
        } catch (SQLException e) {
            System.out.println(e);
            throw e;
        }
        return "";
    }

    public void resetPassword(UserEntity user, String password) throws Exception {
        String query = "UPDATE users SET password=? WHERE username=?";
        try (PreparedStatement preparedStatement = conn.prepareStatement(query)) {
            preparedStatement.setString(1, password);
            preparedStatement.setString(2, user.getUsername());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e);
            throw e;
        }
    }
}
