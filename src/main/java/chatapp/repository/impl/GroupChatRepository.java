package chatapp.repository.impl;

import chatapp.entity.GroupChatEntity;
import chatapp.entity.UserEntity;
import chatapp.internal.database.Postgres;
import chatapp.repository.IGroupChatRepository;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class GroupChatRepository implements IGroupChatRepository {

    Postgres db;
    Connection conn;

    public GroupChatRepository() {
        db = Postgres.getInstance();
        conn = db.getConnection();
    }

    @Override
    public String getGroupChatListByAdmin(String sortBy, String name) throws SQLException{
        String query = "SELECT * FROM chat_groups ";
        if(name != null){
            query += "WHERE name ILIKE" + "'%" + name + "%' ";
        }
        if(sortBy.equals("time")){
            query += "ORDER BY created_at DESC ";
        }
        else if(sortBy.equals("name")){
            query += "ORDER BY name ";
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
}
