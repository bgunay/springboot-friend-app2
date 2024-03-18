package org.pinsoft.interview.utils.validations.annotations;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

@Component
public class PasswordValidator implements ConstraintValidator<Password, String> {
    private int minLength;
    private int maxLength;
    private boolean containsOnlyLettersAndDigits;

    @Override
    public void initialize(Password password) {
        this.minLength = password.minLength();
        this.maxLength = password.maxLength();
        this.containsOnlyLettersAndDigits = password.containsOnlyLettersAndDigits();
    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        String regex = "^[a-zA-Z0-9]+$";

        if (s.length() < this.minLength || s.length() > this.maxLength) {
            return false;
        }

        if (!Pattern.matches(regex, s) && this.containsOnlyLettersAndDigits) {
            return false;
        }
        return true;
    }
}
