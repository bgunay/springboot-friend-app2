package org.pinsoft.friendapp.utils.validations.serviceValidation.services;

import org.pinsoft.friendapp.domain.dto.logger.LoggerServiceModel;

public interface LoggerValidationService {
    boolean isValid(LoggerServiceModel loggerServiceModel);

    boolean isValid(String method, String principal, String tableName, String action);

    boolean isValid(String username);
}
