package ru.yandex.practicum.filmorate.repository.storages;

import java.util.List;

public interface GenreMpaStorage<T> {

    T getById(Integer id);

    List<T> getAll();
}
