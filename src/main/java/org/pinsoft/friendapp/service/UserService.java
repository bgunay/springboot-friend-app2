package org.pinsoft.friendapp.service;

import org.pinsoft.friendapp.domain.dto.user.UserCreateViewModel;
import org.pinsoft.friendapp.domain.dto.user.UserDetailsViewModel;
import org.pinsoft.friendapp.domain.dto.user.UserEditViewModel;
import org.pinsoft.friendapp.domain.dto.user.UserServiceModel;
import org.pinsoft.friendapp.domain.repo.entity.UserEntity;

import java.util.List;

public interface UserService  {
    UserCreateViewModel createUser(UserServiceModel userRegisterBindingModel);

    boolean updateUser(UserServiceModel userUpdateBindingModel, String loggedInUserId) throws Exception;

    UserServiceModel updateUserOnlineStatus(String userName, boolean changeToOnline) throws Exception;

    UserDetailsViewModel getById(String id) throws Exception;

    UserEditViewModel editById(String id) throws Exception;

    UserEntity getByEmailValidation(String email);

    UserEntity getByUsernameValidation(String username);

    List<UserServiceModel> getAllUsers(String userId) throws Exception;

    boolean deleteUserById(String id) throws Exception;

}
