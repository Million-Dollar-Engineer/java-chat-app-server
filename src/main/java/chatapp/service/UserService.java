package chatapp.service;

import chatapp.repository.IUserRepository;

public class UserService {
    private final IUserRepository userRepository;

    public UserService(IUserRepository userRepository) {
        this.userRepository = userRepository;
    }
}
