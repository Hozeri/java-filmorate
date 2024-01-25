package ru.yandex.practicum.filmorate.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.repository.storageimpl.GenreDbStorage;

import java.util.List;

@Service
@AllArgsConstructor
public class GenreService {

    private final GenreDbStorage genreStorage;

    public Genre getGenreById(Integer id) {
        if (id == null || id <= 0) {
            throw new EntityNotFoundException("Жанра с таким id не существует");
        }
        Genre genre = genreStorage.getById(id);
        if (genre == null) {
            throw new EntityNotFoundException("Жанра с таким id не существует");
        }
        return genre;
    }

    public List<Genre> getAllGenres() {
        return genreStorage.getAll();
    }
}
