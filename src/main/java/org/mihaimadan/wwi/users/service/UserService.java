package org.mihaimadan.wwi.users.service;

import org.mihaimadan.wwi.users.model.PasswordToken;
import org.mihaimadan.wwi.users.model.dto.CreateUserRequest;
import org.mihaimadan.wwi.users.model.User;
import org.mihaimadan.wwi.users.repository.PasswordTokenRepository;
import org.mihaimadan.wwi.users.repository.UserRepository;
import org.mihaimadan.wwi.users.service.event.PasswordResetCompleteEvent;
import org.springframework.beans.BeanUtils;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordTokenRepository passwordTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final ApplicationEventPublisher eventPublisher;

    public UserService(PasswordEncoder passwordEncoder, UserRepository userRepository,
                       PasswordTokenRepository passwordTokenRepository,
                       ApplicationEventPublisher eventPublisher) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.passwordTokenRepository = passwordTokenRepository;
        this.eventPublisher = eventPublisher;
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

    public void createForgotPasswordToken(String email) {
        if(!userRepository.existsByEmailAddress(email)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No user found with registered email");
        }

        PasswordToken foundTokenForEmail = passwordTokenRepository.findByEmail(email);
        if(foundTokenForEmail != null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "There's already a password flow initialised for this email!");
        }

        String token = UUID.randomUUID().toString();
        PasswordToken passwordToken = PasswordToken.builder()
                .token(token)
                .email(email)
                .build();
        passwordTokenRepository.save(passwordToken);
        eventPublisher.publishEvent(new PasswordResetCompleteEvent(email, token));
    }

    public void updateUserPassword(String token, String newPassword) {
        PasswordToken passwordToken = passwordTokenRepository.findByToken(token)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No password token entry found with given token"));

        User userToBeUpdated = userRepository.findByEmailAddress(passwordToken.getEmail())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No user found with password token email"));
        userToBeUpdated.setPassword(passwordEncoder.encode(newPassword));

        userRepository.save(userToBeUpdated);
        passwordTokenRepository.delete(passwordToken);
    }
}
