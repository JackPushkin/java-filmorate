package ru.yandex.practicum.filmorate.validator;

import ru.yandex.practicum.filmorate.exeption.FilmValidationException;
import ru.yandex.practicum.filmorate.exeption.UserValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

public class Validator {

    public static boolean isFilmFormatValid(Film film) {
        final int maxFilmDescriptionLength = 200;
        final LocalDate minDate = LocalDate.of(1895, 12, 28);

        if (film.getName().isEmpty() ||
            film.getName().isBlank() ||
            film.getDescription().length() > maxFilmDescriptionLength ||
            film.getReleaseDate().isBefore(minDate) ||
            film.getDuration().isNegative())
        {
            throw new FilmValidationException("Incorrect film format");
        }
        return true;
    }

    public static boolean isUserFormatValid(User user) {
        String userLogin = user.getLogin();
        String userName = user.getName();
        String userEmail = user.getEmail();

        if (userEmail.isBlank() || !userEmail.contains("@") ||
            userLogin.isEmpty() || userLogin.isBlank() ||
            user.getBirthday().isAfter(LocalDate.now()))
        {
            throw new UserValidationException("Incorrect user format");
        }
        if (userName.isEmpty() || userName.isBlank()) {
            user.setName(userLogin);
        }
        return true;
    }
}
