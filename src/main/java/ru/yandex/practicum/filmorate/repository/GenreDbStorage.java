package ru.yandex.practicum.filmorate.repository;

import lombok.AllArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

import static ru.yandex.practicum.filmorate.repository.queries.GenresQueries.GET_ALL_GENRES;
import static ru.yandex.practicum.filmorate.repository.queries.GenresQueries.GET_GENRE_BY_ID;

@Repository
@AllArgsConstructor
public class GenreDbStorage implements GenreMpaStorage<Genre> {

    private JdbcTemplate jdbcTemplate;

    @Override
    public Genre getById(Integer id) {
        try {
            return jdbcTemplate.queryForObject(
                    GET_GENRE_BY_ID,
                    (rs, rowNum) -> new Genre(rs.getInt("id"), rs.getString("name")),
                    id
            );
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public List<Genre> getAll() {
        return jdbcTemplate.query(
                GET_ALL_GENRES,
                (rs, rowNum) -> new Genre(rs.getInt("id"), rs.getString("name"))
        );
    }
}
