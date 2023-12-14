package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {

    private final Map<Integer, User> users = new HashMap<>();
    private int id = 1;

    @PostMapping
    private User create(@RequestBody @Valid User user) {
        user.setId(id++);
        users.put(user.getId(), user);
        log.info("Добавлен новый пользователь {}", user);
        return user;
    }

    @PutMapping
    private User update(@RequestBody @Valid User user) {
        if (!users.containsKey(user.getId())) {
            throw new IllegalArgumentException("Пользователя с таким id не существует");
        }
        users.put(user.getId(), user);
        log.info("Обновлены данные пользователя с id = {}", user.getId());
        return user;
    }

    @GetMapping
    private List<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }
}