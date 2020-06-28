package org.mihaimadan.wwi;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mihaimadan.wwi.users.model.User;
import org.mihaimadan.wwi.users.model.dto.EditUserRequest;
import org.mihaimadan.wwi.users.repository.UserRepository;
import org.mihaimadan.wwi.users.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.server.ResponseStatusException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class EditUserDataTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Test
    public void test_user_data_edit() {
        User userToBeUpdated = userRepository.findById(3263L)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "user with given id not found"));

        EditUserRequest editRequest = new EditUserRequest();
        BeanUtils.copyProperties(userToBeUpdated, editRequest);
        editRequest.setDeliveryAddress("Strada Buzesti");

        assertDoesNotThrow(() -> userService.editUserData(3263L, editRequest));

        String updatedDeliveryAddress = userRepository.findById(3263L)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "user with given id not found"))
                .getDeliveryAddress();

        assertThat(updatedDeliveryAddress)
                .isEqualTo("Strada Buzesti");
    }
}
