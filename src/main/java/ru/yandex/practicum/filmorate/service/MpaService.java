package ru.yandex.practicum.filmorate.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.MpaNotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.repository.MpaDbStorage;

import java.util.List;

@Service
@AllArgsConstructor
public class MpaService {

    private MpaDbStorage mpaStorage;

    public Mpa getMpaById(Integer id) {
        if (id == null || id <= 0) {
            throw new MpaNotFoundException("Рейтинга с таким id не существует");
        }
        Mpa mpa = mpaStorage.getById(id);
        if (mpa == null) {
            throw new MpaNotFoundException("Рейтинга с таким id не существует");
        }
        return mpa;
    }

    public List<Mpa> getAllMpa() {
        return mpaStorage.getAll();
    }
}
