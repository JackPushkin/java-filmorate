package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.interfaces.UserService;
import ru.yandex.practicum.filmorate.storage.interfaces.UserStorage;

import java.time.LocalDate;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    private final UserStorage userStorage;

    @Autowired
    public UserServiceImpl(@Qualifier(value = "userDbStorage") UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    @Override
    public void addFriend(Integer userId, Integer friendId) {
        userStorage.addFriend(userId, friendId);
    }

    @Override
    public void deleteFriend(Integer userId, Integer friendId) {
        userStorage.deleteFriend(userId, friendId);
    }

    @Override
    public List<User> getCommonFriendsList(Integer userId, Integer otherId) {
        return userStorage.getCommonFriendsList(userId, otherId);
    }

    @Override
    public User getUserById(Integer id) {
        return userStorage.getUserById(id);
    }

    @Override
    public List<User> getFriendsList(Integer userId) {
        return userStorage.getUserFriends(userId);
    }

    @Override
    public User addUser(User user) {
        String userName = user.getName();
        if (userName == null || userName.isBlank()) {
            user.setName(user.getLogin());
        }
        return userStorage.addUser(user);
    }

    @Override
    public User updateUser(User user) {
        String email = user.getEmail();
        String login = user.getLogin();
        String name = user.getName();
        LocalDate birthday = user.getBirthday();

        User selectedUser = userStorage.getUserById(user.getId());

        if (email != null) selectedUser.setEmail(email);
        if (login != null) selectedUser.setLogin(login);
        if (name != null) selectedUser.setName(name);
        if (birthday != null) selectedUser.setBirthday(birthday);

        return userStorage.updateUser(selectedUser);
    }

    @Override
    public List<User> getUsers() {
        return userStorage.getUsers();
    }
}
