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
    private Film film;

    @BeforeEach
    public void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
        film = new Film();
        film.setId(ID);
    }

    @Test
    public void emptyNameShouldFailValidation() {
        film.setDescription("Description");
        film.setDuration(100);
        film.setReleaseDate(LocalDate.of(1956, 1, 1));
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void descriptionOver200CharsShouldFailValidation() {
        film.setName("Name");
        film.setDescription("Description over 200 Description over 200 Description over 200 Description over 200 " +
                "Description over 200 Description over 200 Description over 200 Description over 200 " +
                "Description over 200 Descriptiono");
        film.setDuration(100);
        film.setReleaseDate(LocalDate.of(1956, 1, 1));
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void releaseDateBefore1895ShouldFailValidation() {
        film.setName("Name");
        film.setDescription("Description");
        film.setDuration(100);
        film.setReleaseDate(LocalDate.of(1894, 1, 1));
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void negativeDurationShouldFailValidation() {
        film.setName("Name");
        film.setDescription("Description");
        film.setDuration(-100);
        film.setReleaseDate(LocalDate.of(1896, 1, 1));
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertFalse(violations.isEmpty());
    }
}
