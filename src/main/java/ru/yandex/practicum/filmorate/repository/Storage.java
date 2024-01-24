package ru.yandex.practicum.filmorate.repository;

import java.util.List;

public interface Storage<T> {

    T create(T entity);

    T update(T entity);

    List<T> getAll();

    T getById(Integer id);
}