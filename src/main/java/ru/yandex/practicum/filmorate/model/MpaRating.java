package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Data
@Builder
public class MpaRating {

    @NotNull(message = "must not be null")
    @Positive(message = "must be greater than 0")
    @Max(value = 5, message = "must not be greater than 5")
    private Integer id;
    private String name;
}
