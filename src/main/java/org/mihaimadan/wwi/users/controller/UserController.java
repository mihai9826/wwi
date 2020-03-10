package org.mihaimadan.wwi.users.controller;

import org.mihaimadan.wwi.users.model.User;
import org.mihaimadan.wwi.users.model.UserDTO;
import org.mihaimadan.wwi.users.repository.UserRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
public class UserController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserController(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/users")
    public UserDTO findUserByEmail(@RequestParam String email) {
       UserDTO userDTO = new UserDTO();
       User theUser = userRepository.findByEmailAddress(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "no user found with given email"));

        BeanUtils.copyProperties(theUser, userDTO, "password");
        return userDTO;
    }

    @PutMapping("/users/{userId}")
    public UserDTO updateUser(@RequestBody UserDTO userDTO, @PathVariable Long userId) {
        User userToBeUpdated = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "no user found with given email"));
        String newPassword = userDTO.getPassword();
        userToBeUpdated.setPassword(passwordEncoder.encode(newPassword));
        BeanUtils.copyProperties(userDTO, userToBeUpdated, "password");
        userRepository.save(userToBeUpdated);
        BeanUtils.copyProperties(userToBeUpdated, userDTO);
        return userDTO;
    }

}