package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.MpaRating;
import ru.yandex.practicum.filmorate.validator.ValidationMarkerInterface;

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
    private final MpaRating mpaRating = MpaRating.builder().id(1).build();

    @Test
    public void filmNameValidationTest() {
        // Создаю фильмы с некорректным названием
        Film film1 = Film.builder().name("").description("Y").releaseDate(date).duration(100).mpa(mpaRating).build();
        Film film2 = Film.builder().name("   ").description("Y").releaseDate(date).duration(100).mpa(mpaRating).build();
        Film film3 = Film.builder().name(null).description("Y").releaseDate(date).duration(100).mpa(mpaRating).build();

        // Получаю список ошибок валидации
        Set<ConstraintViolation<Film>> violations1 = validator.validate(film1, ValidationMarkerInterface.OnCreate.class);
        Set<ConstraintViolation<Film>> violations2 = validator.validate(film2, ValidationMarkerInterface.OnCreate.class);
        Set<ConstraintViolation<Film>> violations3 = validator.validate(film3, ValidationMarkerInterface.OnCreate.class);

        // Проверяю корректность вывода сообщений об ошибках
        assertEquals("must not be empty", getErrorMessage(violations1));
        assertEquals("must not be empty", getErrorMessage(violations2));
        assertEquals("must not be empty", getErrorMessage(violations3));
    }

    @Test
    public void filmDescriptionValidationTest() {
        char[] array = new char[205];
        Arrays.fill(array, 'X');
        String tooLongDescription = new String(array);

        // Создаю фильмы с некорректным описанием
        Film film1 = Film.builder().name("X").description(tooLongDescription).releaseDate(date).duration(100).mpa(mpaRating).build();
        Film film2 = Film.builder().name("X").description("").releaseDate(date).duration(100).mpa(mpaRating).build();
        Film film3 = Film.builder().name("X").description("   ").releaseDate(date).duration(100).mpa(mpaRating).build();
        Film film4 = Film.builder().name("X").description(null).releaseDate(date).duration(100).mpa(mpaRating).build();

        // Получаю список ошибок валидации
        Set<ConstraintViolation<Film>> violations1 = validator.validate(film1);
        Set<ConstraintViolation<Film>> violations2 = validator.validate(film2, ValidationMarkerInterface.OnCreate.class);
        Set<ConstraintViolation<Film>> violations3 = validator.validate(film3, ValidationMarkerInterface.OnCreate.class);
        Set<ConstraintViolation<Film>> violations4 = validator.validate(film4, ValidationMarkerInterface.OnCreate.class);

        // Проверяю корректность вывода сообщений об ошибках
        assertEquals("size must be between 0 and 200", getErrorMessage(violations1));
        assertEquals("must not be empty", getErrorMessage(violations2));
        assertEquals("must not be empty", getErrorMessage(violations3));
        assertEquals("must not be empty", getErrorMessage(violations4));
    }

    @Test
    public void filmDurationValidationTest() {
        int negativeDuration = -50;
        int zeroDuration = 0;

        // Создаю фильмы с некорректной продолжительностью
        Film film1 = Film.builder().name("X").description("Y").releaseDate(date).duration(negativeDuration).build();
        Film film2 = Film.builder().name("X").description("Y").releaseDate(date).duration(zeroDuration).build();

        // Получаю список ошибок валидации
        Set<ConstraintViolation<Film>> violations1 = validator.validate(film1);
        Set<ConstraintViolation<Film>> violations2 = validator.validate(film2);

        // Проверяю корректность вывода сообщений об ошибках
        assertEquals("must be greater than 0", getErrorMessage(violations1));
        assertEquals("must be greater than 0", getErrorMessage(violations2));
    }

    @Test
    public void filmReleaseDateValidationTest() {
        // Создаю фильмы с некорректной датой
        Film film1 = Film.builder().name("X").description("Y").releaseDate(LocalDate.parse("1890-12-01")).duration(100).build();
        Film film2 = Film.builder().name("X").description("Y").releaseDate(LocalDate.parse("2890-12-01")).duration(100).build();
        Film film3 = Film.builder().name("X").description("Y").releaseDate(null).duration(100).build();

        // Получаю список ошибок валидации
        Set<ConstraintViolation<Film>> violations1 = validator.validate(film1);
        Set<ConstraintViolation<Film>> violations2 = validator.validate(film2);
        Set<ConstraintViolation<Film>> violations3 = validator.validate(film3, ValidationMarkerInterface.OnCreate.class);

        // Проверяю корректность вывода сообщений об ошибках
        assertEquals("must not be before 1895-12-28 or in the future", getErrorMessage(violations1));
        assertEquals("must not be before 1895-12-28 or in the future", getErrorMessage(violations2));
        assertEquals("must not be null", getErrorMessage(violations3));
    }

    private String getErrorMessage(Set<ConstraintViolation<Film>> violations) {
        return violations.stream()
                .map(ConstraintViolation::getMessage)
                .findFirst()
                .orElse("validation success");
    }
}
