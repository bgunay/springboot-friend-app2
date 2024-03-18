package org.pinsoft.interview.service.impl;

import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.pinsoft.interview.domain.dto.user.UserCreateViewModel;
import org.pinsoft.interview.domain.dto.user.UserServiceModel;
import org.pinsoft.interview.domain.repo.UserRepository;
import org.pinsoft.interview.domain.repo.entity.User;
import org.pinsoft.interview.service.LoggerService;
import org.pinsoft.interview.service.UserService;
import org.pinsoft.interview.utils.responseHandler.exceptions.CustomException;
import org.pinsoft.interview.utils.validations.serviceValidation.services.UserValidationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static kl.socialnetwork.utils.constants.ResponseMessageConstants.*;
import static org.pinsoft.interview.utils.constants.ResponseMessageConstants.SERVER_ERROR_MESSAGE;
import static org.pinsoft.interview.utils.constants.ResponseMessageConstants.UNAUTHORIZED_SERVER_ERROR_MESSAGE;

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
    public void isOnlineSetup(){
        if(this.userRepository.count() > 0){
            this.userRepository.setIsOnlineToFalse();
        }
    }

    @Override
    public UserCreateViewModel createUser(UserServiceModel userServiceModel) {
        this.loggerService.createLog("POST", userServiceModel.getUsername(), "users", "register");

        if (!this.userValidation.isValid(userServiceModel)) {
            throw new CustomException(SERVER_ERROR_MESSAGE);
        }

        User userEntity = this.modelMapper.map(userServiceModel, User.class);

        userEntity.setPassword(this.bCryptPasswordEncoder
                .encode(userEntity.getPassword()));


        User user = this.userRepository.saveAndFlush(userEntity);
        UserCreateViewModel userModel = this.modelMapper.map(user, UserCreateViewModel.class);
        return userModel;
    }

    @Override
    public boolean updateUser(UserServiceModel userServiceModel, String loggedInUserId) throws Exception {
        if (!userValidation.isValid(userServiceModel)) {
            throw new Exception(SERVER_ERROR_MESSAGE);
        }

        User userToEdit = this.userRepository.findById(userServiceModel.getId()).orElse(null);
        User loggedInUser = this.userRepository.findById(loggedInUserId).orElse(null);

        if (!userValidation.isValid(userToEdit) || !userValidation.isValid(loggedInUser)) {
            throw new Exception(SERVER_ERROR_MESSAGE);
        }

        if (!userServiceModel.getId().equals(loggedInUserId)) {
            String userAuthority = this.getUserAuthority(loggedInUserId);
            if (!("ROOT").equals(userAuthority) && !("ADMIN").equals(userAuthority)) {
                throw new CustomException(UNAUTHORIZED_SERVER_ERROR_MESSAGE);
            }
        }

        User userEntity = this.modelMapper.map(userServiceModel, User.class);
        userEntity.setPassword(userToEdit.getPassword());

        return this.userRepository.save(userEntity) != null;
    }

    @Override
    public UserServiceModel updateUserOnlineStatus(String userName, boolean changeToOnline) throws Exception {
        User user = this.userRepository.findByUsername(userName)
                .filter(userValidation::isValid)
                .orElseThrow(() -> new CustomException(SERVER_ERROR_MESSAGE));

        if(changeToOnline){
            user.setOnline(true);
        }else {
            user.setOnline(false);
        }

        User updatedUser = this.userRepository.save(user);

        if (updatedUser != null) {
            return this.modelMapper.map(updatedUser, UserServiceModel.class);
        }

        throw new CustomException(SERVER_ERROR_MESSAGE);
    }


    @Override
    public List<UserServiceModel> getAllUsers(String userId) throws Exception {
        User userById = this.userRepository.findById(userId).orElse(null);

        if (!userValidation.isValid(userById)) {
            throw new Exception(SERVER_ERROR_MESSAGE);
        }

        List<UserRole> userRoles = this.getUserRoles(userById);

        if (userRoles.size() > 0) {
            return this.userRepository
                    .findAll()
                    .stream()
                    .map(x -> this.modelMapper.map(x, UserServiceModel.class))
                    .collect(Collectors.toList());
        }

        throw new CustomException(UNAUTHORIZED_SERVER_ERROR_MESSAGE);
    }

    @Override
    public UserDetailsViewModel getById(String id) throws Exception {
        User user = this.userRepository.findById(id)
                .filter(userValidation::isValid)
                .orElseThrow(Exception::new);

        return this.modelMapper.map(user, UserDetailsViewModel.class);
    }

    @Override
    public UserEditViewModel editById(String id) throws Exception {
        User user = this.userRepository.findById(id)
                .filter(userValidation::isValid)
                .orElseThrow(Exception::new);

        return this.modelMapper.map(user, UserEditViewModel.class);
    }

    @Override
    public User getByEmailValidation(String email) {
        return this.userRepository.findByEmail(email);
    }

    @Override
    public User getByUsernameValidation(String username) {
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

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return this.userRepository
                .findByUsername(username)
                .filter(userValidation::isValid)
                .orElseThrow(() -> new UsernameNotFoundException(USER_NOT_FOUND_ERROR_MESSAGE));
    }

    @Override
    public boolean promoteUser(String id) throws Exception {
        User user = this.userRepository.findById(id)
                .filter(userValidation::isValid)
                .orElseThrow(Exception::new);

        String userAuthority = this.getUserAuthority(user.getId());

        switch (userAuthority) {
            case "USER":
                user.setAuthorities(this.getAuthorities("ADMIN"));
                break;
            case "ROOT":
                throw new CustomException(USER_FAILURE_CHANGING_ROOT_AUTHORITY_MESSAGE);
            default:
                throw new CustomException(USER_FAILURE_PROMOTING_ADMIN_MESSAGE);
        }

        return this.userRepository.save(user) != null;
    }

    @Override
    public boolean demoteUser(String id) throws Exception {
        User user = this.userRepository.findById(id)
                .filter(userValidation::isValid)
                .orElseThrow(Exception::new);

        String userAuthority = this.getUserAuthority(user.getId());

        switch (userAuthority) {
            case "ADMIN":
                user.setAuthorities(this.getAuthorities("USER"));
                break;
            case "ROOT":
                throw new CustomException(USER_FAILURE_CHANGING_ROOT_AUTHORITY_MESSAGE);
            case "USER":
                throw new CustomException(USER_FAILURE_DEMOTING_USER_MESSAGE);
        }

        return this.userRepository.save(user) != null;
    }

    private List<UserRole> getUserRoles(User userById) {
        return userById
                .getAuthorities()
                .stream().filter(userRole ->
                        userRole.getAuthority().equals("ROOT")
                                || userRole.getAuthority().equals("ADMIN"))
                .collect(Collectors.toList());
    }

    private Set<UserRole> getAuthorities(String authority) {
        Set<UserRole> userAuthorities = new HashSet<>();

        userAuthorities.add(this.roleRepository.getByAuthority(authority));

        return userAuthorities;
    }

    private String getUserAuthority(String userId) {
        return this
                .userRepository
                .findById(userId)
                .get()
                .getAuthorities()
                .stream()
                .findFirst()
                .get()
                .getAuthority();
    }
}
