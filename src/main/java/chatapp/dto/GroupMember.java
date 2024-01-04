package chatapp.dto;

import lombok.Getter;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Getter
public class GroupMember {
    private final String name;

    private final String userName;

    private final String role;

    public GroupMember(String name, String userName, String role) {
        this.name = name;
        this.userName = userName;
        this.role = role;
    }

    public static GroupMember mapRowToEntity(ResultSet rs) throws SQLException {
        return new GroupMember(rs.getString("full_name"), rs.getString("username"), rs.getString("member_role"));
    }

    public static List<GroupMember> mapRSToListEntity(ResultSet rs) throws SQLException {
        List<GroupMember> users = new ArrayList<>();
        while (rs.next()) {
            users.add(mapRowToEntity(rs));
        }

        return users;
    }

}