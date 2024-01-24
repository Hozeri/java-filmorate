package ru.yandex.practicum.filmorate.repository;

import lombok.AllArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;

import static ru.yandex.practicum.filmorate.repository.queries.MpaQueries.GET_ALL_MPA;
import static ru.yandex.practicum.filmorate.repository.queries.MpaQueries.GET_MPA_BY_ID;

@Repository
@AllArgsConstructor
public class MpaDbStorage implements GenreMpaStorage<Mpa> {

    private JdbcTemplate jdbcTemplate;

    @Override
    public Mpa getById(Integer id) {
        try {
            return jdbcTemplate.queryForObject(
                    GET_MPA_BY_ID,
                    (rs, rowNum) -> new Mpa(rs.getInt("id"), rs.getString("name")),
                    id
            );
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public List<Mpa> getAll() {
        return jdbcTemplate.query(
                GET_ALL_MPA,
                (rs, rowNum) -> new Mpa(rs.getInt("id"), rs.getString("name"))
        );
    }
}
