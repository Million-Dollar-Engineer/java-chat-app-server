package chatapp.controller;


import chatapp.entity.UserEntity;
import chatapp.repository.IUserRepository;
import chatapp.repository.impl.UserRepository;
import chatapp.service.UserService;
import chatapp.utils.Utils;
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
            String jsonMessage = String.format("{\"message\": \"%s\"}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .header("Content-Type", "application/json")
                    .body(jsonMessage);
        }
    }

    @PostMapping(path = "/login")
    public ResponseEntity<String> loginUser(@RequestBody UserEntity user) {
        try {
            user.setPassword(Utils.hashString(user.getPassword()));
            System.out.println(user.getPassword());
            int id = service.findUserForLogin(user);
            System.out.println(id);

            String jsonMessage;
            if (id == -2) {
                jsonMessage = String.format("{\"message\": \"Your account is banned\", \"id\": \"%s\"}",
                        id);
            } else if (id != -1) {
                jsonMessage = String.format("{\"message\": \"Login successfully\", \"id\": \"%s\"}",
                        id);
            } else {
                jsonMessage = String.format("{\"message\": \"Username or password is incorrect\"}");
            }
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
            String jsonMessage = String.format("{\"message\": \"%s\"}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .header("Content-Type", "application/json")
                    .body(jsonMessage);
        }
    }
}
