package ru.yandex.practicum.filmorate.storage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exeption.NotFoundException;
import ru.yandex.practicum.filmorate.model.MpaRating;
import ru.yandex.practicum.filmorate.storage.interfaces.MpaStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class MpaStorageImpl implements MpaStorage {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public MpaStorageImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public MpaRating getMpaRatingById(Integer id) {
        String sqlQuery = "SELECT * FROM mpa_rating WHERE id_rating = ?";
        List<MpaRating> ratings = jdbcTemplate.query(sqlQuery, (rs, rowNum) -> makeMpaRating(rs), id);
        if (ratings.isEmpty()) {
            throw new NotFoundException(String.format("Rating with id=%d does not exist", id));
        }
        return ratings.get(0);
    }

    @Override
    public List<MpaRating> getAllMpaRatings() {
        String sqlQuery = "SELECT * FROM mpa_rating";
        return jdbcTemplate.query(sqlQuery, (rs, rowNum) -> makeMpaRating(rs));
    }

    private MpaRating makeMpaRating(ResultSet rs) throws SQLException {
        return MpaRating.builder()
                .id(rs.getInt("id_rating"))
                .name(rs.getString("rating_name"))
                .build();
    }
}
