package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FilmValidationTests {
    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
    private final LocalDate date = LocalDate.parse("2000-12-01");

    @Test
    public void filmNameValidationTest() {
        // Создаю фильмы с некорректным названием
        Film film1 = new Film("", "Y", date, 100);
        Film film2 = new Film("   ", "Y", date, 100);
        Film film3 = new Film(null, "Y", date, 100);

        // Получаю список ошибок валидации
        Set<ConstraintViolation<Film>> violations1 = validator.validate(film1);
        Set<ConstraintViolation<Film>> violations2 = validator.validate(film1);
        Set<ConstraintViolation<Film>> violations3 = validator.validate(film1);

        // Проверяю корректность вывода сообщений об ошибках
        assertEquals("не должно быть пустым", getErrorMessage(violations1));
        assertEquals("не должно быть пустым", getErrorMessage(violations2));
        assertEquals("не должно быть пустым", getErrorMessage(violations3));
    }

    @Test
    public void filmDescriptionValidationTest() {
        char[] array = new char[205];
        Arrays.fill(array, 'X');
        String tooLongDescription = new String(array);

        // Создаю фильмы с некорректным описанием
        Film film1 = new Film("X", tooLongDescription, date, 100);
        Film film2 = new Film("X", "", date, 100);
        Film film3 = new Film("X", "   ", date, 100);
        Film film4 = new Film("X", null, date, 100);

        // Получаю список ошибок валидации
        Set<ConstraintViolation<Film>> violations1 = validator.validate(film1);
        Set<ConstraintViolation<Film>> violations2 = validator.validate(film2);
        Set<ConstraintViolation<Film>> violations3 = validator.validate(film3);
        Set<ConstraintViolation<Film>> violations4 = validator.validate(film4);

        // Проверяю корректность вывода сообщений об ошибках
        assertEquals("размер должен находиться в диапазоне от 0 до 200", getErrorMessage(violations1));
        assertEquals("не должно быть пустым", getErrorMessage(violations2));
        assertEquals("не должно быть пустым", getErrorMessage(violations3));
        assertEquals("не должно быть пустым", getErrorMessage(violations4));
    }

    @Test
    public void filmDurationValidationTest() {
        int negativeDuration = -50;
        int zeroDuration = 0;

        // Создаю фильмы с некорректной продолжительностью
        Film film1 = new Film("X", "Y", date, negativeDuration);
        Film film2 = new Film("X", "Y", date, zeroDuration);

        // Получаю список ошибок валидации
        Set<ConstraintViolation<Film>> violations1 = validator.validate(film1);
        Set<ConstraintViolation<Film>> violations2 = validator.validate(film2);

        // Проверяю корректность вывода сообщений об ошибках
        assertEquals("должно быть больше 0", getErrorMessage(violations1));
        assertEquals("должно быть больше 0", getErrorMessage(violations2));
    }

    @Test
    public void filmReleaseDateValidationTest() {
        // Создаю фильмы с некорректной датой
        Film film1 = new Film("X", "Y", LocalDate.parse("1890-12-01"), 100);
        Film film2 = new Film("X", "Y", LocalDate.parse("2890-12-01"), 100);
        Film film3 = new Film("X", "Y", null, 100);

        // Получаю список ошибок валидации
        Set<ConstraintViolation<Film>> violations1 = validator.validate(film1);
        Set<ConstraintViolation<Film>> violations2 = validator.validate(film2);
        Set<ConstraintViolation<Film>> violations3 = validator.validate(film3);

        // Проверяю корректность вывода сообщений об ошибках
        assertEquals("не должна быть null или до 1895-12-28", getErrorMessage(violations1));
        assertEquals("должно содержать прошедшую дату", getErrorMessage(violations2));
        assertEquals("не должна быть null или до 1895-12-28", getErrorMessage(violations3));
    }

    private String getErrorMessage(Set<ConstraintViolation<Film>> violations) {
        return violations.stream()
                .map(ConstraintViolation::getMessage)
                .findFirst()
                .orElse("валидация прошла успешно");
    }
}
