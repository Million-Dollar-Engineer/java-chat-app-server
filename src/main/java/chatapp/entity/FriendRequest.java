package chatapp.entity;

public class FriendRequest {
    public String user_id;
    public String friend_id;

    public FriendRequest(String user_id, String friend_id) {
        this.user_id = user_id;
        this.friend_id = friend_id;
    }
}
