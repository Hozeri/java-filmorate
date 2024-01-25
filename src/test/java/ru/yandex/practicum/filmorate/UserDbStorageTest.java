package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.repository.storageimpl.UserDbStorage;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@SpringBootTest
@AutoConfigureTestDatabase
@Sql(statements = "delete from Users")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserDbStorageTest {

    private final UserDbStorage userStorage;

    @Test
    void emptyListShouldReturnSize0() {
        assertThat(userStorage.getAll()).isEmpty();
    }

    @Test
    void create1UserShouldReturnSize1WhenGetAllFilms() {
        userStorage.create(getUser(1, "email@email.ru"));
        assertThat(userStorage.getAll()).hasSize(1);
    }

    @Test
    void emptyUsersListShouldReturnNullWhenGetById() {
        assertThat(userStorage.getById(1)).isNull();
    }

    @Test
    void shouldReturnSameUserWhenGetById() {
        User user = userStorage.create(getUser(1, "email@email.ru"));
        assertThat(userStorage.getById(user.getId())).isEqualTo(getUser(user.getId(), "email@email.ru"));
    }

    @Test
    void shouldReturnNullWhenUpdate() {
        assertThat(userStorage.update(getUser(22, "email@email.ru"))).isNull();
    }

    @Test
    void shouldReturnUpdatedUserWhenUpdateUser() {
        User user = userStorage.create(getUser(2, "email@email.ru"));
        User newUser = getUser(2, "email1@email.ru");
        assertNotEquals(user, userStorage.update(newUser));
    }

    private User getUser(int id, String email) {
        User user = new User();
        user.setId(id);
        user.setName("Name");
        user.setEmail(email);
        user.setLogin("login");
        user.setBirthday(LocalDate.now());
        return user;
    }
}
