package chatapp.entity;

public class FriendRequestEntity {
    public String user_id;
    public String friend_id;

    public FriendRequestEntity(String user_id, String friend_id) {
        this.user_id = user_id;
        this.friend_id = friend_id;
    }
}
