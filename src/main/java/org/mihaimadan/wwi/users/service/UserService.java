package org.mihaimadan.wwi.users.service;

import org.mihaimadan.wwi.users.model.CreateUserRequest;
import org.mihaimadan.wwi.users.model.User;
import org.mihaimadan.wwi.users.repository.UserRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(PasswordEncoder passwordEncoder, UserRepository userRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

    public User getById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "no user found with given id"));
    }

    public void registerUser(CreateUserRequest createUserReq) {
        if (userRepository.existsByEmailAddress(createUserReq.getEmailAddress())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "User with this email already exists");
        }
        User newUser = new User();
        BeanUtils.copyProperties(createUserReq, newUser);

        String encodedPassword = passwordEncoder.encode(newUser.getPassword());
        newUser.setPassword(encodedPassword);

        userRepository.save(newUser);
    }
}
