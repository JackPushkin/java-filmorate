package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exeption.FilmValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.validator.Validator;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class FilmValidationTests {

    private final LocalDate date = LocalDate.parse("2000-12-01");

    @Test
    public void filmNameValidationTest() {
        // Создаю фильмы с некорректным названием
        Film film1 = new Film("", "desc", date, 100);
        Film film2 = new Film("   ", "desc", date, 100);
        // Создаю фильм с незаданными именем
        Film film3 = new Film(null, "desc", date, 100);
        // Должно вылететь исключение
        assertThrows(FilmValidationException.class, () -> Validator.filmFormatValidation(film1));
        assertThrows(FilmValidationException.class, () -> Validator.filmFormatValidation(film2));
        assertThrows(FilmValidationException.class, () -> Validator.filmFormatValidation(film3));
    }

    @Test
    public void filmDescriptionValidationTest() {
        // Создаю слишком длинное описание к фильму
        char[] array = new char[201];
        Arrays.fill(array, 'X');
        String description = new String(array);
        // Создаю фильм. Ожидаем, что вылетит исключение
        Film film = new Film("X", description, date, 100);
        assertThrows(FilmValidationException.class, () -> Validator.filmFormatValidation(film));
        // Добавляю пустое описание. Ожидаем, что вылетит исключение
        film.setDescription("");
        assertThrows(FilmValidationException.class, () -> Validator.filmFormatValidation(film));
        // Установил описание в null. Ожидаем, что вылетит исключение
        film.setDescription(null);
        assertThrows(FilmValidationException.class, () -> Validator.filmFormatValidation(film));
    }

    @Test
    public void filmReleaseDateValidationTest() {
        // Создаю фильмы с некорректной датой выхода
        Film film1 = new Film("X", "Y", LocalDate.parse("1890-12-01"), 100);
        Film film2 = new Film("X", "Y", LocalDate.parse("2890-12-01"), 100);
        Film film3 = new Film("X", "Y", null, 100);
        // Должно вылететь исключение
        assertThrows(FilmValidationException.class, () -> Validator.filmFormatValidation(film1));
        assertThrows(FilmValidationException.class, () -> Validator.filmFormatValidation(film2));
        assertThrows(FilmValidationException.class, () -> Validator.filmFormatValidation(film3));
    }

    @Test
    public void filmDurationValidationTest() {
        // Создаю фильм с некорректной продолжительностью
        Film film = new Film("X", "Y", date, -50);
        // Должно вылететь исключение
        assertThrows(FilmValidationException.class, () -> Validator.filmFormatValidation(film));
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
        assertThrows(FilmValidationException.class, () -> Validator.filmAddedValidation(filmCopy, films));
    }
}
