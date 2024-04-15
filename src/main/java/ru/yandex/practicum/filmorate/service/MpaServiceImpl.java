package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.MpaRating;
import ru.yandex.practicum.filmorate.service.interfaces.MpaService;
import ru.yandex.practicum.filmorate.storage.interfaces.MpaStorage;

import java.util.List;

@Service
public class MpaServiceImpl implements MpaService {

    private final MpaStorage mpaStorage;

    @Autowired
    public MpaServiceImpl(MpaStorage mpaStorage) {
        this.mpaStorage = mpaStorage;
    }

    @Override
    public MpaRating getMpaRatingById(Integer id) {
        return mpaStorage.getMpaRatingById(id);
    }

    @Override
    public List<MpaRating> getAllMpaRatings() {
        return mpaStorage.getAllMpaRatings();
    }
}
