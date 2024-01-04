package chatapp.entity;

import java.sql.ResultSet;
import java.sql.SQLException;

public class AdminEntity {
    private String username;
    private String password;

    public AdminEntity(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    static public String numberOfUserEachMonthResultSetToJSON(ResultSet resultSet) throws SQLException {
        String res = "[ ";
        int count = 0;
        try{
            while (resultSet.next()) {
                if(count >= 1) res += ",";
                res += "{" +
                        "\"month\": \"" + resultSet.getInt("month") + '\"' +
                        ", \"number_of_user\": \"" + resultSet.getInt("number_of_user") + '\"' +
                        '}';
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
    static public String FriendAndFOFResultSetToJSON(ResultSet resultSet, String greaterThan, String lowerThan,
                                                     String equal
    ) throws SQLException {
        String res = "[ ";
        int count = 0;
        try{
            while (resultSet.next()) {
                int f = 0;
                int fof = 0;
                f = resultSet.getInt("friend");
                fof = resultSet.getInt("fof");

                if(greaterThan != null){
                    if(Integer.parseInt(greaterThan) != 0){
                        if(f <= Integer.parseInt(greaterThan)) continue;
                    }
                }
                if(lowerThan != null){
                    if(Integer.parseInt(lowerThan) != 0){
                        if(f >= Integer.parseInt(lowerThan)) continue;
                    }
                }
                if(equal != null){
                    if(Integer.parseInt(equal) != 0){
                        if(f != Integer.parseInt(equal)) continue;
                    }
                }

                if(count >= 1) res += ",";


                res += "{" +
                        "\"id\": \"" + resultSet.getString("u_id") + '\"' +
                        ", \"username\": \"" + resultSet.getString("username") + '\"' +
                        ", \"created_at\": \"" + resultSet.getString("created_at") + '\"' +
                        ", \"friend\": \"" + f + '\"' +
                        ", \"friendOfFriend\": \"" + fof + '\"' +

                        '}';
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

    static public String ActiveUsersAndRelevantInfoResultSetToJSON(ResultSet resultSet, String greaterThan,
                                                                   String lowerThan, String equal
    ) throws SQLException {
        String res = "[ ";
        int count = 0;
        try{
            while (resultSet.next()) {
                int access_times = 0;
                int personal_mes_num = 0;
                int group_mes_num = 0;
                access_times = resultSet.getInt("access_times");
                personal_mes_num = resultSet.getInt("personal_num");
                group_mes_num = resultSet.getInt("group_num");

                System.out.println( "AA" +greaterThan);

                if(greaterThan != null){
                    if(Integer.parseInt(greaterThan) != 0){
                        if(access_times <= Integer.parseInt(greaterThan)) continue;
                    }
                }
                if(lowerThan != null){
                    if(Integer.parseInt(lowerThan) != 0){
                        if(access_times >= Integer.parseInt(lowerThan)) continue;
                    }
                }
                if(equal != null){
                    if(Integer.parseInt(equal) != 0){
                        if(access_times != Integer.parseInt(equal)) continue;
                    }
                }

                if(count >= 1) res += ",";


                res += "{" +
                        "\"id\": \"" + resultSet.getString("id") + '\"' +
                        ", \"username\": \"" + resultSet.getString("username") + '\"' +
                        ", \"created_at\": \"" + resultSet.getString("created_at") + '\"' +
                        ", \"access_times\": \"" + access_times + '\"' +
                        ", \"chatted_people\": \"" + personal_mes_num + '\"' +
                        ", \"chatted_group\": \"" + group_mes_num + '\"' +

                        '}';
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
