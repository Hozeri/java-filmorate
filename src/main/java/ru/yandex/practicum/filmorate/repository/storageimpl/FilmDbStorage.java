package ru.yandex.practicum.filmorate.repository.storageimpl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.repository.storages.FilmStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static ru.yandex.practicum.filmorate.repository.queries.FilmQueries.CHECK_IS_EXIST_BY_ID;
import static ru.yandex.practicum.filmorate.repository.queries.FilmQueries.DELETE_FILM_GENRES;
import static ru.yandex.practicum.filmorate.repository.queries.FilmQueries.DELETE_FILM_LIKE;
import static ru.yandex.practicum.filmorate.repository.queries.FilmQueries.GET_ALL_FILMS;
import static ru.yandex.practicum.filmorate.repository.queries.FilmQueries.GET_ALL_FILM_GENRES;
import static ru.yandex.practicum.filmorate.repository.queries.FilmQueries.GET_ALL_FILM_LIKES;
import static ru.yandex.practicum.filmorate.repository.queries.FilmQueries.GET_FILM_BY_ID;
import static ru.yandex.practicum.filmorate.repository.queries.FilmQueries.INSERT_FILM_GENRES;
import static ru.yandex.practicum.filmorate.repository.queries.FilmQueries.INSERT_FILM_LIKE;
import static ru.yandex.practicum.filmorate.repository.queries.FilmQueries.UPDATE_FILM_BY_ID;
import static ru.yandex.practicum.filmorate.repository.queries.GenresQueries.GET_ALL_GENRES_BY_FILM_ID;
import static ru.yandex.practicum.filmorate.repository.queries.MpaQueries.GET_MPA_NAME_BY_FILM_ID;

@Repository
@Slf4j
@AllArgsConstructor
public class FilmDbStorage implements FilmStorage {

    private JdbcTemplate jdbcTemplate;

    @Override
    public Film create(Film film) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate.getDataSource())
                .withTableName("films")
                .usingGeneratedKeyColumns("id");
        Map<String, String> params = Map.of(
                "name", film.getName(),
                "description", film.getDescription(),
                "release_date", film.getReleaseDate().toString(),
                "duration", String.valueOf(film.getDuration()),
                "mpa_id", String.valueOf(film.getMpa().getId())
        );
        film.setId(simpleJdbcInsert.executeAndReturnKey(params).intValue());
        updateFilmMpaName(film);
        insertFilmGenres(film);
        log.info("Добавлен новый фильм {}", film);
        return film;
    }

    @Override
    public Film update(Film film) {
        if (isFilmInStorage(film.getId())) {
            jdbcTemplate.update(
                    UPDATE_FILM_BY_ID,
                    film.getName(),
                    film.getDescription(),
                    film.getReleaseDate(),
                    film.getDuration(),
                    film.getMpa().getId(),
                    film.getId()
            );
            updateFilmMpaName(film);
            updateFilmGenres(film);
        } else {
            return null;
        }
        log.info("Обновлены данные фильма с id = {}", film.getId());
        return film;
    }

    @Override
    public List<Film> getAll() {
        List<Film> films = jdbcTemplate.query(GET_ALL_FILMS, this::makeFilm);
        if (!films.isEmpty()) {
            Map<Integer, List<Genre>> filmGenres;
            try {
                filmGenres = jdbcTemplate.queryForObject(GET_ALL_FILM_GENRES, (rs, rowNum) -> {
                    Map<Integer, List<Genre>> temp = new HashMap<>();
                    do {
                        if (temp.containsKey(rs.getInt("film_id"))) {
                            temp.get(rs.getInt("film_id"))
                                    .add(new Genre(rs.getInt("genre_id"), rs.getString("name")));
                        } else {
                            List<Genre> genres = new ArrayList<>();
                            genres.add(new Genre(rs.getInt("genre_id"), rs.getString("name")));
                            temp.put(rs.getInt("film_id"), genres);
                        }
                    } while (rs.next());
                    return temp;
                });
            } catch (EmptyResultDataAccessException e) {
                return films;
            }
            return films.stream()
                    .peek(film -> {
                        if (filmGenres.get(film.getId()) != null) {
                            film.setGenres(filmGenres.get(film.getId()));
                        } else {
                            film.setGenres(List.of());
                        }
                    })
                    .collect(Collectors.toList());
        } else {
            return List.of();
        }
    }

    @Override
    public Film getById(Integer id) {
        if (isFilmInStorage(id)) {
            return jdbcTemplate.queryForObject(GET_FILM_BY_ID, this::makeFilm, id);
        } else {
            return null;
        }
    }

    @Override
    public void addLike(Integer id, Integer userId) {
        jdbcTemplate.update(INSERT_FILM_LIKE, id, userId);
    }

    @Override
    public void deleteLike(Integer id, Integer userId) {
        jdbcTemplate.update(DELETE_FILM_LIKE, id, userId);
    }

    @Override
    public List<Film> getMostLikedFilms(Integer count) {
        List<Film> films = jdbcTemplate.query(GET_ALL_FILMS, this::makeFilm);
        Map<Integer, Integer> filmLikes;
        if (!films.isEmpty()) {
            try {
                filmLikes = jdbcTemplate.queryForObject(GET_ALL_FILM_LIKES, (rs, rowNum) -> {
                    Map<Integer, Integer> temp = new HashMap<>();
                    do {
                        temp.put(rs.getInt("id"), rs.getInt("likes"));
                    } while (rs.next());
                    return temp;
                });
            } catch (EmptyResultDataAccessException e) {
                return List.of();
            }
        } else {
            return List.of();
        }
        return films.stream()
                .peek(film -> film.getLikes().add(filmLikes.get(film.getId())))
                .sorted(this::compare)
                .limit(count)
                .collect(Collectors.toList());
    }

    private int compare(Film f1, Film f2) {
        Integer f1Size = f1.getLikes().size();
        Integer f2Size = f2.getLikes().size();
        if (f1.getLikes().contains(0)) {
            f1Size = 0;
        }
        if (f2.getLikes().contains(0)) {
            f2Size = 0;
        }
        int result = Integer.compare(f1Size, f2Size);
        return -1 * result;
    }

    private Film makeFilm(ResultSet rs, int rowNum) throws SQLException {
        Film film = new Film();
        film.setId(rs.getInt("id"));
        film.setName(rs.getString("name"));
        film.setDescription(rs.getString("description"));
        film.setReleaseDate(rs.getDate("release_date").toLocalDate());
        film.setDuration(rs.getInt("duration"));
        film.setMpa(new Mpa(rs.getInt("mpa_id"), rs.getString("mpa_name")));
        addGenresByFilmId(film);
        return film;
    }

    private void addGenresByFilmId(Film film) {
        List<Genre> genres = jdbcTemplate.query(
                GET_ALL_GENRES_BY_FILM_ID,
                (rs1, rowNum1) -> new Genre(rs1.getInt("id"), rs1.getString("name")),
                film.getId()
        );
        if (!genres.isEmpty()) {
            film.setGenres(genres);
        }
    }

    private void updateFilmMpaName(Film film) {
        film.getMpa().setName(jdbcTemplate.queryForObject(
                GET_MPA_NAME_BY_FILM_ID,
                (rs, rowNum) -> rs.getString("name"),
                film.getMpa().getId()
        ));
    }

    private void insertFilmGenres(Film film) {
        if (film.getGenres() != null && !film.getGenres().isEmpty()) {
            film.getGenres().forEach(genre -> jdbcTemplate.update(INSERT_FILM_GENRES, film.getId(), genre.getId()));
            List<Genre> genres = jdbcTemplate.query(
                    GET_ALL_GENRES_BY_FILM_ID,
                    (rs, rowNum) -> new Genre(rs.getInt("id"), rs.getString("name")),
                    film.getId()
            );
            film.setGenres(genres);
        }
    }

    private void updateFilmGenres(Film film) {
        if (film.getGenres() != null && !film.getGenres().isEmpty()) {
            jdbcTemplate.update(DELETE_FILM_GENRES, film.getId());
            List<Genre> uniqueGenres = film.getGenres().stream()
                    .distinct()
                    .collect(Collectors.toList());
            uniqueGenres.forEach(genre -> jdbcTemplate.update(INSERT_FILM_GENRES, film.getId(), genre.getId()));
            List<Genre> genres = jdbcTemplate.query(
                    GET_ALL_GENRES_BY_FILM_ID,
                    (rs, rowNum) -> new Genre(rs.getInt("id"), rs.getString("name")),
                    film.getId()
            );
            film.setGenres(genres);
        } else {
            jdbcTemplate.update(DELETE_FILM_GENRES, film.getId());
        }
    }

    private boolean isFilmInStorage(Integer id) {
        return Boolean.TRUE.equals(jdbcTemplate.queryForObject(CHECK_IS_EXIST_BY_ID, Boolean.class, id));
    }
}
