package ru.yandex.practicum.filmorate.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import javax.validation.ValidationException;
import java.util.List;
import java.util.stream.Collectors;

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
        if (filmStorage.getFilmById(film.getId()) == null) {
            throw new FilmNotFoundException("Фильма с таким id не существует");
        }
        return filmStorage.update(film);
    }

    public List<Film> getAllFilms() {
        return filmStorage.getAllFilms();
    }

    public Film getFilmById(Integer id) {
        if (id == null || id <= 0) {
            throw new ValidationException("id фильма не может быть пустым или отрицательным");
        }
        Film film = filmStorage.getFilmById(id);
        if (film == null) {
            throw new FilmNotFoundException("Фильма с таким id не существует");
        }
        return film;
    }

    public Film addLike(Integer id, Integer userId) {
        Film film = checkFilmAndUserNotNull(id, userId);
        film.getLikes().add(userId);
        log.info("Для фильма c id {} добавлен лайк от пользователя c userId {}", id, userId);
        return film;
    }

    public void deleteLike(Integer id, Integer userId) {
        Film film = checkFilmAndUserNotNull(id, userId);
        film.getLikes().remove(userId);
        log.info("Для фильма c id {} удалён лайк от пользователя с userId {}", id, userId);
    }

    public List<Film> getMostLikedFilms(Integer count) {
        return filmStorage.getAllFilms().stream()
                .sorted(this::compare)
                .limit(count)
                .collect(Collectors.toList());
    }

    private int compare(Film f1, Film f2) {
        int result = Integer.compare(f1.getLikes().size(), f2.getLikes().size());
        return -1 * result;
    }

    private Film checkFilmAndUserNotNull(Integer id, Integer userId) {
        Film film = filmStorage.getFilmById(id);
        User user = userStorage.getUserById(userId);
        if (film == null) {
            throw new FilmNotFoundException("Фильма с таким id не существует");
        } else if (user == null) {
            throw new UserNotFoundException("Пользователя с таким id не существует");
        } else {
            return film;
        }
    }
}
