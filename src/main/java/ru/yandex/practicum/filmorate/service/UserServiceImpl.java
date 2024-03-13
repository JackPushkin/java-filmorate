package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.interfaces.UserService;
import ru.yandex.practicum.filmorate.storage.interfaces.UserStorage;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    private final UserStorage userStorage;

    @Autowired
    public UserServiceImpl(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    @Override
    public void addFriend(Integer userId, Integer friendId) {
        User user = userStorage.getUserById(userId);
        User friend = userStorage.getUserById(friendId);
        user.addFriendIdToList(friendId);
        friend.addFriendIdToList(userId);
    }

    @Override
    public void deleteFriend(Integer userId, Integer friendId) {
        User user = userStorage.getUserById(userId);
        User friend = userStorage.getUserById(friendId);
        user.deleteFriendIdFromList(friend.getId());
    }

    @Override
    public List<User> getCommonFriendsList(Integer userId, Integer otherId) {
        User user = userStorage.getUserById(userId);
        User otherUser = userStorage.getUserById(otherId);

        return user.getFriendsId().stream()
                .filter(id -> otherUser.getFriendsId().contains(id))
                .map(userStorage::getUserById)
                .collect(Collectors.toList());
    }

    @Override
    public User getUserById(Integer id) {
        return userStorage.getUserById(id);
    }

    @Override
    public List<User> getFriendsList(Integer userId) {
        User user = userStorage.getUserById(userId);

        return user.getFriendsId().stream()
                .map(userStorage::getUserById)
                .collect(Collectors.toList());
    }

    @Override
    public User addUser(User user) {
        return userStorage.addUser(user);
    }

    @Override
    public User updateUser(User user) {
        return userStorage.updateUser(user);
    }

    @Override
    public List<User> getUsers() {
        return userStorage.getUsers();
    }
}
