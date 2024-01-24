package ru.yandex.practicum.filmorate.repository.queries;

public class MpaQueries {
    public static final String GET_MPA_NAME_BY_FILM_ID = "SELECT name FROM mpa WHERE id = ?";
    public static final String GET_ALL_MPA = "SELECT * FROM mpa";
    public static final String GET_MPA_BY_ID = "SELECT * FROM mpa WHERE id = ?";
}
