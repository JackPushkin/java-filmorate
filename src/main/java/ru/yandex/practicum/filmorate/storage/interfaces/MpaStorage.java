package ru.yandex.practicum.filmorate.storage.interfaces;

import ru.yandex.practicum.filmorate.model.MpaRating;

import java.util.List;

public interface MpaStorage {

    public MpaRating getMpaRatingById(Integer id);

    List<MpaRating> getAllMpaRatings();
}