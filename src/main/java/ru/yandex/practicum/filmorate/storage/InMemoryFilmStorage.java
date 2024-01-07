package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class InMemoryFilmStorage implements Storage<Film> {

    private final Map<Integer, Film> films = new HashMap<>();
    private Integer id = 1;

    @Override
    public Film create(Film film) {
        film.setId(id++);
        films.put(film.getId(), film);
        log.info("Добавлен новый фильм {}", film);
        return film;
    }

    @Override
    public Film update(Film film) {
        films.put(film.getId(), film);
        log.info("Обновлены данные фильма с id = {}", film.getId());
        return film;
    }

    @Override
    public List<Film> getAll() {
        return new ArrayList<>(films.values());
    }

    @Override
    public Film getById(Integer id) {
        return films.get(id);
    }
}
