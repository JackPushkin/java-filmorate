package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.yandex.practicum.filmorate.validator.ValidationMarkerInterface;
import ru.yandex.practicum.filmorate.validator.annotation.MinDate;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
public class Film {
    @EqualsAndHashCode.Exclude
    private int id;

    @NotBlank(message = "must not be empty",
            groups = {ValidationMarkerInterface.onCreate.class})
    @Pattern(regexp = ".*[^ ].*",
            message = "must not be empty",
            groups = {ValidationMarkerInterface.onUpdate.class})
    private String name;

    @NotBlank(message = "must not be empty",
            groups = {ValidationMarkerInterface.onCreate.class})
    @Size(max = 200, message = "size must be between 0 and 200")
    private String description;

    @MinDate
    @NotNull(message = "must not be null",
            groups = {ValidationMarkerInterface.onCreate.class})
    private LocalDate releaseDate;

    @Positive(message = "must be greater than 0")
    private int duration;

    private final Set<Integer> usersId = new HashSet<>();

    public Film(String name, String description, LocalDate releaseDate, int duration) {
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
    }

    public void addUserIdToList(Integer id) {
        usersId.add(id);
    }

    public void deleteUserIdFromList(Integer id) {
        usersId.remove(id);
    }
}
