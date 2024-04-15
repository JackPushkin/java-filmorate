package ru.yandex.practicum.filmorate.storage_test;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenreStorageImpl;
import ru.yandex.practicum.filmorate.storage.interfaces.GenreStorage;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@JdbcTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class GenreStorageTest {

    private GenreStorage genreStorage;
    private final JdbcTemplate jdbcTemplate;

    @BeforeEach()
    public void init() {
        genreStorage = new GenreStorageImpl(jdbcTemplate);
    }

    @Test
    public void getGenreByIdTest() {

        // Получаю жанр из БД по id
        Genre genre = genreStorage.getGenreById(3);

        // Проверяю полученный результат
        assertEquals(3, genre.getId());
        assertEquals("Мультфильм", genre.getName());
    }

    @Test
    public void getAllGenresTest() {

        // Получаю список жанров
        List<Genre> genres = genreStorage.getAllGenres();

        // Проверяю полученный результат
        assertEquals(6, genres.size());
        assertEquals("Комедия", genres.get(0).getName());
        assertEquals("Драма", genres.get(1).getName());
        assertEquals("Мультфильм", genres.get(2).getName());
        assertEquals("Триллер", genres.get(3).getName());
        assertEquals("Документальный", genres.get(4).getName());
        assertEquals("Боевик", genres.get(5).getName());
    }
}
