package chatapp.controller;


import chatapp.entity.UserEntity;
import chatapp.repository.IAdminRepository;
import chatapp.repository.IUserRepository;
import chatapp.repository.impl.AdminRepository;
import chatapp.repository.impl.UserRepository;
import chatapp.service.AdminService;
import chatapp.service.UserService;
import chatapp.utils.Utils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/admin")
public class AdminController {
    public IAdminRepository repo;
    public AdminService service;
    public IUserRepository userRepo;
    public UserService userService;

    public AdminController() {
        repo = new AdminRepository();
        service = new AdminService(repo);

        userRepo = new UserRepository();
        userService = new UserService(userRepo);
    }

    @GetMapping("/all-user")
    public ResponseEntity<String> allUserData(
            @RequestParam(name = "fullname", required = false) String fullname,
            @RequestParam(name = "username", required = false) String username,
            @RequestParam(name = "status", required = false) String status
    ) {
        try {
            System.out.println(fullname + username + status);
            String userData = service.readUserData(fullname, username, status);
            String jsonMessage = String.format("{\"userData\": %s }", userData);
            return ResponseEntity.ok()
                    .header("Content-Type", "application/json")
                    .body(jsonMessage);
        } catch (Exception e) {
            String jsonMessage = String.format("{\"message\": \"%s\"}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .header("Content-Type", "application/json")
                    .body(jsonMessage);
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
            String jsonMessage = String.format("{\"message\": \"%s\"}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .header("Content-Type", "application/json")
                    .body(jsonMessage);
        }
    }

    @PatchMapping("/all-user/update-user")
    public ResponseEntity<String> changeUserData(@RequestBody UserEntity user) {
        try {
            service.updateUserData(user);
            String jsonMessage = String.format("{\"message\": %s }", "Update successfully");
            return ResponseEntity.ok()
                    .header("Content-Type", "application/json")
                    .body(jsonMessage);
        } catch (Exception e) {
            String jsonMessage = String.format("{\"message\": \"%s\"}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .header("Content-Type", "application/json")
                    .body(jsonMessage);
        }
    }

    @DeleteMapping("/all-user/{id}")
    public ResponseEntity<String> deleteUserData(@PathVariable String id) {
        try {
            service.deleteUserData(id);
            String jsonMessage = String.format("{\"message\": %s }", "Delete successfully");
            return ResponseEntity.ok()
                    .header("Content-Type", "application/json")
                    .body(jsonMessage);
        } catch (Exception e) {
            String jsonMessage = String.format("{\"message\": \"%s\"}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .header("Content-Type", "application/json")
                    .body(jsonMessage);
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
            String jsonMessage = String.format("{\"message\": \"%s\"}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .header("Content-Type", "application/json")
                    .body(jsonMessage);
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
            String jsonMessage = String.format("{\"message\": \"%s\"}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .header("Content-Type", "application/json")
                    .body(jsonMessage);
        }
    }

    @GetMapping("/login-histories")
    public ResponseEntity<String> getLoginHistories(
            @RequestParam(name = "orderBy", required = false) String order
    ) {
        try {
            String loginData = service.getLoginHistories(order);
            String jsonMessage = String.format("{\"loginData\":  %s }", loginData);
            return ResponseEntity.ok()
                    .header("Content-Type", "application/json")
                    .body(jsonMessage);
        } catch (Exception e) {
            String jsonMessage = String.format("{\"message\": \"%s\"}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .header("Content-Type", "application/json")
                    .body(jsonMessage);
        }
    }
}
