package org.mihaimadan.wwi.users.service;

import org.mihaimadan.wwi.users.model.CreateUserRequest;
import org.mihaimadan.wwi.users.model.User;
import org.mihaimadan.wwi.users.repository.UserRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(PasswordEncoder passwordEncoder, UserRepository userRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

    public void createUser(CreateUserRequest createUserReq) {
        User newUser = new User();
        BeanUtils.copyProperties(createUserReq, newUser);

        String encodedPassword = passwordEncoder.encode(newUser.getPassword());
        newUser.setPassword(encodedPassword);

        userRepository.save(newUser);
    }
}
