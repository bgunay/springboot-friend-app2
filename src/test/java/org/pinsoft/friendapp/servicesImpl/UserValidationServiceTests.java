package org.pinsoft.friendapp.servicesImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.pinsoft.friendapp.domain.dto.user.UserRegisterBindingModel;
import org.pinsoft.friendapp.domain.dto.user.UserServiceModel;
import org.pinsoft.friendapp.domain.dto.user.UserUpdateBindingModel;
import org.pinsoft.friendapp.domain.repo.entity.UserEntity;
import org.pinsoft.friendapp.testUtils.UsersUtils;
import org.pinsoft.friendapp.utils.validations.serviceValidation.services.UserValidationService;
import org.pinsoft.friendapp.utils.validations.serviceValidation.servicesImpl.UserValidationServiceImpl;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class UserValidationServiceTests {
    private UserValidationService userValidationService;

    @BeforeEach
    public void setupTest() {
        userValidationService = new UserValidationServiceImpl();
    }

    @Test
    public void isValidWithUser_whenValid_true() {
        UserEntity user = UsersUtils.createUser();
        boolean result = userValidationService.isValid(user);
        assertTrue(result);
    }

    @Test
    public void isValidWithUser_whenNull_false() {
        UserEntity user = null;
        boolean result = userValidationService.isValid(user);
        assertFalse(result);
    }

    @Test
    public void isValidWithUserServiceModel_whenValid_true() {
        UserServiceModel userServiceModel = UsersUtils.getUserServiceModels(1).get(0);
        boolean result = userValidationService.isValid(userServiceModel);
        assertTrue(result);
    }

    @Test
    public void isValidWithUserServiceModel_whenNull_false() {
        UserServiceModel userServiceModel = null;
        boolean result = userValidationService.isValid(userServiceModel);
        assertFalse(result);
    }

    @Test
    public void isValidWithUserRegisterBindingModel_whenValid_true() {
        UserRegisterBindingModel userRegisterBindingModel = UsersUtils.getUserRegisterBindingModel();
        boolean result = userValidationService.isValid(userRegisterBindingModel);
        assertTrue(result);
    }

    @Test
    public void isValidWithUserRegisterBindingModel_whenNull_false() {
        UserRegisterBindingModel userRegisterBindingModel = null;
        boolean result = userValidationService.isValid(userRegisterBindingModel);
        assertFalse(result);
    }

    @Test
    public void isValidWithUserRegisterBindingModel_whenPasswordsDontMatch_false() {
        UserRegisterBindingModel userRegisterBindingModel = UsersUtils.getUserRegisterBindingModel();
        userRegisterBindingModel.setConfirmPassword("wrong_password");
        boolean result = userValidationService.isValid(userRegisterBindingModel);
        assertFalse(result);
    }

    @Test
    public void isValidWithPasswordAndConfirmPassword_whenPasswordsMatch_true() {
        String password = "1111";
        String confirmPassword = "1111";
        boolean result = userValidationService.isValid(password, confirmPassword);
        assertTrue(result);
    }

    @Test
    public void isValidWithPasswordAndConfirmPassword_whenPasswordsDontMatch_false() {
        String password = "1111";
        String confirmPassword = "wrong_password";
        boolean result = userValidationService.isValid(password, confirmPassword);
        assertFalse(result);
    }

    @Test
    public void isValidWithUserUpdateBindingModel_whenValid_true() {
        UserUpdateBindingModel userUpdateBindingModel = UsersUtils.getUserUpdateBindingModel();
        boolean result = userValidationService.isValid(userUpdateBindingModel);
        assertTrue(result);
    }

    @Test
    public void isValidWithUserUpdateBindingModel_whenNull_false() {
        UserUpdateBindingModel userUpdateBindingModel = null;
        boolean result = userValidationService.isValid(userUpdateBindingModel);
        assertFalse(result);
    }

    @Test
    public void isValidWithUserDetails_whenValid_true() {
        UserEntity user = UsersUtils.createUser();
        boolean result = userValidationService.isValid(user);
        assertTrue(result);
    }

    @Test
    public void isValidWithUserDetails_whenNull_false() {
        UserEntity user = null;
        boolean result = userValidationService.isValid(user);
        assertFalse(result);
    }
}
