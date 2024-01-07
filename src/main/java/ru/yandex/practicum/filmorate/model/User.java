package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;

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
    private final Set<Integer> friends = new TreeSet<>();
}
