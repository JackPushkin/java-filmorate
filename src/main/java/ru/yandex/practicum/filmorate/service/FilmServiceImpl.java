package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.interfaces.FilmService;
import ru.yandex.practicum.filmorate.storage.interfaces.FilmStorage;
import ru.yandex.practicum.filmorate.storage.interfaces.UserStorage;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FilmServiceImpl implements FilmService {

    private final UserStorage userStorage;
    private final FilmStorage filmStorage;

    @Autowired
    public FilmServiceImpl(UserStorage userStorage, FilmStorage filmStorage) {
        this.userStorage = userStorage;
        this.filmStorage = filmStorage;
    }

    @Override
    public void addLike(Integer filmId, Integer userId) {
        User user = userStorage.getUserById(userId);
        Film film = filmStorage.getFilmById(filmId);
        user.addFilmIdToList(filmId);
        film.addUserIdToList(userId);
    }

    @Override
    public void deleteLike(Integer filmId, Integer userId) {
        User user = userStorage.getUserById(userId);
        Film film = filmStorage.getFilmById(filmId);
        user.deleteFilmIdFromList(filmId);
        film.deleteUserIdFromList(userId);
    }

    @Override
    public List<Film> getPopularFilmsList(Integer count) {
        return filmStorage.getFilms().stream()
                .sorted((f1, f2) -> Integer.compare(f2.getUsersId().size(), f1.getUsersId().size()))
                .limit(count)
                .collect(Collectors.toList());
    }

    @Override
    public Film getFilmById(Integer filmId) {
        return filmStorage.getFilmById(filmId);
    }

    @Override
    public Film addFilm(Film film) {
        return filmStorage.addFilm(film);
    }

    @Override
    public Film updateFilm(Film film) {
        return filmStorage.updateFilm(film);
    }

    @Override
    public List<Film> getFilms() {
        return filmStorage.getFilms();
    }
}
