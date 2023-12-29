package chatapp.controller;


import chatapp.dto.User;
import chatapp.entity.UserEntity;
import chatapp.repository.IAdminRepository;
import chatapp.repository.IGroupChatRepository;
import chatapp.repository.IUserRepository;
import chatapp.repository.impl.AdminRepository;
import chatapp.repository.impl.GroupChatRepository;
import chatapp.repository.impl.UserRepository;
import chatapp.service.AdminService;
import chatapp.service.GroupChatService;
import chatapp.service.UserService;
import chatapp.utils.Utils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Collections;
import java.util.List;


@RestController
@RequestMapping("/admin")
public class AdminController {
    public IAdminRepository repo;
    public AdminService service;
    public IUserRepository userRepo;
    public UserService userService;

    public IGroupChatRepository groupChatRepo;
    public GroupChatService groupChatService;

    public AdminController() {
        repo = new AdminRepository();
        service = new AdminService(repo);

        userRepo = new UserRepository();
        userService = new UserService(userRepo);

        groupChatRepo = new GroupChatRepository();
        groupChatService = new GroupChatService(groupChatRepo);
    }

    ResponseEntity<String> responseError(Exception e){
        String errorMessage = e.getMessage();
        errorMessage = errorMessage.replaceAll("\"", "\'");

        String jsonMessage = String.format("{\"error\": \"%s\"}", errorMessage);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .header("Content-Type", "application/json")
                .body(jsonMessage);
    }

    @GetMapping("/all-user")
    public ResponseEntity<String> allUserData(
            @RequestParam(name = "fullname", required = false) String fullname,
            @RequestParam(name = "username", required = false) String username,
            @RequestParam(name = "status", required = false) String status,
            @RequestParam(name = "sortBy", required = false) String sortBy,
            @RequestParam(name = "order", required = false) String order,
            @RequestParam(name = "startTime", required = false) String startTime,
            @RequestParam(name = "endTime", required = false) String endTime
    ) {
        try {
            System.out.println(fullname + username + status);
            String userData = service.readUserData(fullname, username, status, sortBy, order, startTime, endTime);
            String jsonMessage = String.format("{\"userData\": %s }", userData);
            return ResponseEntity.ok()
                    .header("Content-Type", "application/json")
                    .body(jsonMessage);
        } catch (Exception e) {
            return responseError(e);
        }
    }

    @PostMapping("/all-user/create-user")
    public ResponseEntity<String> createUser(@RequestBody UserEntity user) {
        try {
            user.setPassword(Utils.hashString(user.getPassword()));
            System.out.println(user.getPassword());
            userService.createUser(user);

            String jsonMessage = "{\"message\": \"User created successfully\"}";
            return ResponseEntity
                    .ok()
                    .header("Content-Type", "application/json").
                    body(jsonMessage);
        } catch (Exception e) {
            return responseError(e);
        }
    }

    @PatchMapping("/all-user/update-user")
    public ResponseEntity<String> changeUserData(@RequestBody UserEntity user) {
        try {
            service.updateUserData(user);
            String jsonMessage = "{\"message\": \"Updated successfully\"}";
            return ResponseEntity.ok()
                    .header("Content-Type", "application/json")
                    .body(jsonMessage);
        } catch (Exception e) {
            return responseError(e);
        }
    }

    @DeleteMapping("/all-user/{id}")
    public ResponseEntity<String> deleteUserData(@PathVariable String id) {
        try {
            service.deleteUserData(id);
            String jsonMessage = "{\"message\": \"Delete successfully\"}";
            return ResponseEntity.ok()
                    .header("Content-Type", "application/json")
                    .body(jsonMessage);
        } catch (Exception e) {
            return responseError(e);
        }
    }


    @PostMapping("/ban-account")
    public ResponseEntity<String> banAccount(@RequestBody UserEntity user) {
        try {
            service.setAccountStatus(user.getId(), "banned");
            String jsonMessage = String.format("{\"message\": \"User with id = %s is banned\"}", user.getId());
            return ResponseEntity.ok()
                    .header("Content-Type", "application/json")
                    .body(jsonMessage);
        } catch (Exception e) {
            return responseError(e);
        }
    }

    @PostMapping("/unban-account")
    public ResponseEntity<String> unbanAccount(@RequestBody UserEntity user) {
        try {
            service.setAccountStatus(user.getId(), "active");
            String jsonMessage = String.format("{\"message\": \"User with id = %s is unbanned\"}", user.getId());
            return ResponseEntity.ok()
                    .header("Content-Type", "application/json")
                    .body(jsonMessage);
        } catch (Exception e) {
            return responseError(e);
        }
    }

    @GetMapping("/login-histories")
    public ResponseEntity<String> getLoginHistories(
            @RequestParam(name = "orderBy", required = false) String order,
            @RequestParam(name = "username", required = false) String username
    ) {
        try {
            String loginData = service.getLoginHistories(order, username);
            String jsonMessage = String.format("{\"loginData\":  %s }", loginData);
            return ResponseEntity.ok()
                    .header("Content-Type", "application/json")
                    .body(jsonMessage);
        } catch (Exception e) {
            return responseError(e);
        }
    }

    @GetMapping("/friend-list/{user_id}")
    public List<User> getFriendList(@PathVariable String user_id) {
        try {
            List<User> friendList = userService.getFriendList(user_id);
            String jsonMessage = String.format("{\"friendList\":  %s }", "");
            return friendList;
        } catch (Exception e) {
            return Collections.EMPTY_LIST;
        }
    }

    @GetMapping("/group-chat")
    public ResponseEntity<String> getGroupChatList(
            @RequestParam(name = "sortBy", required = false) String sortBy,
            @RequestParam(name = "order", required = false) String order,
            @RequestParam(name = "name", required = false) String name
    ) {
        try {
            String groupData = groupChatService.getGroupChatList(sortBy, order, name);
            String jsonMessage = String.format("{\"groupList\":  %s }", groupData);
            return ResponseEntity.ok()
                    .header("Content-Type", "application/json")
                    .body(jsonMessage);
        } catch (Exception e) {
            return responseError(e);
        }
    }

    @GetMapping("/group-chat-member/{group_id}")
    public ResponseEntity<String> getGroupChatMember(
            @PathVariable String group_id,
            @RequestParam(name = "admin", required = false) String admin
    ) {
        try {
            String memberData = groupChatService.getGroupChatMember(group_id, admin);
            String jsonMessage = String.format("{\"members\":  %s }", memberData);
            return ResponseEntity.ok()
                    .header("Content-Type", "application/json")
                    .body(jsonMessage);
        } catch (Exception e) {
            return responseError(e);
        }
    }

    @GetMapping("/spam-reports")
    public ResponseEntity<String> getSpamReports(
            @RequestParam(name = "sortBy", required = false) String sortBy,
            @RequestParam(name = "order", required = false) String order,
            @RequestParam(name = "startTime", required = false) String startTime,
            @RequestParam(name = "endTime", required = false) String endTime,
            @RequestParam(name = "username", required = false) String username
    ) {
        try {
            String spamReportData = service.getSpamReport(sortBy, order, startTime, endTime, username);
            String jsonMessage = String.format("{\"spamReports\":  %s }", spamReportData);
            return ResponseEntity.ok()
                    .header("Content-Type", "application/json")
                    .body(jsonMessage);
        } catch (Exception e) {
            return responseError(e);
        }
    }

    @GetMapping("/users-each-month")
    public ResponseEntity<String> getUserCreatedEachMonth(
            @RequestParam(name = "year", required = true) int year
    ) {
        try {
            String numberOfUserData = service.getNumberOfUserEachMonth(year);
            String jsonMessage = String.format("{\"data\":  %s }", numberOfUserData);
            return ResponseEntity.ok()
                    .header("Content-Type", "application/json")
                    .body(jsonMessage);
        } catch (Exception e) {
            return responseError(e);
        }
    }

    @GetMapping("/friend-and-fof")
    public ResponseEntity<String> getFriendsAndFriendsOfFriends(
            @RequestParam(name = "sortBy", required = false) String sortBy,
            @RequestParam(name = "order", required = false) String order,
            @RequestParam(name = "username", required = false) String name,
            @RequestParam(name = "equal", required = false) String equal,
            @RequestParam(name = "greaterThan", required = false) String greaterThan,
            @RequestParam(name = "lowerThan", required = false) String lowerThan
    ) {
        try {
            String data = service.getNumberOfFriendAndFOF(sortBy, order, name, greaterThan, lowerThan, equal);
            String jsonMessage = String.format("{\"data\":  %s }", data);
            return ResponseEntity.ok()
                    .header("Content-Type", "application/json")
                    .body(jsonMessage);
        } catch (Exception e) {
            return responseError(e);
        }
    }

    @GetMapping("/users-active-each-month")
    public ResponseEntity<String> getUserActiveEachMonth(
            @RequestParam(name = "year", required = true) int year
    ) {
        try {
            String numberOfUserData = service.getNumberOfUserActiveEachMonth(year);
            String jsonMessage = String.format("{\"data\":  %s }", numberOfUserData);
            return ResponseEntity.ok()
                    .header("Content-Type", "application/json")
                    .body(jsonMessage);
        } catch (Exception e) {
            return responseError(e);
        }
    }

    @GetMapping("/active-user-and-relevant-info")
    public ResponseEntity<String> getActiveUsersAndInfo(
            @RequestParam(name = "sortBy", required = false) String sortBy,
            @RequestParam(name = "order", required = false) String order,
            @RequestParam(name = "startTime", required = true) String startTime,
            @RequestParam(name = "endTime", required = true) String endTime,
            @RequestParam(name = "username", required = false) String username,
            @RequestParam(name = "equal", required = false) String equal,
            @RequestParam(name = "greaterThan", required = false) String greaterThan,
            @RequestParam(name = "lowerThan", required = false) String lowerThan

    ) {
        try {
            String data = service.getActiveUsersOperation(sortBy,order,startTime, endTime,
                    username, greaterThan, lowerThan, equal);
            String jsonMessage = String.format("{\"data\":  %s }", data);
            return ResponseEntity.ok()
                    .header("Content-Type", "application/json")
                    .body(jsonMessage);
        } catch (Exception e) {
            return responseError(e);
        }
    }
}
