package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {

    private final Map<Integer, Film> films = new HashMap<>();
    private int id = 1;

    @PostMapping
    private Film create(@RequestBody @Valid Film film) {
        film.setId(id++);
        films.put(film.getId(), film);
        log.info("Добавлен новый фильм {}", film);
        return film;
    }

    @PutMapping
    private Film update(@RequestBody @Valid Film film) {
        if (!films.containsKey(film.getId())) {
            throw new IllegalArgumentException("Фильма с таким id не существует");
        }
        films.put(film.getId(), film);
        log.info("Обновлены данные фильма с id = {}", film.getId());
        return film;
    }

    @GetMapping
    private List<Film> getAllFilms() {
        return new ArrayList<>(films.values());
    }

}
