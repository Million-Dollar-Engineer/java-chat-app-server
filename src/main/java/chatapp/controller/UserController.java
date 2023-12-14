package chatapp.controller;


import chatapp.entity.FriendRequest;
import chatapp.entity.UserEntity;
import chatapp.repository.IUserRepository;
import chatapp.repository.impl.UserRepository;
import chatapp.service.UserService;
import chatapp.utils.Utils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/user")
public class UserController {

    public IUserRepository repo;
    public UserService service;

    UserController() {
        repo = new UserRepository();
        service = new UserService(repo);
    }


    ResponseEntity<String> responseError(Exception e){
        String jsonMessage = String.format("{\"error\": \"%s\"}", e.getMessage());
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

    @PostMapping("/add-friend")
    public ResponseEntity<String> sendFriendRequest(
            @RequestBody FriendRequest friendRequest
            ) {
        try {
            service.saveFriendRequest(friendRequest.user_id, friendRequest.friend_id);
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
            @RequestBody FriendRequest friendRequest
    ) {
        try {
            service.acceptFriendRequest(friendRequest.user_id, friendRequest.friend_id);
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
            @RequestBody FriendRequest friendRequest
    ) {
        try {
            service.unFriend(friendRequest.user_id, friendRequest.friend_id);
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
    public ResponseEntity<String> getFriendList(@RequestBody UserEntity user) {
        try {
            String friendListData = service.getFriendList(user.getId());
            String jsonMessage;
            jsonMessage = String.format("{\"friendList\": %s }", friendListData);

            return ResponseEntity
                    .ok()
                    .header("Content-Type", "application/json").
                    body(jsonMessage);
        } catch (Exception e) {
            return responseError(e);
        }
    }
}
