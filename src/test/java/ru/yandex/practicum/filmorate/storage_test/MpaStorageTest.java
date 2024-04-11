package ru.yandex.practicum.filmorate.storage_test;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.model.MpaRating;
import ru.yandex.practicum.filmorate.storage.MpaStorageImpl;
import ru.yandex.practicum.filmorate.storage.interfaces.MpaStorage;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@JdbcTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class MpaStorageTest {

    private MpaStorage mpaStorage;
    private final JdbcTemplate jdbcTemplate;

    @BeforeEach()
    public void init() {
        mpaStorage = new MpaStorageImpl(jdbcTemplate);
    }

    @Test
    public void getMpaRatingByIdTest() {

        // Получаю рейтинг из БД
        MpaRating rating = mpaStorage.getMpaRatingById(3);

        // Проверяю полученный результат
        assertEquals(3, rating.getId());
        assertEquals("PG-13", rating.getName());
    }

    @Test
    public void getAllMpaRatingsTest() {

        // Получаю список рейтингов
        List<MpaRating> ratings = mpaStorage.getAllMpaRatings();

        // Проверяю полученный результат
        assertEquals(5, ratings.size());
        assertEquals("G", ratings.get(0).getName());
        assertEquals("PG", ratings.get(1).getName());
        assertEquals("PG-13", ratings.get(2).getName());
        assertEquals("R", ratings.get(3).getName());
        assertEquals("NC-17", ratings.get(4).getName());
    }
}