package ru.yandex.practicum.filmorate.exeption;

public class FilmAlreadyAddedException extends RuntimeException {

    public FilmAlreadyAddedException(String message) {
        super(message);
    }
}
