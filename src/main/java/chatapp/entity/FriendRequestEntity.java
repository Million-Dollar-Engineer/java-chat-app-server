package chatapp.entity;

public class FriendRequestEntity {
    public String user_id;
    public String friend_user_name;

    public FriendRequestEntity(String user_id, String friend_id) {
        this.user_id = user_id;
        this.friend_user_name = friend_id;
    }
}
