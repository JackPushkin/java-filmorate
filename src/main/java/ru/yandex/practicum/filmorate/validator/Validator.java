package ru.yandex.practicum.filmorate.validator;

import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.exeption.FilmValidationException;
import ru.yandex.practicum.filmorate.exeption.UserValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Map;

@Slf4j
public class Validator {

    public static boolean isFilmFormatValid(Film film) {
        final int maxFilmDescriptionLength = 200;
        final LocalDate minDate = LocalDate.of(1895, 12, 28);

        if (film.getName().isEmpty() ||
            film.getName().isBlank() ||
            film.getDescription().length() > maxFilmDescriptionLength ||
            film.getReleaseDate().isBefore(minDate) ||
            film.getDuration() < 0) {
            log.warn("Not valid data: {}", film);
            throw new FilmValidationException("Incorrect film format");
        }
        return true;
    }

    public static boolean isFilmNotAdded(Film film, Map<Integer, Film> films) {
        if (films.containsValue(film)) {
            log.warn("This film already added: {} ", film);
            return false;
        }
        return true;
    }

    public static boolean isUserFormatValid(User user) {
        String userLogin = user.getLogin();
        String userName = user.getName();
        String userEmail = user.getEmail();

        if (userEmail.isBlank() || !userEmail.contains("@") ||
            userLogin.isEmpty() || userLogin.isBlank() ||
            user.getBirthday().isAfter(LocalDate.now())) {
            log.warn("Not valid data: {}", user);
            throw new UserValidationException("Incorrect user format");
        }
        if (userName == null || userName.isEmpty() || userName.isBlank()) {
            user.setName(userLogin);
        }
        return true;
    }

    public static boolean isUserNotRegistered(User user, Map<Integer, User> users) {
        if (users.values().stream().anyMatch((u) -> u.getEmail().equals(user.getEmail()))) {
            log.warn("Email {} is already busy: ", user.getEmail());
            return false;
        }
        return true;
    }

//    public static boolean isLoginFree(User user, Map<Integer, User> users) {
//        if (users.values().stream().anyMatch((u) -> u.getLogin().equals(user.getLogin()))) {
//            log.warn("Login {} is already busy: ", user.getLogin());
//            return false;
//        }
//        return true;
//    }
}
