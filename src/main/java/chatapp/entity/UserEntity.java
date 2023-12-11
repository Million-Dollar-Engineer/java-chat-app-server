package chatapp.entity;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserEntity {
    private String id;
    private String username;
    private String password;
    private String fullname;
    private String address;
    private String dateOfBirth;
    private String sex;
    private String email;
    private String role;
    private String status;
    private String last_active;
    private String deleted;
    private String created_at;
    private String updated_at;


    public UserEntity(String id, String username, String password, String fullname, String address,
                      String dateOfBirth, String sex, String email, String role, String status, String last_active,
                      String deleted, String created_at, String updated_at) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.fullname = fullname;
        this.address = address;
        this.dateOfBirth = dateOfBirth;
        this.sex = sex;
        this.email = email;
        this.role = role;
        this.status = status;
        this.last_active = last_active;
        this.deleted = deleted;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    static public String resultSetToJSON(ResultSet resultSet) throws SQLException {
        String res = "[ ";
        try{
            while (resultSet.next()) {
                UserEntity user = new UserEntity(
                        resultSet.getString("id"),
                        resultSet.getString("username"),
                        resultSet.getString("password"),
                        resultSet.getString("full_name"),
                        resultSet.getString("addr"),
                        resultSet.getString("dob"),
                        resultSet.getString("sex"),
                        resultSet.getString("email"),
                        resultSet.getString("role"),
                        resultSet.getString("status"),
                        resultSet.getString("last_active"),
                        resultSet.getString("deleted"),
                        resultSet.getString("created_at"),
                        resultSet.getString("updated_at")
                );
                res += user.toString();
            }
        }
        catch (Exception e){
            System.out.println(e);
            throw e;
        }
        res += " ]";
        return res;
    }

    @Override
    public String toString() {
        return "UserEntity{" +
                "id:'" + id + '\'' +
                ", username:'" + username + '\'' +
                ", password:'" + password + '\'' +
                ", fullname:'" + fullname + '\'' +
                ", address:'" + address + '\'' +
                ", dateOfBirth:'" + dateOfBirth + '\'' +
                ", sex:'" + sex + '\'' +
                ", email:'" + email + '\'' +
                ", role:'" + role + '\'' +
                ", status:'" + status + '\'' +
                ", last_active:'" + last_active + '\'' +
                ", deleted:'" + deleted + '\'' +
                ", created_at:'" + created_at + '\'' +
                ", updated_at:'" + updated_at + '\'' +
                '}';
    }
}
