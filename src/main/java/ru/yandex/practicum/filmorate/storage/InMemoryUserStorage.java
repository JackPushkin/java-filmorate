package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exeption.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.interfaces.UserStorage;
import ru.yandex.practicum.filmorate.validator.Validator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage {
    private final Map<Integer, User> users = new HashMap<>();
    private int userId = 1;

    @Override
    public User addUser(User user) {
        Validator.userFormatValidation(user);
        Validator.userRegisteredValidation(user, users);
        user.setId(userId);
        users.put(userId++, user);
        log.debug("Add user: {}", user);
        return user;
    }

    @Override
    public User updateUser(User user) {
        if (!users.containsKey(user.getId())) {
            throw new UserNotFoundException(String.format("User with id=%d not found", userId));
        }
        Validator.userFormatValidation(user);
        Validator.userRegisteredValidation(user, users);
        users.put(user.getId(), user);
        log.debug("Update user: {}", user);
        return user;
    }

    @Override
    public List<User> getUsers() {
        return new ArrayList<>(users.values());
    }

    @Override
    public User getUserById(Integer userId) {
        User user = users.get(userId);
        if (user == null) {
            throw new UserNotFoundException(String.format("User with id=%d not found", userId));
        }
        return user;
    }
}
