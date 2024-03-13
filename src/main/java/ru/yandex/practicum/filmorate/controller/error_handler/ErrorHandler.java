package ru.yandex.practicum.filmorate.controller.error_handler;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.yandex.practicum.filmorate.exeption.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exeption.FilmValidationException;
import ru.yandex.practicum.filmorate.exeption.IncorrectParameterException;
import ru.yandex.practicum.filmorate.exeption.UserNotFoundException;
import ru.yandex.practicum.filmorate.exeption.UserValidationException;
import ru.yandex.practicum.filmorate.model.ErrorResponse;

@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler(value = { FilmNotFoundException.class, UserNotFoundException.class })
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public ErrorResponse filmNotFoundExceptionHandler(RuntimeException e) {
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler(value = { FilmValidationException.class, UserValidationException.class })
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ErrorResponse filmValidationExceptionHandler(RuntimeException e) {
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ErrorResponse incorrectParameterExceptionHandler(IncorrectParameterException e) {
        return new ErrorResponse(String.format("Incorrect parameter: %s", e.getParameter()));
    }

    @ExceptionHandler
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse throwableHandler(Throwable e) {
        return new ErrorResponse("Unexpected error.");
    }
}
