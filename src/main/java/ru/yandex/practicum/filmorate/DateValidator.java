package ru.yandex.practicum.filmorate;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;
import java.time.Month;

public class DateValidator implements ConstraintValidator<DateValidation, LocalDate> {

    private static final LocalDate MIN_VALID_DATE = LocalDate.of(1895, Month.DECEMBER, 28);

    @Override
    public boolean isValid(LocalDate value, ConstraintValidatorContext context) {
        if (value == null) {
            return false;
        }
        return !value.isBefore(MIN_VALID_DATE);
    }
}
