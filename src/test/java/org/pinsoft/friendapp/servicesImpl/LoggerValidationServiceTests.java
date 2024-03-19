package org.pinsoft.friendapp.servicesImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.pinsoft.friendapp.domain.dto.logger.LoggerServiceModel;
import org.pinsoft.friendapp.testUtils.LoggerUtils;
import org.pinsoft.friendapp.utils.validations.serviceValidation.services.LoggerValidationService;
import org.pinsoft.friendapp.utils.validations.serviceValidation.servicesImpl.LoggerValidationServiceImpl;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class LoggerValidationServiceTests {
    private LoggerValidationService loggerValidation;

    @BeforeEach
    public void setupTest() {
        loggerValidation = new LoggerValidationServiceImpl();
    }

    @Test
    public void isValidWithLoggerServiceModel_whenValid_true() {
        LoggerServiceModel loggerServiceModel = LoggerUtils.getLoggerServiceModels(1).get(0);

        boolean result = loggerValidation.isValid(loggerServiceModel);
        assertTrue(result);
    }

    @Test
    public void isValidWithLoggerServiceModel_whenNull_false() {
        LoggerServiceModel loggerServiceModel =  null;
        boolean result = loggerValidation.isValid(loggerServiceModel);
        assertFalse(result);
    }

    @Test
    public void isValidWith4Strings_whenValid_true() {
        String method = "POST";
        String principal = "principal";
        String tableName = "users";
        String action = "add";
        boolean result = loggerValidation.isValid(method, principal, tableName, action);
        assertTrue(result);
    }

    @Test
    public void isValidWith4Strings_whenOneOfTheStringIsNull_false() {
        String method = "POST";
        String principal = "principal";
        String tableName = "users";
        String action = null;
        boolean result = loggerValidation.isValid(method, principal, tableName, action);
        assertFalse(result);
    }

    @Test
    public void isValidWith4Strings_whenAllAreNull_false() {
        String method = null;
        String principal = null;
        String tableName = null;
        String action = null;
        boolean result = loggerValidation.isValid(method, principal, tableName, action);
        assertFalse(result);
    }

    @Test
    public void isValidWithUsername_whenValid_true() {
        String username = "username";
        boolean result = loggerValidation.isValid(username);
        assertTrue(result);
    }

    @Test
    public void isValidWithUsername_whenIsNull_false() {
        String username = null;
        boolean result = loggerValidation.isValid(username);
        assertFalse(result);
    }
}
