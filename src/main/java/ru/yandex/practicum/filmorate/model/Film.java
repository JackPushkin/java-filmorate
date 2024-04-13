package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;
import ru.yandex.practicum.filmorate.validator.ValidationMarkerInterface;
import ru.yandex.practicum.filmorate.validator.annotation.MinDate;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Data
@Builder
public class Film {

    private int id;

    @NotBlank(message = "must not be empty",
            groups = {ValidationMarkerInterface.OnCreate.class})
    @Pattern(regexp = ".*[^ ].*",
            message = "must not be empty",
            groups = {ValidationMarkerInterface.OnUpdate.class})
    private String name;

    @NotBlank(message = "must not be empty",
            groups = {ValidationMarkerInterface.OnCreate.class})
    @Size(max = 200, message = "size must be between 0 and 200")
    private String description;

    @MinDate
    @NotNull(message = "must not be null",
            groups = {ValidationMarkerInterface.OnCreate.class})
    private LocalDate releaseDate;

    @Positive(message = "must be greater than 0")
    @NotNull(message = "must not be null",
            groups = {ValidationMarkerInterface.OnCreate.class})
    private Integer duration;

    @Valid
    @NotNull(message = "must not be null",
            groups = {ValidationMarkerInterface.OnCreate.class})
    private MpaRating mpa;

    @Valid
    private Set<Genre> genres;

    private Integer likesCount;
}
