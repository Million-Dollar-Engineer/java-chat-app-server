package chatapp.service;

import chatapp.repository.IUserRepository;
import chatapp.repository.impl.UserRepository;

public class UserService {
    private final IUserRepository userRepository;

    public UserService(IUserRepository userRepository) {
        this.userRepository = userRepository;
    }
}
