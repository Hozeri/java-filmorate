package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;
import java.util.Set;
import java.util.TreeSet;

@Data
@Builder
public class User {

    private Integer id;
    @NotBlank(message = "Электронная почта не может быть пустой")
    @Email
    private String email;
    @NotBlank(message = "Логин не может быть пустым")
    @Pattern(regexp = "^[a-zA-Z0-9]+", message = "Логин содержит пробелы")
    private String login;
    private String name;
    @PastOrPresent
    private LocalDate birthday;
    private Set<Integer> friends;

    public User(Integer id, String email, String login, String name, LocalDate birthday, Set<Integer> friends) {
        this.id = id;
        this.email = email;
        this.login = login;
        if (name == null || name.isBlank()) {
            this.name = login;
        } else {
            this.name = name;
        }
        this.birthday = birthday;
        this.friends = new TreeSet<>();
    }
}
