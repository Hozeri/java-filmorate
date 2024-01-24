package ru.yandex.practicum.filmorate.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.repository.storages.UserStorage;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class UserService {

    private final UserStorage userStorage;

    public User create(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        } else {
            user.setName(user.getName());
        }
        return userStorage.create(user);
    }

    public User update(User user) {
        if (userStorage.getById(user.getId()) == null) {
            throw new UserNotFoundException("Пользователя с таким id не существует");
        }
        return userStorage.update(user);
    }

    public List<User> getAllUsers() {
        return userStorage.getAll();
    }

    public User getUserById(Integer id) {
        return checkUserNotNull(id);
    }

    public User addFriend(Integer id, Integer friendId) {
        User user = checkUserNotNull(id, friendId);
        user.getFriends().add(friendId);
        userStorage.addFriend(id, friendId);
        log.info("Пользователь {} добавил в друзья пользователя {}", id, friendId);
        return user;
    }

    public void deleteFriend(Integer id, Integer friendId) {
        User user = checkUserNotNull(id, friendId);
        user.getFriends().remove(friendId);
        userStorage.deleteFriend(id, friendId);
        log.info("Пользователь {} удалил из друзей пользователя {}", id, friendId);
    }

    public List<User> getUserFriends(Integer id) {
        checkUserNotNull(id);
        return userStorage.getFriends(id);
    }

    public List<User> getCommonFriends(Integer id, Integer otherId) {
        checkUserNotNull(id, otherId);
        List<User> userFriends = getUserFriends(id);
        List<User> otherUserFriends = getUserFriends(otherId);
        if (userFriends.isEmpty() || otherUserFriends.isEmpty()) {
            return List.of();
        } else {
            return userFriends.stream()
                    .filter(otherUserFriends::contains)
                    .collect(Collectors.toList());
        }
    }

    private User checkUserNotNull(Integer id, Integer otherId) {
        if (id == null || id <= 0 || otherId == null || otherId <= 0) {
            throw new UserNotFoundException("id пользователя не может быть пустым или отрицательным");
        }
        User user = userStorage.getById(id);
        User friend = userStorage.getById(otherId);
        if (user == null || friend == null) {
            throw new UserNotFoundException("Пользователя с таким id не существует");
        } else {
            return user;
        }
    }

    private User checkUserNotNull(Integer id) {
        if (id == null || id <= 0) {
            throw new UserNotFoundException("id пользователя не может быть пустым или отрицательным");
        }
        User user = userStorage.getById(id);
        if (user == null) {
            throw new UserNotFoundException("Пользователя с таким id не существует");
        } else {
            return user;
        }
    }
}
