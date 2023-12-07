package chatapp.controller;

import chatapp.repository.IUserRepository;
import chatapp.repository.impl.UserRepository;
import chatapp.service.UserService;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
    private final UserService service;

    public UserController() {
        IUserRepository userRepository = new UserRepository();
        this.service = new UserService(userRepository);
    }
}
