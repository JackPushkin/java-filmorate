package ru.yandex.practicum.filmorate.service.interfaces;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmService {

    Film addFilm(Film film);

    Film updateFilm(Film film);

    List<Film> getFilms();

    void addLike(Integer filmId, Integer userId);

    void deleteLike(Integer filmId, Integer userId);

    List<Film> getPopularFilmsList(Integer count);

    Film getFilmById(Integer filmId);
}
