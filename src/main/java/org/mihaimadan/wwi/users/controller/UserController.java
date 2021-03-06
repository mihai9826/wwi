package org.mihaimadan.wwi.users.controller;

import org.mihaimadan.wwi.users.model.dto.*;
import org.mihaimadan.wwi.users.model.User;
import org.mihaimadan.wwi.users.repository.UserRepository;
import org.mihaimadan.wwi.users.service.UserService;
import org.mihaimadan.wwi.warehouse.model.dto.StockItemClientRespDTO;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
public class UserController {

    private final UserRepository userRepository;
    private final UserService userService;


    public UserController(UserRepository userRepository, UserService userService) {
        this.userRepository = userRepository;
        this.userService = userService;
    }

    @PostMapping("/password-token")
    public void initiatePasswordReset(@RequestBody PasswordTokenInitializationRequest passwordTokenReq) {
        userService.createForgotPasswordToken(passwordTokenReq.getEmail());
    }

    @PutMapping("/password")
    public void confirmPasswordReset(@RequestParam String token,
                                     @RequestBody PasswordTokenConfirmationRequest passwordTokenConfirmationRequest) {
        userService.updateUserPassword(token, passwordTokenConfirmationRequest.getPassword());
    }

    @GetMapping("/admin/users")
    public Page<UserDTO> getAllUsersForAdmin(@RequestParam int page, @RequestParam int size) {
        return userService.getAllUsersForAdmin(page, size);
    }

    @DeleteMapping("/admin/users/{id}")
    public void deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
    }

    @PutMapping("/admin/users/{id}")
    public void adminUpdateUserRole(@PathVariable Long id, @RequestBody String newRole) {
        userService.adminUpdateUserRole(id, newRole);

    }


    @GetMapping("/users")
    public UserDTO findUserByEmail(@RequestParam String email) {
       UserDTO userDTO = new UserDTO();
       User theUser = userRepository.findByEmailAddress(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "no user found with given email"));

        BeanUtils.copyProperties(theUser, userDTO);
        return userDTO;
    }

    @PostMapping("/users")
    public void registerUser(@RequestBody CreateUserRequest createUserReq) { userService.registerUser(createUserReq); }

    @PutMapping("/users/{id}")
    public void updateUser(@PathVariable Long id, @RequestBody EditUserRequest editUserRequest) {
        userService.editUserData(id, editUserRequest);
    }


    @GetMapping("/client/users/{id}/favorites")
    public List<StockItemClientRespDTO> getFavoritesOfUser(@PathVariable Long id) {
        return userService.getFavoritesOfUser(id);
    }

    @PutMapping("/client/users/{id}/favorites")
    public void updateUserFavorites(@PathVariable Long id, @RequestBody Long itemId) {
        userService.updateUserFavorites(id, itemId);
    }

    @DeleteMapping("/client/users/{id}/favorites/{itemId}")
    public void deleteUserFavorite(@PathVariable Long id, @PathVariable Long itemId) {
        userService.deleteUserFavorites(id, itemId);
    }
}
