package org.pinsoft.interview.utils.validations.serviceValidation.services;

import org.pinsoft.interview.domain.dto.logger.LoggerServiceModel;

public interface LoggerValidationService {
    boolean isValid(LoggerServiceModel loggerServiceModel);

    boolean isValid(String method, String principal, String tableName, String action);

    boolean isValid(String username);
}
