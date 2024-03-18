package org.pinsoft.interview.utils.validations.serviceValidation.services;

import org.pinsoft.interview.domain.dto.user.UserRegisterBindingModel;
import org.pinsoft.interview.domain.dto.user.UserServiceModel;
import org.pinsoft.interview.domain.dto.user.UserUpdateBindingModel;
import org.pinsoft.interview.domain.repo.entity.User;
import org.springframework.security.core.userdetails.UserDetails;

public interface UserValidationService {
    boolean isValid(User user);

    boolean isValid(UserServiceModel userServiceModel);

    boolean isValid(UserRegisterBindingModel userRegisterBindingModel);

    boolean isValid(String firstParam, String secondParam);

    boolean isValid(UserUpdateBindingModel userUpdateBindingModel);

    boolean isValid(UserDetails userData);
    public boolean isValid(String password);
}
