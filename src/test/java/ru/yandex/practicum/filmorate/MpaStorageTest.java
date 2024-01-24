package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.repository.storageimpl.MpaDbStorage;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class MpaStorageTest {

    private final MpaDbStorage mpaStorage;

    @Test
    void shouldReturnSize5WhenGetAllMpa() {
        assertThat(mpaStorage.getAll()).hasSize(5);
    }

    @Test
    void shouldReturnTheFirstMpaWhenGetById() {
        Mpa mpa = new Mpa(1, "G");
        assertThat(mpaStorage.getById(1)).isEqualTo(mpa);
    }
}
