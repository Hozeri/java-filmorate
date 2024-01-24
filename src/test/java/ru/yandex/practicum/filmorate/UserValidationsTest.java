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

import static org.junit.jupiter.api.Assertions.assertFalse;

public class UserValidationsTest {

    private Validator validator;
    public static final int ID = 1;
    private User user;

    @BeforeEach
    public void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
        user = new User();
        user.setId(ID);
    }

    @Test
    public void emptyEmailShouldFailValidation() {
        user.setName("Name");
        user.setLogin("Login");
        user.setBirthday(LocalDate.of(2023, 1, 1));
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void emailWithoutAtShouldFailValidation() {
        user.setName("Name");
        user.setLogin("Login");
        user.setEmail("example_email.com");
        user.setBirthday(LocalDate.of(2023, 1, 1));
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void emptyLoginShouldFailValidation() {
        user.setName("Name");
        user.setEmail("example@email.com");
        user.setBirthday(LocalDate.of(2023, 1, 1));
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void blankLoginShouldFailValidation() {
        user.setName("Name");
        user.setLogin("Lo gin");
        user.setEmail("example_email.com");
        user.setBirthday(LocalDate.of(2023, 1, 1));
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void birthdayInFutureShouldFailValidation() {
        user.setName("Name");
        user.setLogin("Login");
        user.setEmail("example@email.com");
        user.setBirthday(LocalDate.of(2100, 1, 1));;
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertFalse(violations.isEmpty());
    }
}
