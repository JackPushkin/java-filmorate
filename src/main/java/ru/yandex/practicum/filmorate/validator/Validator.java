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

    public static void filmFormatValidation(Film film) {
        final int maxFilmDescriptionLength = 200;
        final LocalDate minDate = LocalDate.of(1895, 12, 28);
        final String filmDescription = film.getDescription();
        final LocalDate releaseDate = film.getReleaseDate();

        if (film.getName() == null || film.getDescription() == null || film.getReleaseDate() == null) {
            log.warn("Not valid data: {}", film);
            throw new FilmValidationException("Incorrect film format");
        }

        if (film.getName().isBlank() ||
            filmDescription.length() > maxFilmDescriptionLength ||
            filmDescription.isBlank() ||
            releaseDate.isBefore(minDate) ||
            releaseDate.isAfter(LocalDate.now()) ||
            film.getDuration() <= 0) {
            log.warn("Not valid data: {}", film);
            throw new FilmValidationException("Incorrect film format");
        }
    }

    public static void filmAddedValidation(Film film, Map<Integer, Film> films) {
        if (films.containsValue(film)) {
            log.warn("This film already added: {} ", film);
            throw new FilmValidationException("Film with such id already exists");
        }
    }

    public static void userFormatValidation(User user) {
        String userLogin = user.getLogin();
        String userName = user.getName();
        String userEmail = user.getEmail();

        if (userEmail == null || userLogin == null) {
            log.warn("Not valid data: {}", user);
            throw new UserValidationException("Incorrect user format");
        }

        if (userEmail.isBlank() || !userEmail.contains("@") || userLogin.isBlank() ||
            user.getBirthday().isAfter(LocalDate.now())) {

            log.warn("Not valid data: {}", user);
            throw new UserValidationException("Incorrect user format");
        }
        if (userName == null || userName.isBlank()) {
            user.setName(userLogin);
        }
    }

    public static void userRegisteredValidation(User user, Map<Integer, User> users) {
        if (users.values().stream().anyMatch((u) -> u.getEmail().equals(user.getEmail()))) {
            log.warn("Email {} is already busy: ", user.getEmail());
            throw new UserValidationException("User with such email already exists");
        }
    }
}
