package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.NonNull;

import java.time.LocalDate;

@Data
public class User {
    private final int id;
    private String email;
    @NonNull private String login;
    @NonNull private String name;
    private LocalDate birthday;
}
