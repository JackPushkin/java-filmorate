package ru.yandex.practicum.filmorate.exeption;

public class UpdateUserException extends RuntimeException {

    public UpdateUserException(String message) {
        super(message);
    }
}
