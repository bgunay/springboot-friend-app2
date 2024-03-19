package org.pinsoft.friendapp.utils.validations.annotations;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.pinsoft.friendapp.domain.dto.user.UserRegisterBindingModel;
import org.springframework.stereotype.Component;

@Component
public class PasswordMatchingValidator implements ConstraintValidator<PasswordMatching, Object> {
    @Override
    public void initialize(PasswordMatching constraintAnnotation) {

    }

    @Override
    public boolean isValid(Object o, ConstraintValidatorContext constraintValidatorContext) {
        if (o instanceof UserRegisterBindingModel) {
            UserRegisterBindingModel user = (UserRegisterBindingModel) o;
            return user.getPassword().equals(user.getConfirmPassword());
        }
        return false;
    }
}
