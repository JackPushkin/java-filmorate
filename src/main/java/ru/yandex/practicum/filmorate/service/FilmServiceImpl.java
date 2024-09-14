package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MpaRating;
import ru.yandex.practicum.filmorate.service.interfaces.FilmService;
import ru.yandex.practicum.filmorate.storage.interfaces.FilmStorage;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Service
public class FilmServiceImpl implements FilmService {

    private final FilmStorage filmStorage;

    @Autowired
    public FilmServiceImpl(@Qualifier(value = "filmDbStorage") FilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

    @Override
    public void addLike(Integer filmId, Integer userId) {
        filmStorage.addLike(filmId, userId);
    }

    @Override
    public void deleteLike(Integer filmId, Integer userId) {
        filmStorage.deleteLike(filmId, userId);
    }

    @Override
    public List<Film> getPopularFilmsList(Integer count) {
        return filmStorage.getPopularFilms(count);
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
        String title = film.getName();
        String description = film.getDescription();
        LocalDate releaseDate = film.getReleaseDate();
        Integer duration = film.getDuration();
        MpaRating rating = film.getMpa();
        Set<Genre> genres = film.getGenres();

        Film selectedFilm = filmStorage.getFilmById(film.getId());

        if (title != null) selectedFilm.setName(title);
        if (description != null) selectedFilm.setDescription(description);
        if (releaseDate != null) selectedFilm.setReleaseDate(releaseDate);
        if (duration != null) selectedFilm.setDuration(duration);
        if (rating != null) selectedFilm.setMpa(rating);
        if (genres != null) selectedFilm.setGenres(genres);

        return filmStorage.updateFilm(selectedFilm);
    }

    @Override
    public List<Film> getFilms() {
        return filmStorage.getFilms();
    }
}
