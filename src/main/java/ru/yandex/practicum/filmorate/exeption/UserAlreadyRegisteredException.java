package ru.yandex.practicum.filmorate.exeption;

public class UserAlreadyRegisteredException extends RuntimeException {

    public UserAlreadyRegisteredException(String message) {
        super(message);
    }
}
