package ru.yandex.practicum.filmorate.storage.interfaces;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {

    User addUser(User user);

    User updateUser(User user);

    List<User> getUsers();

    User getUserById(Integer userId);

    void addFriend(Integer userId, Integer friendId);

    void deleteFriend(Integer userId, Integer friendId);

    List<User> getUserFriends(Integer userId);

    List<User> getCommonFriendsList(Integer userId, Integer otherId);
}
