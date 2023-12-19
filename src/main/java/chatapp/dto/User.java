package chatapp.dto;

import lombok.Getter;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Getter
public class User {
    private final String name;

    private final String userName;

    public User(String name, String userName) {
        this.name = name;
        this.userName = userName;
    }


    public static User mapRowToEntity(ResultSet rs) throws SQLException {
        return new User(rs.getString("full_name"), rs.getString("username"));
    }

    public static List<User> mapRSToListEntity(ResultSet rs) throws SQLException {
        List<User> users = new ArrayList<>();
        while (rs.next()) {
            System.out.println(rs.getString("full_name") + " " + rs.getString("username"));
            users.add(mapRowToEntity(rs));
        }

        return users;
    }
}
