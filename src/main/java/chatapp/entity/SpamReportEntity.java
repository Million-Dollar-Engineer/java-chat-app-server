package chatapp.entity;

import java.sql.ResultSet;
import java.sql.SQLException;

public class SpamReportEntity {
    private String id;
    private String reporter_id;
    private String accused_id;
    private String reason;
    private String created_at;

    public SpamReportEntity(String id, String reporter_id, String accused_id, String reason, String created_at) {
        this.id = id;
        this.reporter_id = reporter_id;
        this.accused_id = accused_id;
        this.reason = reason;
        this.created_at = created_at;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getReporter_id() {
        return reporter_id;
    }

    public void setReporter_id(String reporter_id) {
        this.reporter_id = reporter_id;
    }

    public String getAccused_id() {
        return accused_id;
    }

    public void setAccused_id(String accused_id) {
        this.accused_id = accused_id;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    @Override
    public String toString() {
        return "{" +
                "\"id\": \" " + id + '\"' +
                ", \"reporter_id\": \" " + reporter_id + '\"' +
                ", \"accused_id\": \" " + accused_id + '\"' +
                ", \"reason\": \" " + reason + '\"' +
                ", \"created_at\": \" " + created_at + '\"' +
                '}';
    }

    static public String resultSetToJSON(ResultSet resultSet) throws SQLException {
        String res = "[ ";
        int count = 0;
        try{
            while (resultSet.next()) {
                if(count >= 1) res += ",";
                SpamReportEntity spam = new SpamReportEntity(
                        resultSet.getString("id"),
                        resultSet.getString("reporter_id"),
                        resultSet.getString("accused_id"),
                        resultSet.getString("reason"),
                        resultSet.getString("created_at")
                );
                res += spam.toString();
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
