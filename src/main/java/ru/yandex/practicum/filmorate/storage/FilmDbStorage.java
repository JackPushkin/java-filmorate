package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exeption.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MpaRating;
import ru.yandex.practicum.filmorate.storage.interfaces.FilmStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Slf4j
@Repository
public class FilmDbStorage implements FilmStorage {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;
    private final NamedParameterJdbcTemplate parameterJdbcTemplate;

    @Autowired
    public FilmDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("films")
                .usingGeneratedKeyColumns("id_film");
        parameterJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);
    }

    @Override
    public Film addFilm(Film film) {
        Map<String, Object> parameters = Map.of(
                "title", film.getName(),
                "description", film.getDescription(),
                "release_date", film.getReleaseDate(),
                "duration", film.getDuration(),
                "id_rating", film.getMpa().getId());
        film.setId((Integer) simpleJdbcInsert.executeAndReturnKey(parameters));
        addGenres(film);
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        String sqlQuery = "UPDATE films SET title = ?, description = ?, release_date = ?, duration = ?, id_rating = ? " +
                "WHERE id_film = ?";

        int count = jdbcTemplate.update(sqlQuery, film.getName(), film.getDescription(), film.getReleaseDate(),
                film.getDuration(), film.getMpa().getId(), film.getId());
        if (count == 0) {
            throw new NotFoundException(String.format("Film with id=%d not found", film.getId()));
        }
        deleteGenres(film);
        addGenres(film);
        return film;
    }

    @Override
    public List<Film> getFilms() {
        String sqlQuery = "SELECT * FROM films f JOIN mpa_rating mr ON f.id_rating = mr.id_rating";
        Map<Integer, Set<Genre>> genres = getFilmsGenresMap(null);
        return jdbcTemplate.query(sqlQuery, (rs, numRow) -> makeFilm(rs, genres));
    }

    @Override
    public Film getFilmById(Integer filmId) {
        Map<Integer, Set<Genre>> genres = getFilmsGenresMap(Set.of(filmId));
        String sqlQuery = "SELECT * FROM films f JOIN mpa_rating mr ON f.id_rating = mr.id_rating WHERE id_film = ?";
        List<Film> films = jdbcTemplate.query(sqlQuery, (rs, rowNum) -> makeFilm(rs, genres), filmId);
        if (films.isEmpty()) {
            throw new NotFoundException(String.format("Film with id=%d not found", filmId));
        }
        return films.get(0);
    }

    @Override
    public List<Film> getPopularFilms(Integer count) {
        Map<Integer, Set<Genre>> genres = getFilmsGenresMap(null);
        String sqlQuery = "SELECT f.id_film, f.title, f.description, f.release_date, f.duration, f.id_rating, mr.rating_name " +
                          "FROM films f " +
                          "JOIN mpa_rating mr ON f.id_rating = mr.id_rating " +
                          "JOIN likes l ON f.id_film = l.id_film " +
                          "GROUP BY f.id_film " +
                          "ORDER BY COUNT(l.id_user) DESC " +
                          "LIMIT ?";
        return jdbcTemplate.query(sqlQuery, (rs, numRow) -> makeFilm(rs, genres), count);
    }

    @Override
    public void addLike(Integer filmId, Integer userId) {
        String sqlQuery = "INSERT INTO likes (id_user, id_film) VALUES (?, ?)";
        jdbcTemplate.update(sqlQuery, userId, filmId);
    }

    @Override
    public void deleteLike(Integer filmId, Integer userId) {
        String sqlQuery = "DELETE FROM likes WHERE id_user = ? AND id_film = ?";
        jdbcTemplate.update(sqlQuery, userId, filmId);
    }

    private String getRatingName(Integer id) {
        String sqlQuery = "SELECT rating_name FROM mpa_rating WHERE id_rating = ?";
        return jdbcTemplate.queryForObject(sqlQuery, String.class, id);
    }

    private void addGenres(Film film) {
        String sqlQuery = "INSERT INTO films_genres (id_film, id_genre) VALUES (?, ?)";
        int filmId = film.getId();
        Set<Genre> genres = film.getGenres();

        if (genres == null) return;

        jdbcTemplate.batchUpdate(sqlQuery, genres, genres.size(), (ps, genre) -> {
            ps.setInt(1, filmId);
            ps.setInt(2, genre.getId());
        });
    }

    private void deleteGenres(Film film) {
        String sqlQuery = "DELETE FROM films_genres WHERE id_film = ?";
        jdbcTemplate.update(sqlQuery, film.getId());
    }

    private Map<Integer, Set<Genre>> getFilmsGenresMap(Set<Integer> ids) {
        String sqlQuery;
        SqlRowSet rowSet;
        if (ids == null) {
            sqlQuery = "SELECT * FROM films_genres fg JOIN genres g ON fg.id_genre = g.id_genre";
            rowSet = jdbcTemplate.queryForRowSet(sqlQuery);
        } else {
            MapSqlParameterSource parameters = new MapSqlParameterSource("ids", ids);
            sqlQuery = "SELECT * FROM films_genres fg JOIN genres g ON fg.id_genre = g.id_genre " +
                    "WHERE fg.id_film IN (:ids)";
            rowSet = parameterJdbcTemplate.queryForRowSet(sqlQuery, parameters);
        }
        Map<Integer, Set<Genre>> genres = new HashMap<>();
        while (rowSet.next()) {
            Integer filmId = rowSet.getInt("id_film");
            Genre genre = Genre.builder()
                    .id(rowSet.getInt("id_genre"))
                    .name(rowSet.getString("genre_name"))
                    .build();
            if (genres.containsKey(filmId)) {
                genres.get(filmId).add(genre);
            } else {
                genres.put(filmId, new HashSet<>(Set.of(genre)));
            }
        }
        return genres;
    }

    private Integer getFilmLikesCount(Integer filmId) {
        String sqlQuery = "SELECT COUNT(id_user) FROM likes WHERE id_film = ?";
        return jdbcTemplate.queryForObject(sqlQuery, Integer.class, filmId);
    }

    private Film makeFilm(ResultSet rs, Map<Integer, Set<Genre>> map) throws SQLException {
        int filmId = rs.getInt("id_film");
        return Film.builder()
                .id(filmId)
                .name(rs.getString("title"))
                .description(rs.getString("description"))
                .duration(rs.getInt("duration"))
                .releaseDate(rs.getDate("release_date").toLocalDate())
                .mpa(MpaRating.builder()
                        .id(rs.getInt("id_rating"))
                        .name(rs.getString("rating_name"))
                        .build())
                .genres(map.getOrDefault(filmId, new HashSet<>()))
                .likesCount(getFilmLikesCount(filmId))
                .build();
    }
}
