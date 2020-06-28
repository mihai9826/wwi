package org.mihaimadan.wwi;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mihaimadan.wwi.users.repository.PasswordTokenRepository;
import org.mihaimadan.wwi.users.repository.UserRepository;
import org.mihaimadan.wwi.users.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class ForgotPasswordTest {


    @Autowired
    private UserService userService;

    @Autowired
    private PasswordTokenRepository passwordTokenRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private final String email = "mihai.ioan98@gmail.com";
    private final String newPassword = "123456789";

    @Test
    public void test_token_generation_email_send() {
        assertDoesNotThrow(() -> userService.createForgotPasswordToken(email));

        assertNotNull(passwordTokenRepository.findByEmail(email));
    }

    @Test
    public void test_password_change() {
        String token = passwordTokenRepository.findByEmail(email).getToken();

        assertDoesNotThrow(() -> userService.updateUserPassword(token, newPassword));

        String updatedPassword = userRepository.findByEmailAddress(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "no user found with given email"))
                .getPassword();

        assertTrue(passwordEncoder.matches(newPassword, updatedPassword));
    }
}
