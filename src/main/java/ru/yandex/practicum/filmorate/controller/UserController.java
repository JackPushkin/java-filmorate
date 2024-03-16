package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.interfaces.UserService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.List;

@RestController
@RequestMapping("/users")
@Validated
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public User addUser(@Valid @RequestBody User user) {
        return userService.addUser(user);
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User user) {
        return userService.updateUser(user);
    }

    @GetMapping
    public List<User> getUsers() {
        return userService.getUsers();
    }

    @GetMapping("/{userId}")
    public User getUserById(@PathVariable @Positive Integer userId) {
        return userService.getUserById(userId);
    }

    @PutMapping("/{userId}/friends/{friendId}")
    public void addFriend(@PathVariable @Positive Integer userId,
                          @PathVariable @Positive Integer friendId) {
        userService.addFriend(userId, friendId);
    }

    @DeleteMapping("/{userId}/friends/{friendId}")
    public void deleteFriend(@PathVariable @Positive Integer userId,
                             @PathVariable @Positive Integer friendId) {
        userService.deleteFriend(userId, friendId);
    }

    @GetMapping("/{userId}/friends")
    public List<User> getFriendsList(@PathVariable @Positive Integer userId) {
        return userService.getFriendsList(userId);
    }

    @GetMapping("/{userId}/friends/common/{otherUserId}")
    public List<User> getCommonFriendsList(@PathVariable @Positive Integer userId,
                                           @PathVariable @Positive Integer otherUserId) {
        return userService.getCommonFriendsList(userId, otherUserId);
    }
}
