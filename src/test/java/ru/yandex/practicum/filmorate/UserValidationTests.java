package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserValidationTests {
    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
    private final LocalDate date = LocalDate.parse("2000-12-01");

    @Test
    public void userEmailValidationTest() {
        // Создаю юзеров с некорректными адресами эл. почты
        User user1 = new User("", "Jack", "Jacky", date);
        User user2 = new User("hello", "Jack", "Jacky", date);
        User user3 = new User(null, "Jack", "Jacky", date);

        // Получаю список ошибок валидации
        Set<ConstraintViolation<User>> violations1 = validator.validate(user1);
        Set<ConstraintViolation<User>> violations2 = validator.validate(user2);
        Set<ConstraintViolation<User>> violations3 = validator.validate(user3);

        // Проверяю корректность вывода сообщений об ошибках
        assertEquals("must not be empty", getErrorMessage(violations1));
        assertEquals("must be a well-formed email address", getErrorMessage(violations2));
        assertEquals("must not be empty", getErrorMessage(violations3));
    }

    @Test
    public void userLoginValidationTest() {
        // Создаю юзеров с некорректными адресами эл. почты
        User user1 = new User("hello@mail.ru", "", "Jacky", date);
        User user2 = new User("world@mail.ru", "   ", "Jacky", date);
        User user3 = new User("world@mail.ru", null, "Jacky", date);

        // Получаю список ошибок валидации
        Set<ConstraintViolation<User>> violations1 = validator.validate(user1);
        Set<ConstraintViolation<User>> violations2 = validator.validate(user2);
        Set<ConstraintViolation<User>> violations3 = validator.validate(user3);

        // Проверяю корректность вывода сообщений об ошибках
        assertEquals("must not be empty", getErrorMessage(violations1));
        assertEquals("must not be empty", getErrorMessage(violations2));
        assertEquals("must not be empty", getErrorMessage(violations3));
    }

    @Test
    public void userBirthdayDateValidationTest() {
        // Создаю юзера из будущего
        User user = new User("hello@mail.ru", "Jack", "Jacky", LocalDate.parse("2100-12-01"));

        // Получаю список ошибок валидации
        Set<ConstraintViolation<User>> violations = validator.validate(user);

        // Проверяю корректность вывода сообщений об ошибках
        assertEquals("must be a past date", getErrorMessage(violations));
    }

    private String getErrorMessage(Set<ConstraintViolation<User>> violations) {
        return violations.stream()
                .map(ConstraintViolation::getMessage)
                .findFirst()
                .orElse("validation success");
    }
}
