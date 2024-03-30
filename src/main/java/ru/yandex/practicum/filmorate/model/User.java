package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import ru.yandex.practicum.filmorate.validator.ValidationMarkerInterface;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
public class User {
    private int id;

    @Email(message = "must be a well-formed email address")
    @NotNull(message = "must not be empty",
            groups = {ValidationMarkerInterface.OnCreate.class})
    @Pattern(regexp = ".*[^ ].*",
            message = "must not consist only with whitespaces",
            groups = {ValidationMarkerInterface.OnCreate.class, ValidationMarkerInterface.OnUpdate.class})
    private String email;

    @NotBlank(message = "must not be empty",
            groups = {ValidationMarkerInterface.OnCreate.class})
    @Pattern(regexp = ".*[^ ].*",
            message = "must not be empty",
            groups = {ValidationMarkerInterface.OnUpdate.class})
    private String login;

    @Pattern(regexp = ".*[^ ].*",
            message = "must not consist only with whitespaces",
            groups = {ValidationMarkerInterface.OnUpdate.class})
    private String name;

    @Past(message = "must be a past date")
    @NotNull(message = "must not be null",
            groups = {ValidationMarkerInterface.OnCreate.class})
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
