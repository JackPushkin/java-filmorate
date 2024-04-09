package ru.yandex.practicum.filmorate.service.interfaces;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Set;

public interface UserService {

    User addUser(User user);

    User updateUser(User user);

    List<User> getUsers();

    void addFriend(Integer userId, Integer friendId);

    void deleteFriend(Integer userId, Integer friendId);

    Set<User> getCommonFriendsList(Integer id, Integer friendId);

    User getUserById(Integer id);

    Set<User> getFriendsList(Integer userId);
}
