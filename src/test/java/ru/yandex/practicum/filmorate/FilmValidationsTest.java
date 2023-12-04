package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;

public class FilmValidationsTest {

    private Validator validator;
    public static final int ID = 1;

    @BeforeEach
    public void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void emptyNameShouldFailValidation() {
        Film film = Film.builder()
                .id(ID)
                .description("Description")
                .duration(100)
                .releaseDate(LocalDate.of(1956, 1, 1))
                .build();
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void descriptionOver200CharsShouldFailValidation() {
        Film film = Film.builder()
                .id(ID)
                .name("Name")
                .description("Description over 200 Description over 200 Description over 200 Description over 200 " +
                        "Description over 200 Description over 200 Description over 200 Description over 200 " +
                        "Description over 200 Descriptiono")
                .duration(100)
                .releaseDate(LocalDate.of(1956, 1, 1))
                .build();
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void releaseDateBefore1895ShouldFailValidation() {
        Film film = Film.builder()
                .id(ID)
                .name("Name")
                .description("Description")
                .duration(100)
                .releaseDate(LocalDate.of(1894, 1, 1))
                .build();
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void negativeDurationShouldFailValidation() {
        Film film = Film.builder()
                .id(ID)
                .name("Name")
                .description("Description")
                .duration(-100)
                .releaseDate(LocalDate.of(1896, 1, 1))
                .build();
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertFalse(violations.isEmpty());
    }
}
