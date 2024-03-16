package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exeption.FilmAlreadyAddedException;
import ru.yandex.practicum.filmorate.exeption.FilmNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.interfaces.FilmStorage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Integer, Film> films = new HashMap<>();
    private Integer filmId = 1;

    @Override
    public Film addFilm(Film film) {
        filmAddedCheck(film);
        Integer id = getAndIncreaseId();
        film.setId(id);
        films.put(id, film);
        log.debug("Add film: {}", film);
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        if (!films.containsKey(film.getId())) {
            log.warn("incorrect id: {}", film.getId());
            throw new FilmNotFoundException(String.format("Film with id=%d not found", filmId));
        }
        filmAddedCheck(film);
        films.put(film.getId(), film);
        log.debug("Update film: {}", film);
        return film;
    }

    @Override
    public List<Film> getFilms() {
        return new ArrayList<>(films.values());
    }

    @Override
    public Film getFilmById(Integer filmId) {
        Film film = films.get(filmId);
        if (film == null) {
            throw new FilmNotFoundException(String.format("Film with id=%d not found", filmId));
        }
        return film;
    }

    private Integer getAndIncreaseId() {
        return filmId++;
    }

    private void filmAddedCheck(Film film) {
        if (films.containsValue(film)) {
            log.warn("This film already added: {} ", film);
            throw new FilmAlreadyAddedException("Film with such id already exists");
        }
    }
}
