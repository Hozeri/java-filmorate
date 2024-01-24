package ru.yandex.practicum.filmorate.repository.queries;

public class FilmQueries {
    public static final String GET_FILM_BY_ID =
            "SELECT " +
                    "f.id,  " +
                    "f.name, " +
                    "f.description, " +
                    "f.release_date, " +
                    "f.duration, " +
                    "f.mpa_id, " +
                    "m.name AS mpa_name " +

                    "FROM films AS f " +

                    "JOIN mpa AS m ON f.mpa_id = m.id " +

                    "WHERE f.id = ?";
    public static final String UPDATE_FILM_BY_ID =
            "UPDATE films " +

                    "SET " +

                    "name = ? ," +
                    "description = ?, " +
                    "release_date = ?, " +
                    "duration = ?, " +
                    "mpa_id = ? " +

                    "WHERE id = ?";
    public static final String GET_ALL_FILMS =
            "SELECT " +
                    "f.id,  " +
                    "f.name, " +
                    "f.description, " +
                    "f.release_date, " +
                    "f.duration, " +
                    "f.mpa_id, " +
                    "m.name AS mpa_name " +

                    "FROM films AS f " +

                    "JOIN mpa AS m ON f.mpa_id = m.id";
    public static final String GET_ALL_FILM_GENRES =
            "SELECT " +
                    "fg.film_id, " +
                    "fg.genre_id, " +
                    "g.name " +

                    "FROM film_genres AS fg " +

                    "LEFT JOIN genres AS g ON fg.genre_id = g.id";
    public static final String GET_ALL_FILM_LIKES =
            "SELECT " +
                    "f.id, " +
                    "f.name, " +
                    "f.description, " +
                    "f.release_date, " +
                    "f.duration, " +
                    "f.mpa_id, " +
                    "m.name AS mpa_name, " +
                    "COUNT(l.user_id) AS likes " +

                    "FROM films AS f " +

                    "LEFT JOIN likes AS l ON f.id = l.film_id " +

                    "LEFT JOIN mpa AS m ON f.mpa_id = m.id " +

                    "GROUP BY f.id, mpa_name " +

                    "ORDER BY likes DESC " +

                    "LIMIT ?";
    public static final String INSERT_FILM_GENRES = "INSERT INTO film_genres (film_id, genre_id) VALUES (?, ?)";
    public static final String INSERT_FILM_LIKE = "INSERT INTO likes (film_id, user_id) VALUES (?, ?)";
    public static final String DELETE_FILM_GENRES = "DELETE FROM film_genres WHERE film_id = ?";
    public static final String DELETE_FILM_LIKE = "DELETE FROM likes WHERE film_id = ? AND user_id = ?";
    public static final String CHECK_IS_EXIST_BY_ID = "SELECT EXISTS (SELECT 1 FROM films WHERE id = ?)";
}
