package org.pinsoft.friendapp.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.pinsoft.friendapp.domain.dto.user.*;
import org.pinsoft.friendapp.service.UserService;
import org.pinsoft.friendapp.utils.responseHandler.exceptions.BadRequestException;
import org.pinsoft.friendapp.utils.responseHandler.exceptions.CustomException;
import org.pinsoft.friendapp.utils.responseHandler.successResponse.SuccessResponse;
import org.pinsoft.friendapp.utils.validations.serviceValidation.services.UserValidationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static org.pinsoft.friendapp.utils.constants.ResponseMessageConstants.*;

@RestController
@RequestMapping(value = "/users")
@Tag(name = "users", description = "users")
@RequiredArgsConstructor
public class UserController {
    private static final String SERVER_ERROR_description = "Server Error";

    private final UserService userService;
    private final ModelMapper modelMapper;
    private final ObjectMapper objectMapper;
    private final UserValidationService userValidationService;


    @PostMapping(value = "/register", consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            summary = "register new user",
            description = "register new user."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "ok"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<String> registerUser(@RequestBody @Valid UserRegisterBindingModel userRegisterBindingModel) throws Exception {

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
    @Operation(
            summary = "get all users in system",
            description = "get all users in system."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode  = "200", description = "ok"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public List<UserAllViewModel> getAllUsers(@PathVariable(value = "id") String userId) throws Exception {
        List<UserServiceModel> allUsers = this.userService.getAllUsers(userId);

        return allUsers.stream()
                .map(x -> this.modelMapper.map(x, UserAllViewModel.class))
                .collect(Collectors.toList());
    }

    @GetMapping(value = "/details/{id}")
    @Operation(
            summary = "get user detail",
            description = "get user detail."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "ok"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<String> getDetails(@PathVariable String id) throws Exception {
        UserDetailsViewModel user = this.userService.getById(id);

        return new ResponseEntity<>(this.objectMapper.writeValueAsString(user), HttpStatus.OK);
    }

    @PutMapping(value = "/update/{id}")
    @Operation(
            summary = "update user",
            description = "update user."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "ok"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
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
    @Operation(
            summary = "delete user with id ",
            description = "delete user with id "
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "ok"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
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
