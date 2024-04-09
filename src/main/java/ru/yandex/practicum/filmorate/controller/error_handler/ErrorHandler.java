package ru.yandex.practicum.filmorate.controller.error_handler;

import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.yandex.practicum.filmorate.exeption.*;
import ru.yandex.practicum.filmorate.model.ErrorResponse;

import javax.validation.ConstraintViolationException;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler(value = {
            FilmNotFoundException.class,
            UserNotFoundException.class,
            RatingNotFoundException.class,
            GenreNotFoundException.class })
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public ErrorResponse notFoundExceptionHandler(RuntimeException e) {
        return new ErrorResponse(Map.of("id", e.getMessage()));
    }

    @ExceptionHandler
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ErrorResponse userAlreadyRegisteredExceptionHandler(UserAlreadyRegisteredException e) {
        return new ErrorResponse(Map.of("error", e.getMessage()));
    }

    @ExceptionHandler
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ErrorResponse validationExceptionHandler(MethodArgumentNotValidException e) {
        ErrorResponse errors = new ErrorResponse(new HashMap<>());
        e.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.getErrors().put(fieldName, errorMessage);
        });
        return errors;
    }

    @ExceptionHandler
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ErrorResponse parameterExceptionHandler(ConstraintViolationException e) {
        return new ErrorResponse(Map.of("Incorrect parameter", e.getMessage()));
    }
}
