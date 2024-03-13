package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
public class User {
    private int id;
    private String email;
    private String login;
    private String name;
    private LocalDate birthday;
    private final Set<Integer> friendsId = new HashSet<>();
    private final Set<Integer> likedFilmsId = new HashSet<>();

    public User(String email, String login, String name, LocalDate birthday) {
        this.email = email;
        this.login = login;
        this.name = name;
        this.birthday = birthday;
    }

    public void addFilmIdToList(Integer filmId) {
        likedFilmsId.add(filmId);
    }

    public void addFriendIdToList(Integer friendId) {
        friendsId.add(friendId);
    }

    public void deleteFriendIdFromList(Integer friendId) {
        friendsId.remove(friendId);
    }

    public void deleteFilmIdFromList(Integer filmId) {
        likedFilmsId.remove(filmId);
    }
}
