package org.pinsoft.friendapp.service.impl;

import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.pinsoft.friendapp.domain.dto.user.UserCreateViewModel;
import org.pinsoft.friendapp.domain.dto.user.UserDetailsViewModel;
import org.pinsoft.friendapp.domain.dto.user.UserEditViewModel;
import org.pinsoft.friendapp.domain.dto.user.UserServiceModel;
import org.pinsoft.friendapp.domain.repo.UserRepository;
import org.pinsoft.friendapp.domain.repo.entity.UserEntity;
import org.pinsoft.friendapp.service.LoggerService;
import org.pinsoft.friendapp.service.UserService;
import org.pinsoft.friendapp.utils.responseHandler.exceptions.CustomException;
import org.pinsoft.friendapp.utils.validations.serviceValidation.services.UserValidationService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static org.pinsoft.friendapp.utils.constants.ResponseMessageConstants.*;

@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final UserValidationService userValidation;
    private final LoggerService loggerService;

    @PostConstruct
    public void isOnlineSetup() {
        if (this.userRepository.count() > 0) {
            this.userRepository.setIsOnlineToFalse();
        }
    }

    @Override
    public UserCreateViewModel createUser(UserServiceModel userServiceModel) {
        this.loggerService.createLog("POST", userServiceModel.getUsername(), "users", "register");

        if (!this.userValidation.isValid(userServiceModel)) {
            throw new CustomException(SERVER_ERROR_MESSAGE);
        }

        UserEntity userEntity = this.modelMapper.map(userServiceModel, UserEntity.class);

        userEntity.setPassword(this.bCryptPasswordEncoder
                .encode(userEntity.getPassword()));


        UserEntity user = this.userRepository.saveAndFlush(userEntity);
        if (user.getId() == null) {
            throw new CustomException(USER_CAN_NOT_SAVED);
        }
        UserCreateViewModel userModel = this.modelMapper.map(user, UserCreateViewModel.class);
        return userModel;
    }

    @Override
    public boolean updateUser(UserServiceModel userServiceModel, String loggedInUserId) throws Exception {
        if (!userValidation.isValid(userServiceModel)) {
            throw new CustomException(USER_SERVICE_MODEL_INVALID);
        }

        UserEntity userToEdit = this.userRepository.findById(userServiceModel.getId()).orElse(null);
        UserEntity loggedInUser = this.userRepository.findById(loggedInUserId).orElse(null);

        if (!userValidation.isValid(userToEdit) || !userValidation.isValid(loggedInUser)) {
            throw new CustomException(USER_ENTITY_MODEL_INVALID);
        }

        UserEntity userEntity = this.modelMapper.map(userServiceModel, UserEntity.class);
        userEntity.setPassword(userToEdit.getPassword());

        this.userRepository.save(userEntity);
        return true;
    }

    @Override
    public UserServiceModel updateUserOnlineStatus(String userName, boolean changeToOnline) throws Exception {
        UserEntity user = this.userRepository.findByUsername(userName)
                .filter(userValidation::isValid)
                .orElseThrow(() -> new CustomException(SERVER_ERROR_MESSAGE));

        if (changeToOnline) {
            user.setOnline(true);
        } else {
            user.setOnline(false);
        }

        UserEntity updatedUser = this.userRepository.save(user);

        if (updatedUser.getId() != null) {
            return this.modelMapper.map(updatedUser, UserServiceModel.class);
        }

        throw new CustomException(SERVER_ERROR_MESSAGE);
    }


    @Override
    public List<UserServiceModel> getAllUsers(String userId) throws Exception {
        UserEntity userById = this.userRepository.findById(userId).orElse(null);

        if (!userValidation.isValid(userById)) {
            throw new Exception(SERVER_ERROR_MESSAGE);
        }


        return this.userRepository
                .findAll()
                .stream()
                .map(x -> this.modelMapper.map(x, UserServiceModel.class))
                .collect(Collectors.toList());
    }

    @Override
    public UserDetailsViewModel getById(String id) throws Exception {
        UserEntity user = this.userRepository.findById(id)
                .filter(userValidation::isValid)
                .orElseThrow(Exception::new);

        return this.modelMapper.map(user, UserDetailsViewModel.class);
    }

    @Override
    public UserEditViewModel editById(String id) throws Exception {
        UserEntity user = this.userRepository.findById(id)
                .filter(userValidation::isValid)
                .orElseThrow(Exception::new);

        return this.modelMapper.map(user, UserEditViewModel.class);
    }

    @Override
    public UserEntity getByEmailValidation(String email) {
        return this.userRepository.findByEmail(email);
    }

    @Override
    public UserEntity getByUsernameValidation(String username) {
        return this.userRepository.findByUsername(username).orElse(null);
    }

    @Override
    public boolean deleteUserById(String id) throws Exception {
        this.userRepository.findById(id)
                .filter(userValidation::isValid)
                .orElseThrow(Exception::new);

        try {
            this.userRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            throw new Exception(SERVER_ERROR_MESSAGE);
        }
    }

}
