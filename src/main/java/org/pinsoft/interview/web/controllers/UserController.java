package org.pinsoft.interview.web.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.modelmapper.ModelMapper;
import org.pinsoft.interview.domain.dto.user.*;
import org.pinsoft.interview.service.UserService;
import org.pinsoft.interview.utils.responseHandler.exceptions.BadRequestException;
import org.pinsoft.interview.utils.responseHandler.exceptions.CustomException;
import org.pinsoft.interview.utils.responseHandler.successResponse.SuccessResponse;
import org.pinsoft.interview.utils.validations.serviceValidation.services.UserValidationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static org.pinsoft.interview.utils.constants.ResponseMessageConstants.*;

@RestController
@RequestMapping(value = "/users")
public class UserController {
    private static final String SERVER_ERROR_MESSAGE = "Server Error";

    private final UserService userService;
    private final ModelMapper modelMapper;
    private final ObjectMapper objectMapper;
    private final UserValidationService userValidationService;

    @Autowired
    public UserController(UserService userService, ModelMapper modelMapper, ObjectMapper objectMapper, UserValidationService userValidationService) {
        this.userService = userService;
        this.modelMapper = modelMapper;
        this.objectMapper = objectMapper;
        this.userValidationService = userValidationService;
    }

    @PostMapping(value = "/register", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> registerUser(@RequestBody @Valid UserRegisterBindingModel userRegisterBindingModel) throws Exception {

        if (!userValidationService.isValid(userRegisterBindingModel.getPassword(), userRegisterBindingModel.getConfirmPassword())) {
            throw new BadRequestException(PASSWORDS_MISMATCH_ERROR_MESSAGE);
        }

        if (!userValidationService.isValid(userRegisterBindingModel)) {
            throw new Exception(SERVER_ERROR_MESSAGE);
        }

        UserServiceModel user = modelMapper.map(userRegisterBindingModel, UserServiceModel.class);
        UserCreateViewModel savedUser = this.userService.createUser(user);

        SuccessResponse successResponse = successResponseBuilder(LocalDateTime.now(), SUCCESSFUL_REGISTER_MESSAGE, savedUser);

        return new ResponseEntity<>(this.objectMapper.writeValueAsString(successResponse), HttpStatus.OK);
    }

    @GetMapping(value = "/all/{id}")
    public List<UserAllViewModel> getAllUsers(@PathVariable(value = "id") String userId) throws Exception {
        List<UserServiceModel> allUsers = this.userService.getAllUsers(userId);

        return allUsers.stream()
                .map(x -> this.modelMapper.map(x, UserAllViewModel.class))
                .collect(Collectors.toList());
    }

    @GetMapping(value = "/details/{id}")
    public ResponseEntity<String> getDetails(@PathVariable String id) throws Exception {
        UserDetailsViewModel user = this.userService.getById(id);

        return new ResponseEntity<>(this.objectMapper.writeValueAsString(user), HttpStatus.OK);
    }

    @PutMapping(value = "/update/{id}")
    public ResponseEntity<String> updateUser(@RequestBody @Valid UserUpdateBindingModel userUpdateBindingModel,
                                     @PathVariable(value = "id") String loggedInUserId) throws Exception {

        if(!userValidationService.isValid(userUpdateBindingModel)){
            throw new Exception(SERVER_ERROR_MESSAGE);
        }

        UserServiceModel userServiceModel = this.modelMapper.map(userUpdateBindingModel, UserServiceModel.class);
        boolean result = this.userService.updateUser(userServiceModel, loggedInUserId);

        if (result) {
            SuccessResponse successResponse = successResponseBuilder(LocalDateTime.now(), SUCCESSFUL_USER_PROFILE_EDIT_MESSAGE, "");
            return new ResponseEntity<>(this.objectMapper.writeValueAsString(successResponse), HttpStatus.OK);
        }

        throw new CustomException(SERVER_ERROR_MESSAGE);
    }

    @DeleteMapping(value = "/delete/{id}")
    public ResponseEntity<Object> deleteUser(@PathVariable String id) throws Exception {
            boolean result = this.userService.deleteUserById(id);

            if(result){
                SuccessResponse successResponse = successResponseBuilder(LocalDateTime.now(), SUCCESSFUL_USER_DELETE_MESSAGE, "");
                return new ResponseEntity<>(this.objectMapper.writeValueAsString(successResponse), HttpStatus.OK);
            }

            throw new CustomException(SERVER_ERROR_MESSAGE);
    }


    private SuccessResponse successResponseBuilder(LocalDateTime timestamp, String message, Object payload) {
        return new SuccessResponse(timestamp, message, payload, true);
    }
}
