package chatapp.entity;

import java.sql.ResultSet;
import java.sql.SQLException;

public class GroupChatEntity {
    private String id;
    private String name;
    private String created_at;
    private String updated_at;
    private boolean deleted;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public GroupChatEntity(String id, String name, String created_at, String updated_at, boolean deleted) {
        this.id = id;
        this.name = name;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.deleted = deleted;
    }

    @Override
    public String toString() {
        return "{" +
                "\"id\": \" " + id + '\"' +
                ", \"username\": \" " + name + '\"' +
                ", \"created_at\": \" " + created_at + '\"' +
                ", \"updated_at\": \" " + updated_at + '\"' +
                ", \"deleted\": \" " + deleted + '\"' +
                '}';
    }

    public static String resultSetToJSON(ResultSet resultSet) throws SQLException {
        String res = "[ ";
        int count = 0;
        try{
            while (resultSet.next()) {
                if(count >= 1) res += ",";
                GroupChatEntity group = new GroupChatEntity(
                        resultSet.getString("id"),
                        resultSet.getString("name"),
                        resultSet.getString("created_at"),
                        resultSet.getString("updated_at"),
                        resultSet.getBoolean("deleted")
                );
                res += group.toString();
                count++;
            }
        }
        catch (Exception e){
            System.out.println(e);
            throw e;
        }
        res += " ]";
        return res;
    }
}
