package org.mihaimadan.wwi;


import org.junit.Test;
import org.junit.runner.RunWith;

import org.mihaimadan.wwi.users.model.User;
import org.mihaimadan.wwi.users.model.dto.CreateUserRequest;
import org.mihaimadan.wwi.users.repository.UserRepository;
import org.mihaimadan.wwi.users.service.UserService;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.server.ResponseStatusException;


import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class RegisterNewUserTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserService userService;

    @Test
    public void test_user_registration() {
        CreateUserRequest usr = new CreateUserRequest();
        usr.setEmailAddress("testtt@gmail.com");
        usr.setDeliveryAddress("sss ccc");
        usr.setFullName("mihai ioan");
        usr.setPassword("123456");
        usr.setPhoneNumber("123456789");
        usr.setRole("CLIENT");

        assertDoesNotThrow(() -> userService.registerUser(usr));

        User registered = userRepository.findByEmailAddress("testtt@gmail.com")
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "sss"));

        assertThat(registered.getRole())
                .isEqualTo("CLIENT");
    }


}
