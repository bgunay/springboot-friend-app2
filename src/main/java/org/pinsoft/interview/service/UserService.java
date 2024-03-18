package org.pinsoft.interview.service;

import org.pinsoft.interview.domain.dto.user.UserCreateViewModel;
import org.pinsoft.interview.domain.dto.user.UserDetailsViewModel;
import org.pinsoft.interview.domain.dto.user.UserEditViewModel;
import org.pinsoft.interview.domain.dto.user.UserServiceModel;
import org.pinsoft.interview.domain.repo.entity.User;

import java.util.List;

public interface UserService  {
    UserCreateViewModel createUser(UserServiceModel userRegisterBindingModel);

    boolean updateUser(UserServiceModel userUpdateBindingModel, String loggedInUserId) throws Exception;

    UserServiceModel updateUserOnlineStatus(String userName, boolean changeToOnline) throws Exception;

    UserDetailsViewModel getById(String id) throws Exception;

    UserEditViewModel editById(String id) throws Exception;

    User getByEmailValidation(String email);

    User getByUsernameValidation(String username);

    List<UserServiceModel> getAllUsers(String userId) throws Exception;

    boolean deleteUserById(String id) throws Exception;
}
