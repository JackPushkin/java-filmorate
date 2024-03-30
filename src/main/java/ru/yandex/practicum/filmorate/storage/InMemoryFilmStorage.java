package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exeption.FilmNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.interfaces.FilmStorage;

import java.time.LocalDate;
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
//        filmAddedCheck(film);
        Integer id = getAndIncreaseId();
        film.setId(id);
        films.put(id, film);
        log.debug("Add film: {}", film);
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        Film existingFilm = films.get(film.getId());
        if (existingFilm == null) {
            throw new FilmNotFoundException(String.format("Film with id=%d not found", filmId));
        }
        updateFilmFields(existingFilm, film);
        log.debug("Update film: {}", film);
        return existingFilm;
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

    private void updateFilmFields(Film existingFilm, Film updateFilm) {
        String newName = updateFilm.getName();
        String newDescription = updateFilm.getDescription();
        LocalDate newReleaseDate = updateFilm.getReleaseDate();
        int newDuration = updateFilm.getDuration();

        if (newName != null) {
            existingFilm.setName(newName);
        }
        if (newDescription != null) {
            existingFilm.setDescription(newDescription);
        }
        if (newReleaseDate != null) {
            existingFilm.setReleaseDate(newReleaseDate);
        }
        if (newDuration != 0) {
            existingFilm.setDuration(newDuration);
        }
    }
}
