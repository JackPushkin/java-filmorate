package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ru.yandex.practicum.filmorate.exeption.UpdateUsersListException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.validator.Validator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {

    private final Map<Integer, User> users = new HashMap<>();
    private int userId = 1;

    @PostMapping
    public User addUser(@RequestBody User user) {
        Validator.userFormatValidation(user);
        Validator.userRegisteredValidation(user, users);
        user.setId(userId);
        users.put(userId++, user);
        return user;
    }

    @PutMapping
    public User updateUser(@RequestBody User user) {
        if (!users.containsKey(user.getId())) {
            throw new UpdateUsersListException("User with such id does not exist");
        }
        Validator.userFormatValidation(user);
        Validator.userRegisteredValidation(user, users);
        users.put(user.getId(), user);
        return user;
    }

    @GetMapping
    public List<User> getUsers() {
        return new ArrayList<>(users.values());
    }
}
