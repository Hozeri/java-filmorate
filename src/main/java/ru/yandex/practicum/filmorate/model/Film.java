package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import ru.yandex.practicum.filmorate.DateValidation;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * Film.
 */
@Data
public class Film {

    private Integer id;
    @NotBlank(message = "Имя фильма не может быть пустым")
    private String name;
    @Size(max = 200, message = "Максимальная длина описания не должна превышать 200 символов")
    private String description;
    @DateValidation
    private LocalDate releaseDate;
    @Positive(message = "Продолжительность фильма должна быть положительной")
    private Integer duration;
    private LinkedHashSet<Genre> genres = new LinkedHashSet<>();
    @NotNull
    private Mpa mpa;
}
