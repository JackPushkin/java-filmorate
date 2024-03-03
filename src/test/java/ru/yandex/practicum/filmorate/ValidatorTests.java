package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exeption.FilmValidationException;
import ru.yandex.practicum.filmorate.exeption.UserValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.validator.Validator;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class ValidatorTests {

    private final LocalDate date = LocalDate.parse("2000-12-01");

    @Test
    public void filmNameValidationTest() {
        // Создаю фильмы с некорректным названием
        Film film1 = new Film("", "desc", date, 100);
        Film film2 = new Film("   ", "desc", date, 100);
        // Должно вылететь исключение
        assertThrows(FilmValidationException.class, () -> Validator.isFilmFormatValid(film1));
        assertThrows(FilmValidationException.class, () -> Validator.isFilmFormatValid(film2));
    }

    @Test
    public void filmDescriptionValidationTest() {
        // Создаю описание к фильму на 201 символ
        char[] array = new char[201];
        Arrays.fill(array, 'X');
        String description = new String(array);
        // Создаю фильм
        Film film = new Film("X", description, date, 100);
        // Должно вылететь исключение
        assertThrows(FilmValidationException.class, () -> Validator.isFilmFormatValid(film));
    }

    @Test
    public void filmReleaseDateValidationTest() {
        // Создаю фильм с некорректной датой выхода
        Film film = new Film("X", "Y", LocalDate.parse("1890-12-01"), 100);
        // Должно вылететь исключение
        assertThrows(FilmValidationException.class, () -> Validator.isFilmFormatValid(film));
    }

    @Test
    public void filmDurationValidationTest() {
        // Создаю фильм с некорректной датой выхода
        Film film = new Film("X", "Y", date, -50);
        // Должно вылететь исключение
        assertThrows(FilmValidationException.class, () -> Validator.isFilmFormatValid(film));
    }

    @Test
    public void filmAlreadyAddedTest() {
        // Создаю фильм
        Map<Integer, Film> films = new HashMap<>();
        Film film = new Film("X", "Y", date, 50);
        films.put(1, film);
        // Создаю такой же фильм
        Film filmCopy = new Film("X", "Y", date, 50);
        // Должно вылететь исключение
        assertThrows(FilmValidationException.class, () -> Validator.isFilmNotAdded(filmCopy, films));
    }

    @Test
    public void userEmailValidationTest() {
        // Создаю юзеров с некорректными адресами эл. почты
        User user1 = new User("", "Jack", "Jacky", date);
        User user2 = new User("   ", "Jack", "Jacky", date);
        User user3 = new User("hello", "Jack", "Jacky", date);
        // Должно вылететь исключение
        assertThrows(UserValidationException.class, () -> Validator.isUserFormatValid(user1));
        assertThrows(UserValidationException.class, () -> Validator.isUserFormatValid(user2));
        assertThrows(UserValidationException.class, () -> Validator.isUserFormatValid(user3));
    }

    @Test
    public void userLoginValidationTest() {
        // Создаю юзеров с некорректными адресами эл. почты
        User user1 = new User("hello@mail.ru", "", "Jacky", date);
        User user2 = new User("world@mail.ru", "   ", "Jacky", date);
        // Должно вылететь исключение
        assertThrows(UserValidationException.class, () -> Validator.isUserFormatValid(user1));
        assertThrows(UserValidationException.class, () -> Validator.isUserFormatValid(user2));
    }

    @Test
    public void userBirthdayDateValidationTest() {
        // Создаю юзера из будущего
        User user = new User("hello@mail.ru", "Jack", "Jacky", LocalDate.parse("2100-12-01"));
        // Должно вылететь исключение
        assertThrows(UserValidationException.class, () -> Validator.isUserFormatValid(user));
    }

    @Test
    public void emailAndLoginAlreadyUsedTest() {
        // Создаю юзера
        Map<Integer, User> users = new HashMap<>();
        User user1 = new User("hello@mail.ru", "Jack", "Jacky", LocalDate.parse("2001-12-01"));
        // Создаю юзера с таким же email
        User user2 = new User("hello@mail.ru", "John", "Johny", LocalDate.parse("2010-12-01"));
        // Создаю юзера с таким же login
        User user3 = new User("hi@mail.com", "Jack", "Johny", LocalDate.parse("2012-12-01"));
        users.put(1, user1);
        // Должно вылететь исключение
        assertThrows(UserValidationException.class, () -> Validator.isUserNotRegistered(user2, users));
        assertThrows(UserValidationException.class, () -> Validator.isLoginFree(user3, users));
    }
}
