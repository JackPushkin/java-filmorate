package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exeption.UserAlreadyRegisteredException;
import ru.yandex.practicum.filmorate.exeption.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.interfaces.UserStorage;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage {
    private final Map<Integer, User> users = new HashMap<>();
    private int userId = 1;

    @Override
    public User addUser(User user) {
        setUserName(user);
        userRegisteredCheck(user);
        user.setId(userId);
        users.put(userId++, user);
        log.debug("Add user: {}", user);
        return user;
    }

    @Override
    public User updateUser(User user) {
        User existingUser = users.get(user.getId());
        if (existingUser == null) {
            throw new UserNotFoundException(String.format("User with id=%d not found", user.getId()));
        }
        userRegisteredCheck(user);
        updateUserFields(existingUser, user);
        log.debug("Update user: {}", existingUser);
        return existingUser;
    }

    @Override
    public List<User> getUsers() {
        return new ArrayList<>(users.values());
    }

    @Override
    public User getUserById(Integer userId) {
        User user = users.get(userId);
        if (user == null) {
            throw new UserNotFoundException(String.format("Пользователь с таким id=%d не найден", userId));
        }
        return user;
    }

    @Override
    public void addFriend(Integer userId, Integer friendId) {
        User user = getUserById(userId);
        User friend = getUserById(friendId);
        user.addFriendIdToList(friendId);
        friend.addFriendIdToList(userId);
    }

    @Override
    public void deleteFriend(Integer userId, Integer friendId) {
        User user = getUserById(userId);
        User friend = getUserById(friendId);
        user.deleteFriendIdFromList(friend.getId());
    }

    @Override
    public Set<User> getUserFriends(Integer userId) {
        User user = getUserById(userId);

        return user.getFriendsId().stream()
                .map(this::getUserById)
                .collect(Collectors.toSet());
    }

    @Override
    public Set<User> getCommonFriendsList(Integer userId, Integer otherId) {
        User user = getUserById(userId);
        User otherUser = getUserById(otherId);

        return user.getFriendsId().stream()
                .filter(id -> otherUser.getFriendsId().contains(id))
                .map(this::getUserById)
                .collect(Collectors.toSet());
    }

    private void setUserName(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
    }

    private void userRegisteredCheck(User user) {
        Map<Integer, User> registeredUsers = new HashMap<>(users);
        registeredUsers.remove(user.getId());
        if (registeredUsers.values().stream().anyMatch((u) -> u.getEmail().equals(user.getEmail()))) {
            log.warn("Email {} is already busy: ", user.getEmail());
            throw new UserAlreadyRegisteredException("Пользователь с таким email-адресом уже зарегистрирован");
        }
    }

    private void updateUserFields(User existingUser, User updateUser) {
        String newEmail = updateUser.getEmail();
        String newLogin = updateUser.getLogin();
        String newName = updateUser.getName();
        LocalDate newBirthday = updateUser.getBirthday();

        if (newEmail != null) {
            existingUser.setEmail(newEmail);
        }
        if (newLogin != null) {
            existingUser.setLogin(newLogin);
        }
        if (newName != null) {
            existingUser.setName(newName);
        }
        if (newBirthday != null) {
            existingUser.setBirthday(newBirthday);
        }
    }
}
