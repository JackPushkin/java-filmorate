package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.validator.ValidationMarkerInterface;

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
        User user1 = User.builder().email("").login("Jack").name("Jacky").birthday(date).build();
        User user2 = User.builder().email("hello").login("Jack").name("Jacky").birthday(date).build();
        User user3 = User.builder().email(null).login("Jack").name("Jacky").birthday(date).build();

        // Получаю список ошибок валидации
        Set<ConstraintViolation<User>> violations1 = validator.validate(user1, ValidationMarkerInterface.OnCreate.class);
        Set<ConstraintViolation<User>> violations2 = validator.validate(user2);
        Set<ConstraintViolation<User>> violations3 = validator.validate(user3, ValidationMarkerInterface.OnCreate.class);

        // Проверяю корректность вывода сообщений об ошибках
        assertEquals("must not consist only with whitespaces", getErrorMessage(violations1));
        assertEquals("must be a well-formed email address", getErrorMessage(violations2));
        assertEquals("must not be empty", getErrorMessage(violations3));
    }

    @Test
    public void userLoginValidationTest() {
        // Создаю юзеров с некорректными адресами эл. почты
        User user1 = User.builder().email("hello@mail.ru").login("").name("Jacky").birthday(date).build();
        User user2 = User.builder().email("hello@mail.ru").login("   ").name("Jacky").birthday(date).build();
        User user3 = User.builder().email("hello@mail.ru").login(null).name("Jacky").birthday(date).build();

        // Получаю список ошибок валидации
        Set<ConstraintViolation<User>> violations1 = validator.validate(user1, ValidationMarkerInterface.OnCreate.class);
        Set<ConstraintViolation<User>> violations2 = validator.validate(user2, ValidationMarkerInterface.OnCreate.class);
        Set<ConstraintViolation<User>> violations3 = validator.validate(user3, ValidationMarkerInterface.OnCreate.class);

        // Проверяю корректность вывода сообщений об ошибках
        assertEquals("must not be empty", getErrorMessage(violations1));
        assertEquals("must not be empty", getErrorMessage(violations2));
        assertEquals("must not be empty", getErrorMessage(violations3));
    }

    @Test
    public void userBirthdayDateValidationTest() {
        // Создаю юзера из будущего
        User user = User.builder().email("hello@mail.ru").login("Jack").name("Jacky").birthday(LocalDate.parse("2100-12-01")).build();

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
