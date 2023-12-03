package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import ru.yandex.practicum.filmorate.DateValidation;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;
import java.time.LocalDate;

/**
 * Film.
 */
@Data
public class Film {

    private int id;
    @NotBlank(message = "Имя фильма не может быть пустым")
    private String name;
    @Size(max = 200, message = "Максимальная длина описания не должна превышать 200 символов")
    private String description;
    @DateValidation
    private LocalDate releaseDate;
    @PositiveOrZero(message = "Продолжительность фильма должна быть положительной")
    private int duration;
}
