package chatapp.entity;

public class UserEntity {
    private String id;
    private String username;
    private String password;



    private String fullname;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    private String address;

    private String dateOfBirth;
    private String sex;
    private String email;
    private String creationTime;
    private String status;
    private String lastest_access;
    private boolean isban;

//    public UserEntity(String username, String password, String fullname, String address, String dateOfBirth,
//                      String sex, String email) {
//        this.username = username;
//        this.password = password;
//        this.fullname = fullname;
//        this.address = address;
//        this.dateOfBirth = dateOfBirth;
//        this.sex = sex;
//        this.email = email;
//    }


    public UserEntity(String id, String username, String password, String fullname, String address, String dateOfBirth,
                      String sex, String email, String creationTime,
                      String status, String lastest_access, boolean isban) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.fullname = fullname;
        this.address = address;
        this.dateOfBirth = dateOfBirth;
        this.sex = sex;
        this.email = email;
        this.creationTime = creationTime;
        this.status = status;
        this.lastest_access = lastest_access;
        this.isban = isban;
    }


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isIsban() {
        return isban;
    }

    public void setIsban(boolean isban) {
        this.isban = isban;
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

    @Override
    public String toString() {
        return "{" +
                "\"id\":" + id +
                ", \"username\":'" + username + '\'' +
                ", \"fullname\":'" + fullname + '\'' +
                ", \"address\":'" + address + '\'' +
                ", \"dateOfBirth\":'" + dateOfBirth + '\'' +
                ", \"sex\":'" + sex + '\'' +
                ", \"email\":'" + email + '\'' +
                ", \"creationTime\":'" + creationTime + '\'' +
                ", \"status\":'" + status + '\'' +
                ", \"lastest_access\":'" + lastest_access + '\'' +
                ", \"isban\":" + isban +
                '}';
    }
}
