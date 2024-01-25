package ru.yandex.practicum.filmorate.repository.storages;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage {

    Film create(Film user);

    Film update(Film user);

    List<Film> getAll();

    Film getById(Integer id);

    void addLike(Integer id, Integer userId);

    void deleteLike(Integer id, Integer userId);

    List<Film> getMostLikedFilms(Integer count);
}