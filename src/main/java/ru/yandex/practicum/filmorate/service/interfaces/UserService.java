package ru.yandex.practicum.filmorate.service.interfaces;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserService {

    User addUser(User user);

    User updateUser(User user);

    List<User> getUsers();

    void addFriend(Integer userId, Integer friendId);

    void deleteFriend(Integer userId, Integer friendId);

    List<User> getCommonFriendsList(Integer id, Integer friendId);

    User getUserById(Integer id);

    List<User> getFriendsList(Integer userId);
}
