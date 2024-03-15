package ru.yandex.practicum.filmorate.validator;

import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.exeption.FilmValidationException;
import ru.yandex.practicum.filmorate.exeption.IncorrectParameterException;
import ru.yandex.practicum.filmorate.exeption.UserValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Map;

@Slf4j
public class Validator {

//    public static void filmFormatValidation(Film film) {
//        final int maxFilmDescriptionLength = 200;
//        final LocalDate minDate = LocalDate.of(1895, 12, 28);
//        final String filmDescription = film.getDescription();
//        final LocalDate releaseDate = film.getReleaseDate();
//        final String filmName = film.getName();
//
//        if (filmName == null || filmName.isBlank()) {
//            log.warn("Not valid film name: {}", film);
//            throw new FilmValidationException("Incorrect film name");
//        }
//
//        if (filmDescription == null || filmDescription.length() > maxFilmDescriptionLength || filmDescription.isBlank()) {
//            log.warn("Not valid film description: {}", film);
//            throw new FilmValidationException("Incorrect film description");
//        }
//
//        if (releaseDate == null || releaseDate.isBefore(minDate) || releaseDate.isAfter(LocalDate.now())) {
//            log.warn("Not valid film release date: {}", film);
//            throw new FilmValidationException("Incorrect film release date");
//        }
//
//        if (film.getDuration() <= 0) {
//            log.warn("Not valid film duration: {}", film);
//            throw new FilmValidationException("Incorrect film duration");
//        }
//    }

    public static void filmAddedValidation(Film film, Map<Integer, Film> films) {
        if (films.containsValue(film)) {
            log.warn("This film already added: {} ", film);
            throw new FilmValidationException("Film with such id already exists");
        }
    }

    public static void userFormatValidation(User user) {
        String name = user.getName();
        String login = user.getLogin();

//        String email = user.getEmail();
//        LocalDate birthday = user.getBirthday();
//
//        if (email == null || email.isBlank() || !email.contains("@")) {
//            log.warn("Not valid user email: {}", user);
//            throw new UserValidationException("Incorrect user email");
//        }
//
//        if (login == null || login.isBlank()) {
//            log.warn("Not valid user login: {}", user);
//            throw new UserValidationException("Incorrect user login");
//        }
//
//        if (birthday.isAfter(LocalDate.now())) {
//            log.warn("Not valid user birthday date: {}", user);
//            throw new UserValidationException("Incorrect user birthday date");
//        }

        /////////////////////////////////////////////
        if (name == null || name.isBlank()) {
            user.setName(login);
        }
        /////////////////////////////////////////////
    }

    public static void userRegisteredValidation(User user, Map<Integer, User> users) {
        if (users.values().stream().anyMatch((u) -> u.getEmail().equals(user.getEmail()))) {
            log.warn("Email {} is already busy: ", user.getEmail());
            throw new UserValidationException("User with such email already exists");
        }
    }

//    public static void idParamValidation(String paramOne, String paramTwo, Integer...param) {
//        if (param[0] == null || param[0] <= 0) {
//            throw new IncorrectParameterException(paramOne);
//        }
//        if (param.length == 2 && (param[1] == null || param[1] <= 0)) {
//            throw new IncorrectParameterException(paramTwo);
//        }
//    }
}
