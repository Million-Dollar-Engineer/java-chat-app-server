package chatapp.repository.impl;

import chatapp.entity.GroupChatEntity;
import chatapp.entity.UserEntity;
import chatapp.internal.database.Postgres;
import chatapp.repository.IGroupChatRepository;

import java.sql.*;

public class GroupChatRepository implements IGroupChatRepository {

    Postgres db;
    Connection conn;

    public GroupChatRepository() {
        db = Postgres.getInstance();
        conn = db.getConnection();
    }

    @Override
    public String getGroupChatListByAdmin(String sortBy, String order , String name) throws SQLException{
        String query = "SELECT * FROM chat_groups ";
        if(name != null){
            query += "WHERE name ILIKE" + "'%" + name + "%' ";
        }
        if(sortBy.equals("time")){
            query += "ORDER BY created_at ";
            if(order != null){
                query += order;
            }
        }
        else if(sortBy.equals("name")){
            query += "ORDER BY name ";
            if(order != null){
                query += order;
            }
        }
        String res = "";
        try {
            Statement statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            res = GroupChatEntity.resultSetToJSON(resultSet);
        } catch (SQLException e) {
            System.out.println(e);
            throw e;
        }

        return res;

    }

    @Override
    public String getGroupChatMember(String group_id, String admin) throws SQLException {
        String query = "SELECT * FROM users u, chat_group_members cgm " +
                "WHERE cgm.group_id = ? AND u.id = cgm.member_id ";

        if(admin != null){
            query += " AND cgm.member_role = 'admin' ";
        }
        String res = "";
        try (PreparedStatement preparedStatement = conn.prepareStatement(query)){
            preparedStatement.setString(1, group_id);
            ResultSet resultSet = preparedStatement.executeQuery();
            res = UserEntity.resultSetToJSON(resultSet);
        } catch (SQLException e) {
            System.out.println(e);
            throw e;
        }

        return res;

    }
}
