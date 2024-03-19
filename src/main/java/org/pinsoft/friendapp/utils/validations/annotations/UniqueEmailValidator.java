package org.pinsoft.friendapp.utils.validations.annotations;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.pinsoft.friendapp.domain.repo.entity.UserEntity;
import org.pinsoft.friendapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UniqueEmailValidator implements ConstraintValidator<UniqueEmail,String> {

    @Autowired
    private UserService userService;

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        UserEntity user = this.userService.getByEmailValidation(s);
        return user == null;
    }
}

