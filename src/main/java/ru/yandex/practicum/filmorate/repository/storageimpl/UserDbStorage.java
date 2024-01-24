package ru.yandex.practicum.filmorate.repository.storageimpl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.repository.storages.UserStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static ru.yandex.practicum.filmorate.repository.queries.UserQueries.CHECK_IS_EXIST_BY_ID;
import static ru.yandex.practicum.filmorate.repository.queries.UserQueries.DELETE_USER_FRIEND;
import static ru.yandex.practicum.filmorate.repository.queries.UserQueries.GET_ALL_USERS;
import static ru.yandex.practicum.filmorate.repository.queries.UserQueries.GET_USER_BY_ID;
import static ru.yandex.practicum.filmorate.repository.queries.UserQueries.GET_USER_FRIENDS;
import static ru.yandex.practicum.filmorate.repository.queries.UserQueries.INSERT_USER_FRIEND;
import static ru.yandex.practicum.filmorate.repository.queries.UserQueries.UPDATE_USER_BY_ID;

@Repository
@Slf4j
@AllArgsConstructor
public class UserDbStorage implements UserStorage {

    private JdbcTemplate jdbcTemplate;

    @Override
    public User create(User user) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate.getDataSource())
                .withTableName("users")
                .usingGeneratedKeyColumns("id");
        Map<String, String> params = Map.of(
                "login", user.getLogin(),
                "email", user.getEmail(),
                "name", user.getName(),
                "birthday", user.getBirthday().toString()
        );
        user.setId(simpleJdbcInsert.executeAndReturnKey(params).intValue());
        log.info("Добавлен новый пользователь {}", user);
        return user;
    }

    @Override
    public User update(User user) {
        if (isUserInStorage(user.getId())) {
            jdbcTemplate.update(
                    UPDATE_USER_BY_ID,
                    user.getName(),
                    user.getLogin(),
                    user.getBirthday(),
                    user.getEmail(),
                    user.getId());
        } else {
            return null;
        }
        log.info("Обновлены данные пользователя с id = {}", user.getId());
        return user;
    }

    @Override
    public void addFriend(Integer id, Integer friendId) {
        jdbcTemplate.update(INSERT_USER_FRIEND, id, friendId);
    }

    @Override
    public void deleteFriend(Integer userId, Integer friendId) {
        jdbcTemplate.update(DELETE_USER_FRIEND, userId, friendId);
    }

    @Override
    public List<User> getFriends(Integer id) {
        List<Integer> userFriends = jdbcTemplate.query(
                GET_USER_FRIENDS,
                (rs, rowNum) -> rs.getInt("friend_id"),
                id
        );
        List<User> users = jdbcTemplate.query(GET_ALL_USERS, this::makeUser);
        return users.stream().filter(user -> userFriends.contains(user.getId())).collect(Collectors.toList());
    }

    @Override
    public List<User> getAll() {
        return jdbcTemplate.query(GET_ALL_USERS, this::makeUser);
    }

    @Override
    public User getById(Integer id) {
        if (isUserInStorage(id)) {
            return jdbcTemplate.queryForObject(GET_USER_BY_ID, this::makeUser, id);
        } else {
            return null;
        }
    }

    private User makeUser(ResultSet rs, int rowNum) throws SQLException {
        User user = new User();
        user.setId(rs.getInt("id"));
        user.setLogin(rs.getString("login"));
        user.setEmail(rs.getString("email"));
        user.setName(rs.getString("name"));
        user.setBirthday(rs.getDate("birthday").toLocalDate());
        return user;
    }

    private boolean isUserInStorage(Integer id) {
        return Boolean.TRUE.equals(jdbcTemplate.queryForObject(CHECK_IS_EXIST_BY_ID, Boolean.class, id));
    }
}
