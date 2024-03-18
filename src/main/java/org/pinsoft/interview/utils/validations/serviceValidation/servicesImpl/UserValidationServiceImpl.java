package org.pinsoft.interview.utils.validations.serviceValidation.servicesImpl;

import io.micrometer.common.util.StringUtils;
import org.pinsoft.interview.domain.dto.user.UserRegisterBindingModel;
import org.pinsoft.interview.domain.dto.user.UserServiceModel;
import org.pinsoft.interview.domain.dto.user.UserUpdateBindingModel;
import org.pinsoft.interview.domain.repo.entity.User;
import org.pinsoft.interview.utils.validations.serviceValidation.services.UserValidationService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class UserValidationServiceImpl implements UserValidationService {

    @Override
    public boolean isValid(User user) {
        return user != null;
    }

    @Override
    public boolean isValid(UserServiceModel userServiceModel) {
        return userServiceModel != null;
    }

    @Override
    public boolean isValid(UserRegisterBindingModel userRegisterBindingModel) {
        return userRegisterBindingModel != null && isValid(userRegisterBindingModel.getPassword(), userRegisterBindingModel.getConfirmPassword());
    }

    @Override
    public boolean isValid(String password) {
        return StringUtils.isNotBlank(password);
    }
    @Override
    public boolean isValid(String firstParam, String secondParam) {
        return firstParam.equals(secondParam);
    }

    @Override
    public boolean isValid(UserUpdateBindingModel userUpdateBindingModel) {
        return userUpdateBindingModel != null;
    }

    @Override
    public boolean isValid(UserDetails userData) {
        return userData != null;
    }

}
