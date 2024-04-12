package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exeption.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.interfaces.FilmStorage;
import ru.yandex.practicum.filmorate.storage.interfaces.UserStorage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Integer, Film> films = new HashMap<>();
    private Integer filmId = 1;
    private final UserStorage userStorage;

    @Autowired
    public InMemoryFilmStorage(@Qualifier("inMemoryUserStorage") UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    @Override
    public Film addFilm(Film film) {
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
            throw new NotFoundException(String.format("Film with id=%d not found", filmId));
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
            throw new NotFoundException(String.format("Film with id=%d not found", filmId));
        }
        return film;
    }

    @Override
    public List<Film> getPopularFilms(Integer count) {
        return getFilms().stream()
                .sorted((f1, f2) -> Integer.compare(f2.getLikesCount(), f1.getLikesCount()))
                .limit(count)
                .collect(Collectors.toList());
    }

    @Override
    public void addLike(Integer filmId, Integer userId) {
        User user = userStorage.getUserById(userId);
        Film film = getFilmById(filmId);
//        user.addFilmIdToList(filmId);
        Integer likesCount = film.getLikesCount();
        film.setLikesCount(++likesCount);
    }

    @Override
    public void deleteLike(Integer filmId, Integer userId) {
        User user = userStorage.getUserById(userId);
        Film film = getFilmById(filmId);
//        user.deleteFilmIdFromList(filmId);
        Integer likesCount = film.getLikesCount();
        film.setLikesCount(--likesCount);
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
