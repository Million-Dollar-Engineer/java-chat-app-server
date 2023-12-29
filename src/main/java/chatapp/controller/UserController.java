package chatapp.controller;


import chatapp.dto.User;
import chatapp.entity.ConnectionEntity;
import chatapp.entity.FriendRequestEntity;
import chatapp.entity.UserEntity;
import chatapp.repository.IUserRepository;
import chatapp.repository.impl.UserRepository;
import chatapp.service.UserService;
import chatapp.socket.SocketComponent;
import chatapp.utils.Utils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping("/user")
public class UserController {

    public static UserService service;
    public IUserRepository repo;

    UserController() {
        repo = new UserRepository();
        service = new UserService(repo);
    }

    public static boolean isUserInGroup(String userId, String groupId) throws Exception {
        return service.isUserInGroup(userId, groupId);
    }

    public static String getUserIdByUsername(String username) throws Exception {
        return service.getUserIdByUsername(username);
    }

    public static String getUsernameById(String id) throws Exception {
        return service.getUserNameById(id);
    }

    ResponseEntity<String> responseError(Exception e) {
        String errorMessage = e.getMessage();
        errorMessage = errorMessage.replaceAll("\"", "\'");

        String jsonMessage = String.format("{\"error\": \"%s\"}", errorMessage);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .header("Content-Type", "application/json")
                .body(jsonMessage);
    }

    @GetMapping()
    public String hello() {
        return "Hello user 123";
    }

    @PostMapping(path = "/signup")
    public ResponseEntity<String> createUser(@RequestBody UserEntity user) {
        try {
            user.setPassword(Utils.hashString(user.getPassword()));
            System.out.println(user.getPassword());
            service.createUser(user);

            String jsonMessage = "{\"message\": \"User created successfully\"}";
            return ResponseEntity
                    .ok()
                    .header("Content-Type", "application/json").
                    body(jsonMessage);
        } catch (Exception e) {
            return responseError(e);
        }
    }

    @PostMapping(path = "/login")
    public ResponseEntity<String> loginUser(@RequestBody UserEntity user, HttpServletRequest request) {
        try {
            user.setPassword(Utils.hashString(user.getPassword()));
            System.out.println(user.getPassword());
            String id = service.findUserForLogin(user, service.getClientIP(request));
            System.out.println(id);

            String jsonMessage;
            jsonMessage = String.format("{\"message\": \"Login successfully\", \"id\": \"%s\"}", id);
            return ResponseEntity
                    .ok()
                    .header("Content-Type", "application/json").
                    body(jsonMessage);
        } catch (Exception e) {
            return responseError(e);
        }
    }

    @PostMapping(path = "/reset-password")
    public ResponseEntity<String> sendPasswordUserToEmail(@RequestBody UserEntity user) {
        try {
            service.resetAndSendPasswordToEmail(user);
            String jsonMessage;
            jsonMessage = String.format("{\"message\": \"Password was sent through your email\"}");

            return ResponseEntity
                    .ok()
                    .header("Content-Type", "application/json").
                    body(jsonMessage);
        } catch (Exception e) {
            return responseError(e);
        }
    }

    @GetMapping("/add-friend")
    public ResponseEntity<String> sendFriendRequest(
            @RequestParam String user_id, @RequestParam String friend_user_name
    ) {
        try {
            service.saveFriendRequest(user_id, UserController.getUserIdByUsername(friend_user_name));
            String jsonMessage;
            jsonMessage = String.format("{\"message\": \"Friend request was sent\"}");

            return ResponseEntity
                    .ok()
                    .header("Content-Type", "application/json").
                    body(jsonMessage);
        } catch (Exception e) {
            return responseError(e);
        }
    }

    @PatchMapping("/accept-friend")
    public ResponseEntity<String> acceptFriendRequest(
            @RequestBody FriendRequestEntity friendRequest
    ) {
        try {
            service.acceptFriendRequest(friendRequest.user_id, UserController.getUserIdByUsername(friendRequest.friend_user_name));
            String jsonMessage;
            jsonMessage = String.format("{\"message\": \"Friend request was accepted\"}");

            return ResponseEntity
                    .ok()
                    .header("Content-Type", "application/json").
                    body(jsonMessage);
        } catch (Exception e) {
            return responseError(e);
        }
    }

    @PutMapping("/unfriend")
    public ResponseEntity<String> unFriend(
            @RequestBody FriendRequestEntity friendRequest
    ) {
        try {
            service.unFriend(friendRequest.user_id, UserController.getUserIdByUsername(friendRequest.friend_user_name));
            String jsonMessage;
            jsonMessage = String.format("{\"message\": \"Friend or friend request was deleted\"}");

            return ResponseEntity
                    .ok()
                    .header("Content-Type", "application/json").
                    body(jsonMessage);
        } catch (Exception e) {
            return responseError(e);
        }
    }

    @GetMapping("/friend-list")
    public List<User> getFriendList(@RequestParam String id) throws Exception {
        return service.getFriendList(id);
    }

    @GetMapping("/online-friend-list")
    public List<User> getOnlineFriendList(@RequestParam String id) throws Exception {
        List<User> friends = service.getFriendList(id);
        List<User> result = new ArrayList<>();
        for (User friend : friends) {
            for (ConnectionEntity connection : SocketComponent.connections) {
                String friendId = UserController.getUserIdByUsername(friend.getUserName());
                if (connection.getUserId().equals(friendId)) {
                    result.add(friend);
                }
            }
        }

        return result;
    }

    @GetMapping("/list")
    public User getUserByUserName(@RequestParam String username) throws Exception {
        User user = service.getUserByUsername(username);
        if (user == null) {
            throw new Exception("User not found");
        }
        return service.getUserByUsername(username);
    }

    @GetMapping("/friend-request-list")
    public List<User> getFriendRequestList(@RequestParam String id) throws Exception {
        return service.getFriendRequestList(id);
    }
}
