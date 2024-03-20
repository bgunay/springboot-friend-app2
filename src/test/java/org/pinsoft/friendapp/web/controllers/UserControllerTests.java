package org.pinsoft.friendapp.web.controllers;

import jakarta.servlet.ServletContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.pinsoft.friendapp.domain.dto.user.*;
import org.pinsoft.friendapp.domain.repo.entity.UserEntity;
import org.pinsoft.friendapp.controller.UserController;
import org.pinsoft.friendapp.service.UserService;
import org.pinsoft.friendapp.testUtils.TestUtil;
import org.pinsoft.friendapp.testUtils.UsersUtils;
import org.pinsoft.friendapp.utils.responseHandler.exceptions.BadRequestException;
import org.pinsoft.friendapp.utils.responseHandler.exceptions.CustomException;
import org.pinsoft.friendapp.utils.validations.serviceValidation.services.UserValidationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockServletContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.pinsoft.friendapp.utils.constants.ResponseMessageConstants.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ContextConfiguration
@WebAppConfiguration
@SpringBootTest
public class UserControllerTests {
    private MockMvc mvc;

    @Autowired
    private WebApplicationContext context;

    @MockBean
    private UserService mockUserService;

    @MockBean
    private UserValidationService mockUserValidation;

    @BeforeEach
    public void setup() {
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .alwaysDo(print())
                .build();
    }

    @Test
    public void givenWac_whenServletContext_thenItProvidesPostController() {
        ServletContext servletContext = context.getServletContext();

        assertNotNull(servletContext);
        assertTrue(servletContext instanceof MockServletContext);
        assertNotNull(context.getBean("userController"));
    }

    @Test
    public void getAllUsers_when2Users_2Users() throws Exception {
        List<UserEntity> users = UsersUtils.getUsers(2);
        List<UserServiceModel> userServiceModels = UsersUtils.getUserServiceModels(2);

        when(this.mockUserService.getAllUsers("1"))
                .thenReturn(userServiceModels);

        this.mvc
                .perform(get("/users/all/{id}", "1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().contentTypeCompatibleWith("application/json"))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is("1")))
                .andExpect(jsonPath("$[0].username", is("ali 0")))
                .andExpect(jsonPath("$[1].id", is("2")))
                .andExpect(jsonPath("$[1].username", is("ali 1")));

        verify(this.mockUserService, times(1)).getAllUsers("1");
        verifyNoMoreInteractions(this.mockUserService);
    }

    @Test()
    public void getAllUsers_whenGetAllUsersThrowsException_throwCustomException() throws Exception {
        when(this.mockUserService.getAllUsers("1"))
                .thenThrow(new CustomException(SERVER_ERROR_MESSAGE));

        Exception resolvedException = this.mvc
                .perform(get("/users/all/{id}", "1"))
                .andExpect(status().isInternalServerError())
                .andReturn().getResolvedException();

        assertEquals(SERVER_ERROR_MESSAGE, resolvedException.getMessage());
        assertEquals(CustomException.class, resolvedException.getClass());

        verify(mockUserService, times(1)).getAllUsers("1");
        verifyNoMoreInteractions(mockUserService);
    }

    @Test
    public void registerUser_whenInputsAreValid_registerUser() throws Exception {
        UserCreateViewModel userCreateViewModel = UsersUtils.getUserCreateViewModel();
        UserRegisterBindingModel userRegisterBindingModel = UsersUtils.getUserRegisterBindingModel();

        when(mockUserValidation.isValid(anyString(), anyString())).thenReturn(true);
        when(mockUserValidation.isValid(any(UserRegisterBindingModel.class))).thenReturn(true);

        when(mockUserService.createUser(any(UserServiceModel.class)))
                .thenReturn(userCreateViewModel);

        this.mvc
                .perform(post("/users/register")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(TestUtil.convertObjectToJsonString(userRegisterBindingModel)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestUtil.TEXT_PLAIN_UTF8))
                .andExpect(jsonPath("$.success").value("true"))
                .andExpect(jsonPath("$.message").value(SUCCESSFUL_REGISTER_MESSAGE))
                .andExpect(jsonPath("$.payload.id").value(userCreateViewModel.getId()))
                .andExpect(jsonPath("$.payload.username").value(userCreateViewModel.getUsername()))
                .andExpect(jsonPath("$.payload.email").value(userCreateViewModel.getEmail()))
                .andExpect(jsonPath("$.payload.address").value(userCreateViewModel.getAddress()))
                .andExpect(jsonPath("$.payload.firstName").value(userCreateViewModel.getFirstName()))
                .andExpect(jsonPath("$.payload.lastName").value(userCreateViewModel.getLastName()));

        verify(mockUserService).createUser(any(UserServiceModel.class));
        verify(mockUserService, times(1)).createUser(any());
    }

    @Test
    public void registerUser_whenPasswordsDontMatch_throwException() throws Exception {
        UserCreateViewModel userCreateViewModel = UsersUtils.getUserCreateViewModel();
        UserRegisterBindingModel userRegisterBindingModel = UsersUtils.getUserRegisterBindingModel();

        when(mockUserValidation.isValid(anyString(), anyString())).thenReturn(false);

        when(mockUserValidation.isValid(any(UserRegisterBindingModel.class))).thenReturn(true);

        when(mockUserService.createUser(any(UserServiceModel.class)))
                .thenReturn(userCreateViewModel);

        Exception resolvedException = this.mvc
                .perform(post("/users/register")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(TestUtil.convertObjectToJsonString(userRegisterBindingModel)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andReturn().getResolvedException();

        assertEquals(PASSWORDS_MISMATCH_ERROR_MESSAGE, resolvedException.getMessage());
        assertEquals(BadRequestException.class, resolvedException.getClass());
    }

    @Test
    public void registerUser_whenUserRegisterBindingModelIsNotValid_throwException() throws Exception {
        UserCreateViewModel userCreateViewModel = UsersUtils.getUserCreateViewModel();
        UserRegisterBindingModel userRegisterBindingModel = UsersUtils.getUserRegisterBindingModel();

        when(mockUserValidation.isValid(anyString(), anyString())).thenReturn(true);

        when(mockUserValidation.isValid(any(UserRegisterBindingModel.class))).thenReturn(false);

        when(mockUserService.createUser(any(UserServiceModel.class)))
                .thenReturn(userCreateViewModel);

        Exception resolvedException = this.mvc
                .perform(post("/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtil.convertObjectToJsonString(userRegisterBindingModel)))
                .andDo(print())
                .andExpect(status().isInternalServerError())
                .andReturn().getResolvedException();

        assertEquals(SERVER_ERROR_MESSAGE, resolvedException.getMessage());
        assertEquals(Exception.class, resolvedException.getClass());
    }

    @Test
    public void getDetails_whenIdIsValid_returnUser() throws Exception {
        UserDetailsViewModel userDetailsViewModel = UsersUtils.getUserDetailsViewModel();

        when(this.mockUserService.getById("1"))
                .thenReturn(userDetailsViewModel);

        this.mvc
                .perform(get("/users/details/{id}", "1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestUtil.TEXT_PLAIN_UTF8))
                .andExpect(jsonPath("$.id").value(userDetailsViewModel.getId()))
                .andExpect(jsonPath("$.username").value(userDetailsViewModel.getUsername()))
                .andExpect(jsonPath("$.email").value(userDetailsViewModel.getEmail()))
                .andExpect(jsonPath("$.address").value(userDetailsViewModel.getAddress()))
                .andExpect(jsonPath("$.firstName").value(userDetailsViewModel.getFirstName()))
                .andExpect(jsonPath("$.lastName").value(userDetailsViewModel.getLastName()));

        verify(this.mockUserService, times(1)).getById("1");
        verifyNoMoreInteractions(this.mockUserService);
    }

    @Test
    public void getDetails_whenIdIsNotValid_throwException() throws Exception {
        when(this.mockUserService.getById(anyString()))
                .thenThrow(new Exception());

        this.mvc
                .perform(get("/users/details/{id}", "invalid_Id"))
                .andDo(print())
                .andExpect(status().isInternalServerError());


        verify(this.mockUserService, times(1)).getById(anyString());
        verifyNoMoreInteractions(this.mockUserService);
    }

    @Test
    public void updateUser_whenInputsAreValid_returnUser() throws Exception {
        UserUpdateBindingModel userUpdateBindingModel = UsersUtils.getUserUpdateBindingModel();

        when(mockUserValidation.isValid(any(UserUpdateBindingModel.class))).thenReturn(true);

        when(this.mockUserService.updateUser(any(UserServiceModel.class), anyString()))
                .thenReturn(true);

        this.mvc
                .perform(put("/users/update/{id}", "1")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(TestUtil.convertObjectToJsonString(userUpdateBindingModel)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestUtil.TEXT_PLAIN_UTF8))
                .andExpect(jsonPath("$.success").value("true"))
                .andExpect(jsonPath("$.message").value(SUCCESSFUL_USER_PROFILE_EDIT_MESSAGE));

        verify(this.mockUserService, times(1)).updateUser(any(UserServiceModel.class), anyString());
        verifyNoMoreInteractions(this.mockUserService);
    }

    @Test
    public void updateUser_whenUserUpdateBindingModelIsNotValid_throwException() throws Exception {
        UserUpdateBindingModel userUpdateBindingModel = UsersUtils.getUserUpdateBindingModel();

        when(mockUserValidation.isValid(any(UserUpdateBindingModel.class))).thenReturn(false);

        when(this.mockUserService.updateUser(any(UserServiceModel.class), anyString()))
                .thenReturn(true);

        Exception resolvedException = this.mvc
                .perform(put("/users/update/{id}", "1")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(TestUtil.convertObjectToJsonString(userUpdateBindingModel)))
                .andDo(print())
                .andExpect(status().isInternalServerError())
                .andReturn().getResolvedException();

        assertEquals(SERVER_ERROR_MESSAGE, resolvedException.getMessage());
        assertEquals(Exception.class, resolvedException.getClass());
    }

    @Test
    public void updateUser_whenUpdateUserReturnsFalse_throwException() throws Exception {
        UserUpdateBindingModel userUpdateBindingModel = UsersUtils.getUserUpdateBindingModel();

        when(mockUserValidation.isValid(any(UserUpdateBindingModel.class))).thenReturn(true);

        when(this.mockUserService.updateUser(any(UserServiceModel.class), anyString()))
                .thenReturn(false);

        Exception resolvedException = this.mvc
                .perform(put("/users/update/{id}", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtil.convertObjectToJsonString(userUpdateBindingModel)))
                .andDo(print())
                .andExpect(status().isInternalServerError())
                .andReturn().getResolvedException();

        assertEquals(SERVER_ERROR_MESSAGE, resolvedException.getMessage());
        assertEquals(CustomException.class, resolvedException.getClass());
    }


    @Test
    public void deleteUser_whenDeleteUserByIdReturnsTrue_deleteUser() throws Exception {
        when(mockUserService.deleteUserById(anyString()))
                .thenReturn(true);

        this.mvc
                .perform(delete("/users/delete/{id}", "1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestUtil.TEXT_PLAIN_UTF8))
                .andExpect(jsonPath("$.success").value("true"))
                .andExpect(jsonPath("$.message").value(SUCCESSFUL_USER_DELETE_MESSAGE));

        verify(mockUserService, times(1)).deleteUserById(anyString());
        verifyNoMoreInteractions(mockUserService);
    }

    @Test
    public void deleteUser_whenDeleteUserByIdReturnsFalse_throwException() throws Exception {
        when(mockUserService.deleteUserById(anyString()))
                .thenReturn(false);

        Exception resolvedException = this.mvc
                .perform(delete("/users/delete/{id}", "1"))
                .andDo(print())
                .andExpect(status().isInternalServerError())
                .andReturn().getResolvedException();

        assertEquals(SERVER_ERROR_MESSAGE, resolvedException.getMessage());
        assertEquals(CustomException.class, resolvedException.getClass());

        verify(mockUserService, times(1)).deleteUserById(anyString());
        verifyNoMoreInteractions(mockUserService);
    }


}
