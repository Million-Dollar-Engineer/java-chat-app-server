package chatapp.controller;


import chatapp.dto.User;
import chatapp.entity.ConnectionEntity;
import chatapp.entity.GroupChatEntity;
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

    @GetMapping("/accept-friend")
    public ResponseEntity<String> acceptFriendRequest(
            @RequestParam String user_id, @RequestParam String friend_user_name
    ) {
        try {
            service.acceptFriendRequest(user_id, UserController.getUserIdByUsername(friend_user_name));
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

    @GetMapping("/unfriend")
    public ResponseEntity<String> unFriend(
            @RequestParam String user_id, @RequestParam String friend_user_name
    ) {
        try {
            service.unFriend(user_id, UserController.getUserIdByUsername(friend_user_name));
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

    @GetMapping("block-user")
    public ResponseEntity<String> blockUser(@RequestParam String user_id, @RequestParam String block_user_name) {
        try {
            service.blockUser(user_id, UserController.getUserIdByUsername(block_user_name));
            String jsonMessage;
            jsonMessage = String.format("{\"message\": \"User was blocked\"}");

            return ResponseEntity
                    .ok()
                    .header("Content-Type", "application/json").
                    body(jsonMessage);
        } catch (Exception e) {
            return responseError(e);
        }
    }

    @GetMapping("is-blocked")
    public Boolean isBlocked(@RequestParam String user_id, @RequestParam String block_user_name) {
        try {
            return service.isBlocked(user_id, UserController.getUserIdByUsername(block_user_name));
        } catch (Exception e) {
            return false;
        }
    }

    @GetMapping("report-spam")
    public ResponseEntity<String> reportSpam(@RequestParam String user_id, @RequestParam String spam_user_name, @RequestParam String reason) {
        try {
            service.reportSpam(user_id, UserController.getUserIdByUsername(spam_user_name), reason);
            String jsonMessage;
            jsonMessage = String.format("{\"message\": \"User was reported\"}");

            return ResponseEntity
                    .ok()
                    .header("Content-Type", "application/json").
                    body(jsonMessage);
        } catch (Exception e) {
            return responseError(e);
        }
    }

    @GetMapping("create-group")
    public ResponseEntity<String> createGroup(@RequestParam String user_id, @RequestParam String group_name) {
        try {
            service.createGroup(user_id, group_name);
            String jsonMessage;
            jsonMessage = String.format("{\"message\": \"Group was created\"}");

            return ResponseEntity
                    .ok()
                    .header("Content-Type", "application/json").
                    body(jsonMessage);
        } catch (Exception e) {
            return responseError(e);
        }
    }

    @GetMapping("add-user-to-group")
    public ResponseEntity<String> addUserToGroup(@RequestParam String user_id, @RequestParam String group_id, @RequestParam String user_name) throws Exception {
        if (!isUserInGroup(user_id, group_id)) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .header("Content-Type", "application/json")
                    .body("{\"error\": \"you dont have permission\"}");
        }

        try {
            service.addUserToGroup(group_id, UserController.getUserIdByUsername(user_name));
            String jsonMessage;
            jsonMessage = String.format("{\"message\": \"User was added to group\"}");

            return ResponseEntity
                    .ok()
                    .header("Content-Type", "application/json").
                    body(jsonMessage);
        } catch (Exception e) {
            return responseError(e);
        }
    }

    @GetMapping("list-my-group")
    public List<GroupChatEntity> listMyGroup(@RequestParam String user_id) throws Exception {
        return service.listMyGroup(user_id);
    }
}
