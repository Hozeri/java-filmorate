package ru.yandex.practicum.filmorate.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.repository.storages.FilmStorage;
import ru.yandex.practicum.filmorate.repository.storages.UserStorage;

import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class FilmService {

    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    public Film create(Film film) {
        return filmStorage.create(film);
    }

    public Film update(Film film) {
        if (filmStorage.getById(film.getId()) == null) {
            throw new FilmNotFoundException("Фильма с таким id не существует");
        }
        return filmStorage.update(film);
    }

    public List<Film> getAllFilms() {
        return filmStorage.getAll();
    }

    public Film getFilmById(Integer id) {
        if (id == null || id <= 0) {
            throw new FilmNotFoundException("id фильма не может быть пустым или отрицательным");
        }
        Film film = filmStorage.getById(id);
        if (film == null) {
            throw new FilmNotFoundException("Фильма с таким id не существует");
        }
        return film;
    }

    public Film addLike(Integer id, Integer userId) {
        Film film = checkFilmAndUserNotNull(id, userId);
        film.getLikes().add(userId);
        filmStorage.addLike(id, userId);
        log.info("Для фильма c id {} добавлен лайк от пользователя c userId {}", id, userId);
        return film;
    }

    public void deleteLike(Integer id, Integer userId) {
        Film film = checkFilmAndUserNotNull(id, userId);
        film.getLikes().remove(userId);
        filmStorage.deleteLike(id, userId);
        log.info("Для фильма c id {} удалён лайк от пользователя с userId {}", id, userId);
    }

    public List<Film> getMostLikedFilms(Integer count) {
        return filmStorage.getMostLikedFilms(count);
    }

    private Film checkFilmAndUserNotNull(Integer id, Integer userId) {
        Film film = filmStorage.getById(id);
        User user = userStorage.getById(userId);
        if (film == null) {
            throw new FilmNotFoundException("Фильма с таким id не существует");
        } else if (user == null) {
            throw new UserNotFoundException("Пользователя с таким id не существует");
        } else {
            return film;
        }
    }
}
