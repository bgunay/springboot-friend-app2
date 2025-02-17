package org.pinsoft.friendapp.utils.validations.annotations;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import org.springframework.stereotype.Component;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Component
@Constraint(validatedBy = PasswordValidator.class)
@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Password {
    String message() default "Invalid password format.";
    int minLength() default 8;
    int maxLength() default 30;
    boolean containsOnlyLettersAndDigits() default false;

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload()default {};
}
