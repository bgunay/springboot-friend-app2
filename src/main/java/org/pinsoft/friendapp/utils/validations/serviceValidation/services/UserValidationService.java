package org.pinsoft.friendapp.utils.validations.serviceValidation.services;

import org.pinsoft.friendapp.domain.dto.user.UserRegisterBindingModel;
import org.pinsoft.friendapp.domain.dto.user.UserServiceModel;
import org.pinsoft.friendapp.domain.dto.user.UserUpdateBindingModel;
import org.pinsoft.friendapp.domain.repo.entity.UserEntity;

public interface UserValidationService {
    boolean isValid(UserEntity user);

    boolean isValid(UserServiceModel userServiceModel);

    boolean isValid(UserRegisterBindingModel userRegisterBindingModel);

    boolean isValid(String firstParam, String secondParam);

    boolean isValid(UserUpdateBindingModel userUpdateBindingModel);

    public boolean isValid(String password);
}
