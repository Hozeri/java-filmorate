package ru.yandex.practicum.filmorate.repository.queries;

public class UserQueries {
    public static final String GET_USER_BY_ID = "SELECT * FROM users WHERE id = ?";
    public static final String GET_USER_FRIENDS = "SELECT * FROM friends WHERE user_id = ?";
    public static final String GET_ALL_USERS = "SELECT * FROM users";
    public static final String UPDATE_USER_BY_ID =
            "UPDATE users " +

                    "SET " +

                    "name = ?, " +
                    "login = ?, " +
                    "birthday = ?, " +
                    "email = ? " +

                    "WHERE id = ?";
    public static final String INSERT_USER_FRIEND = "INSERT INTO friends (user_id, friend_id) VALUES (?, ?)";
    public static final String DELETE_USER_FRIEND = "DELETE FROM friends WHERE user_id = ? AND friend_id = ?";
    public static final String CHECK_IS_EXIST_BY_ID = "SELECT EXISTS (SELECT 1 FROM users WHERE id = ?)";
}
