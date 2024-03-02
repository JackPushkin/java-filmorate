package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exeption.FilmValidationException;
import ru.yandex.practicum.filmorate.exeption.UserValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.validator.Validator;

import java.time.Duration;
import java.time.LocalDate;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ValidatorTests {

    private final LocalDate date = LocalDate.parse("2000-12-01");
    private final Duration duration = Duration.ofMinutes(120);

    @Test
    public void filmNameValidationTest() {
        // Создаю фильмы с некорректным названием
        Film film1 = new Film(1, "", "desc", date, duration);
        Film film2 = new Film(2, "   ", "desc", date, duration);
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
        Film film = new Film(1, "X", description, date, duration);
        // Должно вылететь исключение
        assertThrows(FilmValidationException.class, () -> Validator.isFilmFormatValid(film));
    }

    @Test
    public void filmReleaseDateValidationTest() {
        // Создаю фильм с некорректной датой выхода
        Film film = new Film(1, "X", "Y", LocalDate.parse("1890-12-01"), duration);
        // Должно вылететь исключение
        assertThrows(FilmValidationException.class, () -> Validator.isFilmFormatValid(film));
    }

    @Test
    public void filmDurationValidationTest() {
        // Создаю фильм с некорректной датой выхода
        Film film = new Film(1, "X", "Y", date, Duration.ofMinutes(-50));
        // Должно вылететь исключение
        assertThrows(FilmValidationException.class, () -> Validator.isFilmFormatValid(film));
    }

    @Test
    public void userEmailValidationTest() {
        // Создаю юзеров с некорректными адресами эл. почты
        User user1 = new User(1, "", "Jack", "Jacky", date);
        User user2 = new User(2, "   ", "Jack", "Jacky", date);
        User user3 = new User(3, "hello", "Jack", "Jacky", date);
        // Должно вылететь исключение
        assertThrows(UserValidationException.class, () -> Validator.isUserFormatValid(user1));
        assertThrows(UserValidationException.class, () -> Validator.isUserFormatValid(user2));
        assertThrows(UserValidationException.class, () -> Validator.isUserFormatValid(user3));
    }

    @Test
    public void userLoginValidationTest() {
        // Создаю юзеров с некорректными адресами эл. почты
        User user1 = new User(1, "hello@mail.ru", "", "Jacky", date);
        User user2 = new User(2, "world@mail.ru", "   ", "Jacky", date);
        // Должно вылететь исключение
        assertThrows(UserValidationException.class, () -> Validator.isUserFormatValid(user1));
        assertThrows(UserValidationException.class, () -> Validator.isUserFormatValid(user2));
    }

    @Test
    public void userBirthdayDateValidationTest() {
        // Создаю юзера из будущего
        User user = new User(1, "hello@mail.ru", "Jack", "Jacky", LocalDate.parse("2100-12-01"));
        // Должно вылететь исключение
        assertThrows(UserValidationException.class, () -> Validator.isUserFormatValid(user));
    }
}
