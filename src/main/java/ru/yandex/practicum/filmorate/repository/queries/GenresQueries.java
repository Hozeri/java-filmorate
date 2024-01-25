package ru.yandex.practicum.filmorate.repository.queries;

public class GenresQueries {
    public static final String GET_ALL_GENRES = "SELECT * FROM GENRES";
    public static final String GET_GENRE_BY_ID = "SELECT * FROM genres WHERE id = ?";
    public static final String GET_ALL_GENRES_BY_FILM_ID =
            "SELECT " +
                    "id, " +
                    "name " +

                    "FROM film_genres AS fg " +

                    "LEFT JOIN genres AS g ON fg.genre_id = g.id " +

                    "WHERE fg.film_id = ?";
}
