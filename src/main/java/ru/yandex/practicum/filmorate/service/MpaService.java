package ru.yandex.practicum.filmorate.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.repository.storageimpl.MpaDbStorage;

import java.util.List;

@Service
@AllArgsConstructor
public class MpaService {

    private final MpaDbStorage mpaStorage;

    public Mpa getMpaById(Integer id) {
        if (id == null || id <= 0) {
            throw new EntityNotFoundException("Рейтинга с таким id не существует");
        }
        Mpa mpa = mpaStorage.getById(id);
        if (mpa == null) {
            throw new EntityNotFoundException("Рейтинга с таким id не существует");
        }
        return mpa;
    }

    public List<Mpa> getAllMpa() {
        return mpaStorage.getAll();
    }
}
