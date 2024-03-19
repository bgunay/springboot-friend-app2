package org.pinsoft.friendapp.servicesImpl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.pinsoft.friendapp.domain.dto.logger.LoggerServiceModel;
import org.pinsoft.friendapp.domain.repo.LoggerRepository;
import org.pinsoft.friendapp.domain.repo.entity.Logger;
import org.pinsoft.friendapp.service.LoggerService;
import org.pinsoft.friendapp.testUtils.LoggerUtils;
import org.pinsoft.friendapp.utils.responseHandler.exceptions.CustomException;
import org.pinsoft.friendapp.utils.validations.serviceValidation.services.LoggerValidationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.pinsoft.friendapp.utils.constants.ResponseMessageConstants.*;

@SpringBootTest
public class LoggerServiceTests {

    @Autowired
    private LoggerService loggerService;

    @MockBean
    private LoggerRepository mockLoggerRepository;

    @MockBean
    private LoggerValidationService mockLoggerValidation;

    @BeforeEach
    public void setUpTest() {
    }

    @Test
    public void createLog_whenInputsAreValid_createLog() {
        // Arrange
        Logger log = LoggerUtils.createLog();

        when(mockLoggerValidation.isValid(anyString(), anyString(), anyString(), anyString()))
                .thenReturn(true);

        when(mockLoggerValidation.isValid(any(LoggerServiceModel.class)))
                .thenReturn(true);


        when(mockLoggerRepository.save(any()))
                .thenReturn(log);

        // Act
        boolean result = loggerService.createLog(log.getMethod(), log.getUsername(), log.getTableName(), log.getAction());

        // Assert
        assertTrue(result);

        verify(mockLoggerRepository).save(any());
        verify(mockLoggerRepository, times(1)).save(any());
        verifyNoMoreInteractions(mockLoggerRepository);
    }

    @Test
    public void createComment_whenInputsAreNotValid_throwException() {
        // Arrange
        Logger log = LoggerUtils.createLog();

        when(mockLoggerValidation.isValid(anyString(), anyString(), anyString(), anyString()))
                .thenReturn(false);

        when(mockLoggerValidation.isValid(any(LoggerServiceModel.class)))
                .thenReturn(true);

        when(mockLoggerRepository.save(any()))
                .thenReturn(log);


        CustomException customException = Assertions.assertThrows(CustomException.class, () -> {
            loggerService.createLog(log.getMethod(), log.getUsername(), log.getTableName(), log.getAction());
        });

        assertEquals(FAILURE_LOGS_SAVING_MESSAGE, customException.getMessage());
    }


    @Test()
    public void createComment_whenLoggerServiceModelIsNotValid_throwException() {
        // Arrange
        Logger log = LoggerUtils.createLog();

        when(mockLoggerValidation.isValid(anyString(), anyString(), anyString(), anyString()))
                .thenReturn(true);

        when(mockLoggerValidation.isValid(any(LoggerServiceModel.class)))
                .thenReturn(false);

        when(mockLoggerRepository.save(any()))
                .thenReturn(log);

        // Act

        CustomException customException = Assertions.assertThrows(CustomException.class, () -> {
            loggerService.createLog(log.getMethod(), log.getUsername(), log.getTableName(), log.getAction());
        });

        assertEquals(FAILURE_LOGS_SAVING_MESSAGE, customException.getMessage());

    }

    @Test()
    public void createComment_whenLoggerRepositoryDontSaveLog_throwException() {
        // Arrange
        Logger log = LoggerUtils.createLog();

        when(mockLoggerValidation.isValid(anyString(), anyString(), anyString(), anyString()))
                .thenReturn(true);

        when(mockLoggerValidation.isValid(any(LoggerServiceModel.class)))
                .thenReturn(true);

        when(mockLoggerRepository.save(any()))
                .thenReturn(null);


        // Act
        CustomException customException = Assertions.assertThrows(CustomException.class, () -> {
            loggerService.createLog(log.getMethod(), log.getUsername(), log.getTableName(), log.getAction());
        });
        assertEquals(FAILURE_LOGS_SAVING_MESSAGE, customException.getMessage());

        verify(mockLoggerRepository).save(any());
        verify(mockLoggerRepository, times(1)).save(any());
        verifyNoMoreInteractions(mockLoggerRepository);
    }

    @Test
    public void getAllLogs_when2Logs_2Logs() {
        // Arrange
        List<Logger> logs = LoggerUtils.getLogs(2);

        when(mockLoggerRepository.findAllByOrderByTimeDesc()).thenReturn(logs);

        // Act
        List<LoggerServiceModel> allLogs = loggerService.getAllLogs();

        // Assert
        Logger expected = logs.get(0);
        LoggerServiceModel actual = allLogs.get(0);

        assertEquals(2, allLogs.size());
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getUsername(), actual.getUsername());
        assertEquals(expected.getAction(), actual.getAction());
        assertEquals(expected.getMethod(), actual.getMethod());
        assertEquals(expected.getTableName(), actual.getTableName());
        assertEquals(expected.getTime(), actual.getTime());

        verify(mockLoggerRepository).findAllByOrderByTimeDesc();
        verify(mockLoggerRepository, times(1)).findAllByOrderByTimeDesc();
        verifyNoMoreInteractions(mockLoggerRepository);
    }

    @Test
    public void getAllLogs_whenNoLogs_returnEmptyPosts() {
        when(mockLoggerRepository.findAllByOrderByTimeDesc()).thenReturn(new ArrayList<>());

        List<LoggerServiceModel> allLogs = loggerService.getAllLogs();

        assertTrue(allLogs.isEmpty());

        verify(mockLoggerRepository).findAllByOrderByTimeDesc();
        verify(mockLoggerRepository, times(1)).findAllByOrderByTimeDesc();
        verifyNoMoreInteractions(mockLoggerRepository);
    }

    @Test
    public void getLogsByUsername_when2Logs_2Logs() {
        // Arrange
        List<Logger> logs = LoggerUtils.getLogs(2);

        when(mockLoggerValidation.isValid(anyString())).thenReturn(true);

        when(mockLoggerRepository.findAllByUsernameOrderByTimeDesc(anyString())).thenReturn(logs);

        // Act
        List<LoggerServiceModel> allLogs = loggerService.getLogsByUsername("burhan");

        // Assert
        Logger expected = logs.get(0);
        LoggerServiceModel actual = allLogs.get(0);

        assertEquals(2, allLogs.size());
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getUsername(), actual.getUsername());
        assertEquals(expected.getAction(), actual.getAction());
        assertEquals(expected.getMethod(), actual.getMethod());
        assertEquals(expected.getTableName(), actual.getTableName());
        assertEquals(expected.getTime(), actual.getTime());

        verify(mockLoggerRepository).findAllByUsernameOrderByTimeDesc(anyString());
        verify(mockLoggerRepository, times(1)).findAllByUsernameOrderByTimeDesc(anyString());
        verifyNoMoreInteractions(mockLoggerRepository);
    }

    @Test
    public void getLogsByUsername_whenUsernameIsNotValid_throwException() {
        // Arrange
        List<Logger> logs = LoggerUtils.getLogs(2);

        when(mockLoggerValidation.isValid(anyString())).thenReturn(false);

        when(mockLoggerRepository.findAllByUsernameOrderByTimeDesc(anyString())).thenReturn(logs);

        CustomException customException = Assertions.assertThrows(CustomException.class, () -> {
            loggerService.getLogsByUsername("burhan");
        });
        assertEquals(SERVER_ERROR_MESSAGE, customException.getMessage());
    }

    @Test
    public void getLogsByUsername_whenNoLogs_throwException() {
        // Arrange
        when(mockLoggerValidation.isValid(anyString())).thenReturn(true);

        when(mockLoggerRepository.findAllByUsernameOrderByTimeDesc(anyString())).thenReturn(new ArrayList<>());

        CustomException customException = Assertions.assertThrows(CustomException.class, () -> {
            loggerService.getLogsByUsername("burhan");
        });
        assertEquals(FAILURE_LOGS_NOT_FOUND_MESSAGE, customException.getMessage());

        // Assert
        verify(mockLoggerRepository).findAllByUsernameOrderByTimeDesc(anyString());
        verify(mockLoggerRepository, times(1)).findAllByUsernameOrderByTimeDesc(anyString());
        verifyNoMoreInteractions(mockLoggerRepository);
    }

    @Test
    public void deleteAll_whenInputIsValid_deleteAll() throws Exception {
        // Act
        boolean result = loggerService.deleteAll();

        // Assert
        assertTrue(result);

        verify(mockLoggerRepository).deleteAll();
        verify(mockLoggerRepository, times(1)).deleteAll();
        verifyNoMoreInteractions(mockLoggerRepository);
    }

    @Test
    public void deleteByName_whenUsernameIsValidAndUserHasAnyNumberLogs_returnTrue() {
        // Arrange
        List<Logger> logs = LoggerUtils.getLogs(2);

        when(mockLoggerValidation.isValid(anyString())).thenReturn(true);

        when(mockLoggerRepository.deleteAllByUsername(anyString())).thenReturn(logs);

        // Act
        boolean result = loggerService.deleteByName("burhan");

        // Assert
        assertTrue(result);

        verify(mockLoggerRepository).deleteAllByUsername(anyString());
        verify(mockLoggerRepository, times(1)).deleteAllByUsername(anyString());
        verifyNoMoreInteractions(mockLoggerRepository);
    }

    @Test
    public void deleteByName_whenUsernameIsNotValid_throwException() {
        // Arrange
        List<Logger> logs = LoggerUtils.getLogs(2);

        when(mockLoggerValidation.isValid(anyString())).thenReturn(false);

        when(mockLoggerRepository.deleteAllByUsername(anyString())).thenReturn(logs);

        CustomException customException = Assertions.assertThrows(CustomException.class, () -> {
            loggerService.deleteByName("burhan");
        });
        assertEquals(SERVER_ERROR_MESSAGE, customException.getMessage());

    }

    @Test
    public void deleteByName_whenUsernameIsValidAndUserHasNoLogs_throwException() {
        // Arrange
        when(mockLoggerValidation.isValid(anyString())).thenReturn(true);

        when(mockLoggerRepository.deleteAllByUsername(anyString())).thenReturn(new ArrayList<>());

        CustomException customException = Assertions.assertThrows(CustomException.class, () -> {
            loggerService.deleteByName("burhan");
        });
        assertEquals(SERVER_ERROR_MESSAGE, customException.getMessage());
    }
}
