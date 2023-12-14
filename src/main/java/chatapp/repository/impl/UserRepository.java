package chatapp.repository.impl;


import chatapp.entity.UserEntity;
import chatapp.internal.database.Postgres;
import chatapp.repository.IUserRepository;
import org.springframework.core.annotation.Order;

import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class UserRepository implements IUserRepository {

    Postgres db;
    Connection conn;

    public UserRepository() {
        db = Postgres.getInstance();
        conn = db.getConnection();
    }

    @Override
    public void createUser(UserEntity user) throws SQLException, ParseException {
        // Implement the logic to create a new user
        // Write sql
        String query = "INSERT INTO users (username, password, full_name, addr, dob, sex, email," +
                "role, status, last_active, deleted, created_at, updated_at, id)" +
                " VALUES (?, ?, ?, ?, ?, CAST(? AS GENDER), ?, CAST(? AS USERROLE), CAST(? AS USERSTATUS)," +
                " ?, ?, ?, ?, ?);";

        try (PreparedStatement preparedStatement = conn.prepareStatement(query)) {
            conn.setAutoCommit(false);
            preparedStatement.setString(1, user.getUsername());
            preparedStatement.setString(2, user.getPassword());
            preparedStatement.setString(3, user.getFullname());
            preparedStatement.setString(4, user.getAddress());

            user.setDateOfBirth(user.getDateOfBirth() + " 00:00:00");
            Timestamp timestamp= Timestamp.valueOf(user.getDateOfBirth());

            preparedStatement.setTimestamp(5, timestamp);
            preparedStatement.setString(6, user.getSex());
            preparedStatement.setString(7, user.getEmail());
            preparedStatement.setString(8, "user");
            preparedStatement.setString(9, "active");

            Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());
            preparedStatement.setTimestamp(10, currentTimestamp);

            preparedStatement.setBoolean(11, false);
            preparedStatement.setTimestamp(12, currentTimestamp);
            preparedStatement.setTimestamp(13, currentTimestamp);
            preparedStatement.setString(14, user.getUsername() +
                    user.getFullname().replace(" ", ""));

            preparedStatement.executeUpdate();

            conn.commit();
            System.out.println("Inserted a row");
        } catch (Exception e) {
            System.out.println(e);
            conn.rollback();
            throw e;
        }
        conn.setAutoCommit(true);
    }

    @Override
    public String findUserByUsernamePassword(UserEntity user, String IPaddr) throws Exception {
        String id = "";
        String query = "SELECT * FROM users where username=? and password=?";
        try (PreparedStatement preparedStatement = conn.prepareStatement(query)) {
            preparedStatement.setString(1, user.getUsername());
            preparedStatement.setString(2, user.getPassword());
            ResultSet res = preparedStatement.executeQuery();
            if(!res.next()){
                throw new Exception("Your username or password is incorrect");
            }
            do {
                if (res.getString("status").equals("banned")) {
                    throw new Exception("Your account is banned");
                }
                id = res.getString("id");
                String updateQuery = "UPDATE users SET last_active = ? WHERE id = ?";
                try (PreparedStatement statement = conn.prepareStatement(updateQuery)) {
                    Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());
                    statement.setTimestamp(1, currentTimestamp);
                    statement.setString(2, id);
                    statement.executeUpdate();
                } catch (Exception e) {
                    throw e;
                }

                String updateQuery1 = "INSERT INTO login_histories (user_id, ip_addr, login_time) VALUES(?, ?, ?)";
                try(PreparedStatement statement = conn.prepareStatement(updateQuery1)){
                    Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());
                    statement.setString(1, id);
                    statement.setString(2, IPaddr);
                    statement.setTimestamp(3, currentTimestamp);
                    statement.executeUpdate();
                }
                catch (Exception e){
                    throw e;
                }
            } while (res.next());
        } catch (SQLException e) {
            System.out.println(e);
            throw e;
        }
        return id;
    }

    @Override
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

    @Override
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

    public boolean checkExistFriendRequest(String user_id, String friend_id) throws SQLException{
        String query = "SELECT * FROM user_friends WHERE (user_id = ? AND friend_id = ?) "
                + " OR (user_id = ? AND friend_id = ?)";
        try (PreparedStatement preparedStatement = conn.prepareStatement(query)) {
            preparedStatement.setString(1, user_id);
            preparedStatement.setString(2, friend_id);
            preparedStatement.setString(3, friend_id);
            preparedStatement.setString(4, user_id);

            ResultSet resultSet = preparedStatement.executeQuery();
            if(!resultSet.next()){
                return false;
            }

            return true;
        } catch (SQLException e) {
            System.out.println(e);
            throw e;
        }
    }

    @Override
    public void saveFriendRequest(String user_id, String friend_id) throws SQLException{
        String query = "INSERT INTO user_friends (user_id, friend_id, created_at, is_accepted)" +
                " VALUES(?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = conn.prepareStatement(query)) {
            preparedStatement.setString(1, user_id);
            preparedStatement.setString(2, friend_id);
            Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());
            preparedStatement.setTimestamp(3, currentTimestamp);

            preparedStatement.setBoolean(4, false);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e);
            throw e;
        }
    }

    @Override
    public void acceptFriendRequest(String user_id, String friend_id) throws SQLException{
        String query = "UPDATE user_friends SET is_accepted = true WHERE user_id = ? and friend_id = ?";
        try (PreparedStatement preparedStatement = conn.prepareStatement(query)) {
            preparedStatement.setString(1, friend_id);
            preparedStatement.setString(2, user_id);

            preparedStatement.executeUpdate();
        }
        catch (SQLException e){
            System.out.println(e);
            throw e;
        }
    }

    @Override
    public void deleteFriend(String user_id, String friend_id) throws SQLException{
        String query = "DELETE FROM user_friends WHERE (user_id = ? and friend_id = ?) " +
                " OR (user_id = ? and friend_id = ?) ";
        try (PreparedStatement preparedStatement = conn.prepareStatement(query)) {
            preparedStatement.setString(1, user_id);
            preparedStatement.setString(2, friend_id);
            preparedStatement.setString(3, friend_id);
            preparedStatement.setString(4, user_id);

            preparedStatement.executeUpdate();
        }
        catch (SQLException e){
            System.out.println(e);
            throw e;
        }
    }

    @Override
    public String getFriendList(String user_id) throws SQLException{
        String res = "";
        String query = "SELECT u.username, u.full_name FROM user_friends uf, users u " +
                "WHERE u.id != ? AND uf.is_accepted= true AND (uf.user_id = ? OR uf.friend_id = ?) " +
                "AND (uf.user_id = u.id OR uf.friend_id = u.id)";
        try (PreparedStatement preparedStatement = conn.prepareStatement(query)) {
            preparedStatement.setString(1, user_id);
            preparedStatement.setString(2, user_id);
            preparedStatement.setString(3, user_id);

            res += "[";
            ResultSet resultSet = preparedStatement.executeQuery();
            int count = 0;
            while (resultSet.next()){
                if(count > 0) res+= ",";
                res+= "{";
                res+= ("\"username\": \"" + resultSet.getString("username") + "\" ,");
                res+= ("\"fullname\": \"" + resultSet.getString("full_name") + "\"");
                res+= "}";
                count++;
            }

            res += "]";
            return res;
        } catch (SQLException e) {
            System.out.println(e);
            throw e;
        }
    }



}
