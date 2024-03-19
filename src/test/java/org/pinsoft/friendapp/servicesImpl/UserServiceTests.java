package org.pinsoft.friendapp.servicesImpl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.pinsoft.friendapp.domain.dto.user.UserServiceModel;
import org.pinsoft.friendapp.domain.repo.UserRepository;
import org.pinsoft.friendapp.domain.repo.entity.UserEntity;
import org.pinsoft.friendapp.service.UserService;
import org.pinsoft.friendapp.testUtils.UsersUtils;
import org.pinsoft.friendapp.utils.responseHandler.exceptions.CustomException;
import org.pinsoft.friendapp.utils.validations.serviceValidation.services.UserValidationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.pinsoft.friendapp.utils.constants.ResponseMessageConstants.SERVER_ERROR_MESSAGE;
import static org.pinsoft.friendapp.utils.constants.ResponseMessageConstants.UNAUTHORIZED_SERVER_ERROR_MESSAGE;

@ExtendWith(MockitoExtension.class)
public class UserServiceTests {
    @Autowired
    private UserService userService;

    @MockBean
    private UserRepository mockUserRepository;


    @MockBean
    private UserValidationService mockUserValidationService;


    private List<UserEntity> userList;

    @BeforeEach
    public void setUpTest() {
        userList = new ArrayList<>();
        when(mockUserRepository.findAll())
                .thenReturn(userList);
    }

    @Test
    public void getAllUsers_whenUsersHasRootOrAdminRole2Users_2Users() throws Exception {
        // Arrange
        List<UserEntity> users = UsersUtils.getUsers(2);

        userList.addAll(users);

        when(mockUserRepository.findById(anyString()))
                .thenReturn(java.util.Optional.of(users.get(0)));

        when(mockUserValidationService.isValid(any(UserEntity.class)))
                .thenReturn(true);

        // Act
        List<UserServiceModel> allUsers = userService.getAllUsers("1");

        // Assert
        UserEntity expected = userList.get(0);
        UserServiceModel actual = allUsers.get(0);

        assertEquals(2, allUsers.size());
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getFirstName(), actual.getFirstName());
        assertEquals(expected.getLastName(), actual.getLastName());
        assertEquals(expected.getUsername(), actual.getUsername());
        assertEquals(expected.getEmail(), actual.getEmail());
        assertEquals(expected.getAddress(), actual.getAddress());

        verify(mockUserRepository).findAll();
        verify(mockUserRepository, times(1)).findAll();
    }


    @Test
    public void createUser_whenUserServiceModelIsValidAndThisIsFirstUserInDatabase_createUser() throws Exception {
        // Arrange
        UserServiceModel userServiceModel = UsersUtils.getUserServiceModels(1).get(0);

        UserEntity user = UsersUtils.createUser();

        when(mockUserValidationService.isValid(any(UserServiceModel.class)))
                .thenReturn(true);

        when(mockUserRepository.findAll()).thenReturn(new ArrayList<>());

        when(mockUserRepository.saveAndFlush(any())).thenReturn(user);

        // Act
        userService.createUser(userServiceModel);

        // Assert
        verify(mockUserRepository).saveAndFlush(any());
        verify(mockUserRepository, times(1)).saveAndFlush(any());
    }

    @Test
    public void createUser_whenUserServiceModelIsValidThisIsNotFirstUserInDatabase_createUser() throws Exception {
        // Arrange
        UserServiceModel userServiceModel = UsersUtils.getUserServiceModels(1).get(0);

        UserEntity user = UsersUtils.createUser();

        when(mockUserValidationService.isValid(any(UserServiceModel.class)))
                .thenReturn(true);

        when(mockUserRepository.findAll()).thenReturn(List.of(user));

        when(mockUserRepository.saveAndFlush(any())).thenReturn(user);

        // Act
        userService.createUser(userServiceModel);

        // Assert
        verify(mockUserRepository).saveAndFlush(any());
        verify(mockUserRepository, times(1)).saveAndFlush(any());
    }

    @Test
    public void createUser_whenUserServiceModelIsNotValid_throwException() throws Exception {
        // Arrange
        UserServiceModel userServiceModel = UsersUtils.getUserServiceModels(1).get(0);

        when(mockUserValidationService.isValid(any(UserServiceModel.class)))
                .thenReturn(false);

        CustomException customException = Assertions.assertThrows(CustomException.class, () -> {
            userService.createUser(userServiceModel);
        });
        assertEquals(SERVER_ERROR_MESSAGE, customException.getMessage());

    }

    @Test
    public void createUser_whenSavedUserInDatabaseIsNull_throwException() throws Exception {
        // Arrange
        UserServiceModel userServiceModel = UsersUtils.getUserServiceModels(1).get(0);

        UserEntity user = UsersUtils.createUser();

        when(mockUserValidationService.isValid(any(UserServiceModel.class)))
                .thenReturn(true);

        when(mockUserRepository.findAll()).thenReturn(new ArrayList<>());

        when(mockUserRepository.saveAndFlush(any())).thenReturn(null);

        CustomException customException = Assertions.assertThrows(CustomException.class, () -> {
            userService.createUser(userServiceModel);
        });
        assertEquals(SERVER_ERROR_MESSAGE, customException.getMessage());
    }

    @Test
    public void updateUser_whenUserServiceModelAndLoggedInUserIdAreValid_updateUser() throws Exception {
        // Arrange

        UserServiceModel userServiceModel = UsersUtils.getUserServiceModels(1).get(0);

        List<UserEntity> users = UsersUtils.getUsers(2);

        when(mockUserValidationService.isValid(any(UserServiceModel.class)))
                .thenReturn(true);

        when(mockUserRepository.findById("1"))
                .thenReturn(java.util.Optional.of(users.get(0)));

        when(mockUserRepository.findById("2"))
                .thenReturn(java.util.Optional.of(users.get(1)));

        when(mockUserValidationService.isValid(any(UserEntity.class)))
                .thenReturn(true);

        // Act
        userService.updateUser(userServiceModel, "1");

        // Assert
        verify(mockUserRepository).save(any());
        verify(mockUserRepository, times(1)).save(any());
    }

    @Test
    public void updateUser_whenAllParametersAreValid_updateUser() throws Exception {
        // Arrange
        UserServiceModel userServiceModel = UsersUtils.getUserServiceModels(1).get(0);

        List<UserEntity> users = UsersUtils.getUsers(2);

        when(mockUserValidationService.isValid(any(UserServiceModel.class)))
                .thenReturn(true);

        when(mockUserRepository.findById("1"))
                .thenReturn(java.util.Optional.of(users.get(0)));

        when(mockUserValidationService.isValid(any(UserEntity.class)))
                .thenReturn(true);

        // Act
        userService.updateUser(userServiceModel, "1");

        // Assert
        verify(mockUserRepository).save(any());
        verify(mockUserRepository, times(1)).save(any());
    }

    @Test
    public void updateUser_whenUserServiceModelIsNotValid_throwException() throws Exception {
        // Arrange
        UserServiceModel userServiceModel = UsersUtils.getUserServiceModels(1).get(0);

        when(mockUserValidationService.isValid(any(UserServiceModel.class)))
                .thenReturn(false);

        CustomException customException = Assertions.assertThrows(CustomException.class, () -> {
            userService.updateUser(userServiceModel, "1");
        });
        assertEquals(SERVER_ERROR_MESSAGE, customException.getMessage());
    }

    @Test
    public void updateUser_whenLoggedInUserIdIsNotValid_throwException() throws Exception {
        // Arrange
        UserServiceModel userServiceModel = UsersUtils.getUserServiceModels(1).get(0);

        List<UserEntity> users = UsersUtils.getUsers(2);

        when(mockUserValidationService.isValid(any(UserServiceModel.class)))
                .thenReturn(true);

        when(mockUserRepository.findById("1"))
                .thenReturn(java.util.Optional.of(users.get(0)));

        when(mockUserRepository.findById("2"))
                .thenReturn(java.util.Optional.of(users.get(1)));

        when(mockUserValidationService.isValid(any(UserEntity.class)))
                .thenReturn(false);

        CustomException customException = Assertions.assertThrows(CustomException.class, () -> {
            userService.updateUser(userServiceModel, "5");
        });
        assertEquals(SERVER_ERROR_MESSAGE, customException.getMessage());

        // Assert
        verify(mockUserRepository).save(any());
        verify(mockUserRepository, times(1)).save(any());
    }

    @Test
    public void updateUser_whenLoggedInUserHasRootRole_updateUser() throws Exception {
        // Arrange
        UserServiceModel userServiceModel = UsersUtils.getUserServiceModels(1).get(0);

        List<UserEntity> users = UsersUtils.getUsers(2);

        when(mockUserValidationService.isValid(any(UserServiceModel.class)))
                .thenReturn(true);

        when(mockUserRepository.findById("1"))
                .thenReturn(java.util.Optional.of(users.get(0)));

        when(mockUserRepository.findById("2"))
                .thenReturn(java.util.Optional.of(users.get(1)));

        when(mockUserValidationService.isValid(any(UserEntity.class)))
                .thenReturn(true);

        // Act
        userService.updateUser(userServiceModel, "2");

        // Assert
        verify(mockUserRepository).save(any());
        verify(mockUserRepository, times(1)).save(any());
    }

    @Test
    public void updateUser_whenLoggedInUserHasAdminRole_updateUser() throws Exception {
        // Arrange

        UserServiceModel userServiceModel = UsersUtils.getUserServiceModels(1).get(0);

        List<UserEntity> users = UsersUtils.getUsers(2);

        when(mockUserValidationService.isValid(any(UserServiceModel.class)))
                .thenReturn(true);

        when(mockUserRepository.findById("1"))
                .thenReturn(java.util.Optional.of(users.get(0)));

        when(mockUserRepository.findById("2"))
                .thenReturn(java.util.Optional.of(users.get(1)));

        when(mockUserValidationService.isValid(any(UserEntity.class)))
                .thenReturn(true);

        // Act
        userService.updateUser(userServiceModel, "2");

        // Assert
        verify(mockUserRepository).save(any());
        verify(mockUserRepository, times(1)).save(any());
    }

    @Test
    public void updateUser_whenLoggedInUserHasUserRole_throwException() throws Exception {
        // Arrange

        UserServiceModel userServiceModel = UsersUtils.getUserServiceModels(1).get(0);

        List<UserEntity> users = UsersUtils.getUsers(2);

        when(mockUserValidationService.isValid(any(UserServiceModel.class)))
                .thenReturn(true);

        when(mockUserRepository.findById("1"))
                .thenReturn(java.util.Optional.of(users.get(0)));

        when(mockUserRepository.findById("2"))
                .thenReturn(java.util.Optional.of(users.get(1)));

        when(mockUserValidationService.isValid(any(UserEntity.class)))
                .thenReturn(true);

        CustomException customException = Assertions.assertThrows(CustomException.class, () -> {
            userService.updateUser(userServiceModel, "2");
        });
        assertEquals(UNAUTHORIZED_SERVER_ERROR_MESSAGE, customException.getMessage());

        // Assert
        verify(mockUserRepository).save(any());
        verify(mockUserRepository, times(1)).save(any());
    }


//    updateUserOnlineStatus

    @Test
    public void updateUserOnlineStatus_whenAllInputsAreValidAndChangeToOnlineIsTrue_updateUserOnlineStatus() throws Exception {
        // Arrange

        UserEntity user = UsersUtils.createUser();
        user.setOnline(true);

        when(mockUserRepository.findByUsername(anyString()))
                .thenReturn(java.util.Optional.of(user));

        when(mockUserValidationService.isValid(any(UserEntity.class)))
                .thenReturn(true);

        when(mockUserRepository.save(any(UserEntity.class))).thenReturn(user);

        // Act
        UserServiceModel actual = userService.updateUserOnlineStatus("pesho", true);

        // Assert
        assertTrue(actual.isOnline());

        assertEquals(user.getId(), actual.getId());
        assertEquals(user.getFirstName(), actual.getFirstName());
        assertEquals(user.getLastName(), actual.getLastName());
        assertEquals(user.getUsername(), actual.getUsername());
        assertEquals(user.getEmail(), actual.getEmail());
        assertEquals(user.getAddress(), actual.getAddress());

        verify(mockUserRepository).save(any());
        verify(mockUserRepository, times(1)).save(any());
    }

    @Test
    public void updateUserOnlineStatus_whenAllInputsAreValidAndChangeToOnlineIsFalse_updateUserOnlineStatus() throws Exception {
        // Arrange

        UserEntity user = UsersUtils.createUser();

        when(mockUserRepository.findByUsername(anyString()))
                .thenReturn(java.util.Optional.of(user));

        when(mockUserValidationService.isValid(any(UserEntity.class)))
                .thenReturn(true);

        when(mockUserRepository.save(any(UserEntity.class))).thenReturn(user);

        // Act
        UserServiceModel actual = userService.updateUserOnlineStatus("pesho", false);

        // Assert
        assertFalse(actual.isOnline());

        assertEquals(user.getId(), actual.getId());
        assertEquals(user.getFirstName(), actual.getFirstName());
        assertEquals(user.getLastName(), actual.getLastName());
        assertEquals(user.getUsername(), actual.getUsername());
        assertEquals(user.getEmail(), actual.getEmail());
        assertEquals(user.getAddress(), actual.getAddress());

        verify(mockUserRepository).save(any());
        verify(mockUserRepository, times(1)).save(any());
    }

    @Test
    public void updateUserOnlineStatus_whenAllUsernameIsNotValid_throwException() throws Exception {
        // Arrange
        UserEntity user = UsersUtils.createUser();
        user.setOnline(true);

        when(mockUserRepository.findByUsername(anyString()))
                .thenReturn(java.util.Optional.of(user));

        when(mockUserValidationService.isValid(any(UserEntity.class)))
                .thenReturn(false);

        when(mockUserRepository.save(any(UserEntity.class))).thenReturn(user);

        CustomException customException = Assertions.assertThrows(CustomException.class, () -> {
            userService.updateUserOnlineStatus("pesho", true);
        });
        assertEquals(SERVER_ERROR_MESSAGE, customException.getMessage());
    }

    @Test
    public void updateUserOnlineStatus_whenSaveUserReturnsNull_throwException() throws Exception {
        // Arrange
        UserEntity user = UsersUtils.createUser();
        user.setOnline(true);

        when(mockUserRepository.findByUsername(anyString()))
                .thenReturn(java.util.Optional.of(user));

        when(mockUserValidationService.isValid(any(UserEntity.class)))
                .thenReturn(true);

        when(mockUserRepository.save(any(UserEntity.class))).thenReturn(null);

        CustomException customException = Assertions.assertThrows(CustomException.class, () -> {
            userService.updateUserOnlineStatus("pesho", true);
        });
        assertEquals(SERVER_ERROR_MESSAGE, customException.getMessage());
    }

    @Test
    public void getById_whenUserIdIsValid_returnUser() throws Exception {
        // Arrange
        UserEntity user = UsersUtils.createUser();

        when(mockUserRepository.findById(anyString()))
                .thenReturn(java.util.Optional.of(user));

        when(mockUserValidationService.isValid(any(UserEntity.class)))
                .thenReturn(true);

        // Act
        userService.getById("1");

        // Assert
        verify(mockUserRepository).findById(any());
        verify(mockUserRepository, times(1)).findById(any());
    }

    @Test
    public void getById_whenUserIdIsNotValid_throwException() throws Exception {
        // Arrange
        when(mockUserRepository.findById(anyString()))
                .thenReturn(null);

        when(mockUserValidationService.isValid(any(UserEntity.class)))
                .thenReturn(false);

        Assertions.assertThrows(Exception.class, () -> {
            userService.getById(anyString());
        });
    }

    @Test
    public void editById_whenUserIdIsValid_returnUser() throws Exception {
        // Arrange
        UserEntity user = UsersUtils.createUser();

        when(mockUserRepository.findById(anyString()))
                .thenReturn(java.util.Optional.of(user));

        when(mockUserValidationService.isValid(any(UserEntity.class)))
                .thenReturn(true);

        // Act
        userService.editById("1");

        // Assert
        verify(mockUserRepository).findById(any());
        verify(mockUserRepository, times(1)).findById(any());
    }

    @Test
    public void editById_whenUserIdIsNotValid_throwException() throws Exception {
        // Arrange
        when(mockUserRepository.findById(anyString()))
                .thenReturn(null);

        when(mockUserValidationService.isValid(any(UserEntity.class)))
                .thenReturn(false);

        Assertions.assertThrows(Exception.class, () -> {
            userService.editById(anyString());
        });
    }

    @Test
    public void getByEmailValidation_whenEmailIsValid_returnUser() throws Exception {
        // Arrange
        UserEntity user = UsersUtils.createUser();

        when(mockUserRepository.findByEmail(anyString()))
                .thenReturn(user);

        // Act
        UserEntity actualUser = userService.getByEmailValidation("e-mail");

        // Assert
        assertNotNull(actualUser);

        verify(mockUserRepository).findByEmail(any());
        verify(mockUserRepository, times(1)).findByEmail(any());
    }

    @Test
    public void getByEmailValidation_whenEmailIsNotValid_returnNull() throws Exception {
        // Arrange
        when(mockUserRepository.findByEmail(anyString()))
                .thenReturn(null);

        // Act
        UserEntity user = userService.getByEmailValidation(anyString());

        // Assert
        assertNull(user);

        verify(mockUserRepository).findByEmail(any());
        verify(mockUserRepository, times(1)).findByEmail(anyString());
    }

    @Test
    public void getByUsernameValidation_whenUsernameIsValid_returnUser() throws Exception {
        // Arrange
        UserEntity user = UsersUtils.createUser();

        when(mockUserRepository.findByUsername(anyString()))
                .thenReturn(java.util.Optional.of(user));

        // Act
        UserEntity actualUser = userService.getByUsernameValidation("pesho");

        // Assert
        assertNotNull(actualUser);

        verify(mockUserRepository).findByUsername(any());
        verify(mockUserRepository, times(1)).findByUsername(any());
    }

    @Test
    public void getByUsernameValidation_whenUserNameIsNotValid_returnNull() throws Exception {
        // Act
        UserEntity user = userService.getByUsernameValidation("invalid_username");

        // Assert
        assertNull(user);

        verify(mockUserRepository).findByUsername(any());
        verify(mockUserRepository, times(1)).findByUsername(anyString());
    }

    @Test
    public void deleteUserById_whenUserIdIsValid_deleteUser() throws Exception {
        // Arrange
        UserEntity user = UsersUtils.createUser();

        when(mockUserRepository.findById(anyString()))
                .thenReturn(java.util.Optional.of(user));

        when(mockUserValidationService.isValid(any(UserEntity.class)))
                .thenReturn(true);

        // Act
        userService.deleteUserById("userId");

        // Assert
        verify(mockUserRepository).deleteById(anyString());
        verify(mockUserRepository, times(1)).deleteById(anyString());
    }

    @Test
    public void deleteUserById_whenUserIdIsNotValid_throwException() throws Exception {
        // Arrange
        UserEntity user = UsersUtils.createUser();

        when(mockUserRepository.findById(anyString()))
                .thenReturn(java.util.Optional.of(user));

        when(mockUserValidationService.isValid(any(UserEntity.class)))
                .thenReturn(false);


        Assertions.assertThrows(Exception.class, () -> {
            userService.deleteUserById("invalid_id");
        });
    }

}
