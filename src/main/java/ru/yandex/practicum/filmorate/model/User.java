package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;

@Data
public class User {

    private int id;
    @NotBlank(message = "Электронная почта не может быть пустой")
    @Email
    private String email;
    @NotBlank(message = "Логин не может быть пустым")
    @Pattern(regexp = "^[a-zA-Z0-9]+", message = "Логин содержит пробелы")
    private String login;
    private String name;
    @PastOrPresent
    private LocalDate birthday;

    public User(String email, String login, String name, LocalDate birthday) {
        this.email = email;
        this.login = login;
        if (name == null || name.isBlank()) {
            this.name = login;
        } else {
            this.name = name;
        }
        this.birthday = birthday;
    }
}
