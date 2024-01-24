package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.repository.storageimpl.FilmDbStorage;

import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@SpringBootTest
@AutoConfigureTestDatabase
@Sql(statements = "DELETE FROM films")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class FilmDbStorageTest {

    private final FilmDbStorage filmStorage;

    @Test
    void emptyListShouldReturnSize0() {
        assertThat(filmStorage.getAll()).isEmpty();
    }

    @Test
    void create1FilmShouldReturnSize1WhenGetAllFilms() {
        filmStorage.create(getFilm(1));
        assertThat(filmStorage.getAll()).hasSize(1);
    }

    @Test
    void emptyFilmsListShouldReturnNullWhenGetById() {
        assertThat(filmStorage.getById(1)).isNull();
    }

    @Test
    void shouldReturnSameFilmWhenGetById() {
        Film film = filmStorage.create(getFilm(1));
        assertThat(filmStorage.getById(film.getId())).isEqualTo(getFilm(film.getId()));
    }

    @Test
    void emptyFilmsListShouldReturnNullWhenUpdateFilm() {
        assertThat(filmStorage.update(getFilm(1))).isNull();
    }

    @Test
    void shouldReturnUpdatedFilmWhenUpdateFilm() {
        Film film = filmStorage.create(getFilm(1));
        Film newFilm = new Film();
        film.setId(1);
        film.setName("Name1");
        film.setDescription("Description");
        film.setReleaseDate(LocalDate.now());
        film.setDuration(120);
        film.setMpa(new Mpa(2, "PG"));
        film.setGenres(new LinkedHashSet<>());
        Film updatedFilm = filmStorage.update(newFilm);
        assertNotEquals(film, updatedFilm);
    }

    private Film getFilm(int id) {
        Film film = new Film();
        film.setId(id);
        film.setName("Name");
        film.setDescription("Description");
        film.setReleaseDate(LocalDate.now());
        film.setDuration(120);
        film.setMpa(new Mpa(2, "PG"));
        film.setGenres(new LinkedHashSet<>());
        return film;
    }
}
