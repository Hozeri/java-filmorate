package ru.yandex.practicum.filmorate.repository;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
@Slf4j
@AllArgsConstructor
public class UserDbStorage implements Storage<User> {

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
        Number id = simpleJdbcInsert.executeAndReturnKey(params);
        user.setId(id.intValue());
        log.info("Добавлен новый пользователь {}", user);
        return user;
    }

    @Override
    public User update(User user) {
        log.info("Обновлены данные пользователя с id = {}", user.getId());
        return user;
    }

    @Override
    public List<User> getAll() {
        return new ArrayList<>();
    }

    @Override
    public User getById(Integer id) {
        return null;
    }
}
