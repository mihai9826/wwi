package org.mihaimadan.wwi.users.service;

import org.mihaimadan.wwi.users.model.Favorites;
import org.mihaimadan.wwi.users.model.PasswordToken;
import org.mihaimadan.wwi.users.model.dto.CreateUserRequest;
import org.mihaimadan.wwi.users.model.User;
import org.mihaimadan.wwi.users.model.dto.EditUserRequest;
import org.mihaimadan.wwi.users.repository.PasswordTokenRepository;
import org.mihaimadan.wwi.users.repository.UserRepository;
import org.mihaimadan.wwi.users.service.event.PasswordResetCompleteEvent;
import org.mihaimadan.wwi.warehouse.model.StockItem;
import org.mihaimadan.wwi.warehouse.repository.StockItemRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final StockItemRepository stockItemRepository;
    private final PasswordTokenRepository passwordTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final ApplicationEventPublisher eventPublisher;

    public UserService(PasswordEncoder passwordEncoder, StockItemRepository stockItemRepository,
                       UserRepository userRepository,
                       PasswordTokenRepository passwordTokenRepository,
                       ApplicationEventPublisher eventPublisher) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.stockItemRepository = stockItemRepository;
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

    public void editUserData(Long id, EditUserRequest editUserRequest) {
        User userToBeUpdated = userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "user with given id not found"));

        BeanUtils.copyProperties(editUserRequest, userToBeUpdated, "password");

        String newPassword = editUserRequest.getPassword();
        userToBeUpdated.setPassword(passwordEncoder.encode(newPassword));

        userRepository.save(userToBeUpdated);
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

    public void updateUserFavorites(Long userId, Long itemId) {
        User userToBeUpdated = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No user found with given id"));

        StockItem itemToAdd = stockItemRepository.findById(itemId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No item found with given id"));

        //check if the item is already added to favorites
        if (userToBeUpdated.getFavoriteItems().isEmpty()) {
            Favorites newFavorite = new Favorites();
            newFavorite.setStockItem(itemToAdd);
            userToBeUpdated.addFavorite(newFavorite);
            userRepository.save(userToBeUpdated);
            return;
        }

        boolean itemExists = userToBeUpdated.getFavoriteItems().stream().anyMatch(obj ->
                obj.getStockItem().getStockItemId() == itemToAdd.getStockItemId());

        if (!itemExists) {
            Favorites newFavorite = new Favorites();
            newFavorite.setStockItem(itemToAdd);
            userToBeUpdated.addFavorite(newFavorite);
            userRepository.save(userToBeUpdated);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Duplicate favorite entries");
        }
    }

    public List<StockItem> getFavoritesOfUser(Long itemId) {
        User theUser = userRepository.findById(itemId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User with id doesn't exist"));

        return theUser.getFavoriteItems().stream().map(Favorites::getStockItem).collect(Collectors.toList());
    }

    public void deleteUserFavorites(Long userId, Long itemId) {
        User theUser = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User with id doesn't exist"));

        Favorites deleteFavorite = theUser.getFavoriteItems().stream()
                .filter(x -> x.getStockItem().getStockItemId() == itemId).findAny()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No favourite item found " +
                        "with given item id"));

        theUser.removeFavorite(deleteFavorite);

        userRepository.save(theUser);
    }
}
