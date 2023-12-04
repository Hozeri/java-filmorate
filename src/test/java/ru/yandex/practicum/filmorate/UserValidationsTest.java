package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class UserValidationsTest {

    private Validator validator;
    public static final int ID = 1;

    @BeforeEach
    public void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void emptyEmailShouldFailValidation() {
        User user = User.builder()
                .id(ID)
                .name("Name")
                .login("Login")
                .birthday(LocalDate.of(2023, 1, 1))
                .build();
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void emailWithoutAtShouldFailValidation() {
        User user = User.builder()
                .id(ID)
                .name("Name")
                .email("exmaple_email.com")
                .login("Login")
                .birthday(LocalDate.of(2023, 1, 1))
                .build();
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void emptyLoginShouldFailValidation() {
        User user = User.builder()
                .id(ID)
                .name("Name")
                .email("exmaple@email.com")
                .birthday(LocalDate.of(2023, 1, 1))
                .build();
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void blankLoginShouldFailValidation() {
        User user = User.builder()
                .id(ID)
                .name("Name")
                .login("Lo gin")
                .email("exmaple@email.com")
                .birthday(LocalDate.of(2023, 1, 1))
                .build();
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void blankNameShouldBeReplacedWithLogin() {
        User user = User.builder()
                .id(ID)
                .login("Login")
                .email("exmaple@email.com")
                .birthday(LocalDate.of(2023, 1, 1))
                .build();
        assertEquals(user.getName(), user.getLogin());
    }

    @Test
    public void birthdayInFutureShouldFailValidation() {
        User user = User.builder()
                .id(ID)
                .name("Name")
                .login("Login")
                .email("exmaple@email.com")
                .birthday(LocalDate.of(2100, 1, 1))
                .build();
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertFalse(violations.isEmpty());
    }
}
