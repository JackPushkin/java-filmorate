package ru.yandex.practicum.filmorate.storage_test;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.MpaRating;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.UserDbStorage;
import ru.yandex.practicum.filmorate.storage.interfaces.FilmStorage;
import ru.yandex.practicum.filmorate.storage.interfaces.UserStorage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@JdbcTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class FilmStorageTest {

    private FilmStorage filmStorage;
    private UserStorage userStorage;
    private final JdbcTemplate jdbcTemplate;

    private final LocalDate date = LocalDate.parse("1990-01-01");
    private final MpaRating rating = MpaRating.builder().id(1).name("G").build();

    @BeforeEach
    public void storageInit() {
        filmStorage = new FilmDbStorage(jdbcTemplate);
        userStorage = new UserDbStorage(jdbcTemplate);
    }

    @Test
    public void addAndFindFilmByIdTest() {

        // Создаю фильм
        Film film = Film.builder().name("Film").description("Description").releaseDate(date).mpa(rating)
                .likesCount(0).genres(new HashSet<>()).build();

        // Добавляю фильм в БД
        filmStorage.addFilm(film);

        // Получаю фильм из БД по id
        Film addedFilm = filmStorage.getFilmById(film.getId());

        // Проверяю полученные из БД данные
        assertThat(addedFilm)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(film);
    }

    @Test
    public void getAllFilmsAndUpdateFilmTest() {

        // Создаю фильм
        Film film = Film.builder().name("Film").description("Description").releaseDate(date).mpa(rating)
                .likesCount(0).genres(new HashSet<>()).build();

        // Добавляю фильм в БД
        Film addedFilm = filmStorage.addFilm(film);

        // Получаю список фильмов из БД
        List<Film> films1 = filmStorage.getFilms();

        // Список содержит 1 элемент. Элемент users1.get(0) == addedUser
        assertEquals(1, films1.size());
        assertEquals(films1.get(0), addedFilm);

        // Обновляю пользователя
        addedFilm.setName("newTitle");
        addedFilm.setDescription("newDescription");
        filmStorage.updateFilm(addedFilm);

        // Получаю список с обновленным пользователем из БД
        List<Film> films2 = filmStorage.getFilms();

        // Список по-прежнему содержит 1 элемент. Элемент users2.get(0) == addedUser
        assertEquals(1, films2.size());
        assertEquals(films2.get(0), addedFilm);
    }

    @Test
    public void addDeleteLikesAndGetPopularFilmsTest() {

        // Создаю пользователей
        User user1 = User.builder().email("jack1@mail.ru").login("jack1").name("Jack1").birthday(date)
                .likedFilmsId(new ArrayList<>()).friendsId(new HashSet<>()).build();
        User user2 = User.builder().email("jack2@mail.ru").login("jack2").name("Jack2").birthday(date)
                .likedFilmsId(new ArrayList<>()).friendsId(new HashSet<>()).build();
        User user3 = User.builder().email("jack3@mail.ru").login("jack3").name("Jack3").birthday(date)
                .likedFilmsId(new ArrayList<>()).friendsId(new HashSet<>()).build();

        // Создаю фильм
        Film film1 = Film.builder().name("Film1").description("Description1").releaseDate(date).mpa(rating)
                .likesCount(0).genres(new HashSet<>()).build();
        Film film2 = Film.builder().name("Film2").description("Description2").releaseDate(date).mpa(rating)
                .likesCount(0).genres(new HashSet<>()).build();
        Film film3 = Film.builder().name("Film3").description("Description3").releaseDate(date).mpa(rating)
                .likesCount(0).genres(new HashSet<>()).build();

        // Добавляю пользователей и фильмы в БД
        User addedUser1 = userStorage.addUser(user1);
        User addedUser2 = userStorage.addUser(user2);
        User addedUser3 = userStorage.addUser(user3);
        Film addedFilm1 = filmStorage.addFilm(film1);
        Film addedFilm2 = filmStorage.addFilm(film2);
        Film addedFilm3 = filmStorage.addFilm(film3);

        // Ставлю фильмам лайки (film1 - 2 лайка, film3 - 1 лайк, film2 - 3 лайка)
        filmStorage.addLike(addedFilm1.getId(), addedUser1.getId());
        filmStorage.addLike(addedFilm1.getId(), addedUser2.getId());
        filmStorage.addLike(addedFilm3.getId(), addedUser1.getId());
        filmStorage.addLike(addedFilm2.getId(), addedUser1.getId());
        filmStorage.addLike(addedFilm2.getId(), addedUser2.getId());
        filmStorage.addLike(addedFilm2.getId(), addedUser3.getId());

        // Проверяю количество лайков у каждого фильма
        int film1CountOfLikes = filmStorage.getFilmById(addedFilm1.getId()).getLikesCount();
        int film2CountOfLikes = filmStorage.getFilmById(addedFilm2.getId()).getLikesCount();
        int film3CountOfLikes = filmStorage.getFilmById(addedFilm3.getId()).getLikesCount();
        assertEquals(2, film1CountOfLikes);
        assertEquals(3, film2CountOfLikes);
        assertEquals(1, film3CountOfLikes);

        // Получаю список популярных фильмов (отсортированы по количеству лайков)
        List<Film> popularFilms = filmStorage.getPopularFilms(3);

        // Проверяю полученный результат
        assertEquals(3, popularFilms.size());
        assertEquals(3, popularFilms.get(0).getLikesCount());
        assertEquals(2, popularFilms.get(1).getLikesCount());
        assertEquals(1, popularFilms.get(2).getLikesCount());
    }
}
