package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exeption.UserValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.validator.Validator;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class UserValidationTests {

    private final LocalDate date = LocalDate.parse("2000-12-01");

    @Test
    public void userEmailValidationTest() {
        // Создаю юзеров с некорректными адресами эл. почты
        User user1 = new User("", "Jack", "Jacky", date);
        User user2 = new User("   ", "Jack", "Jacky", date);
        User user3 = new User("hello", "Jack", "Jacky", date);
        User user4 = new User(null, "Jack", "Jacky", date);
        // Должно вылететь исключение
        assertThrows(UserValidationException.class, () -> Validator.userFormatValidation(user1),
                "Incorrect user email");
        assertThrows(UserValidationException.class, () -> Validator.userFormatValidation(user2),
                "Incorrect user email");
        assertThrows(UserValidationException.class, () -> Validator.userFormatValidation(user3),
                "Incorrect user email");
        assertThrows(UserValidationException.class, () -> Validator.userFormatValidation(user4),
                "Incorrect user email");
    }

    @Test
    public void userLoginValidationTest() {
        // Создаю юзеров с некорректными адресами эл. почты
        User user1 = new User("hello@mail.ru", "", "Jacky", date);
        User user2 = new User("world@mail.ru", "   ", "Jacky", date);
        User user3 = new User("world@mail.ru", null, "Jacky", date);
        // Должно вылететь исключение
        assertThrows(UserValidationException.class, () -> Validator.userFormatValidation(user1),
                "Incorrect user login");
        assertThrows(UserValidationException.class, () -> Validator.userFormatValidation(user2),
                "Incorrect user login");
        assertThrows(UserValidationException.class, () -> Validator.userFormatValidation(user3),
                "Incorrect user login");
    }

    @Test
    public void userBirthdayDateValidationTest() {
        // Создаю юзера из будущего
        User user = new User("hello@mail.ru", "Jack", "Jacky", LocalDate.parse("2100-12-01"));
        // Должно вылететь исключение
        assertThrows(UserValidationException.class, () -> Validator.userFormatValidation(user),
                "Incorrect user birthday date");
    }

    @Test
    public void emailAlreadyUsedTest() {
        // Создаю юзера
        Map<Integer, User> users = new HashMap<>();
        User user1 = new User("hello@mail.ru", "Jack", "Jacky", LocalDate.parse("2001-12-01"));
        // Создаю юзера с таким же email
        User user2 = new User("hello@mail.ru", "John", "Johny", LocalDate.parse("2010-12-01"));
        users.put(1, user1);
        // Должно вылететь исключение
        assertThrows(UserValidationException.class, () -> Validator.userRegisteredValidation(user2, users),
                "User with such email already exists");
    }
}
