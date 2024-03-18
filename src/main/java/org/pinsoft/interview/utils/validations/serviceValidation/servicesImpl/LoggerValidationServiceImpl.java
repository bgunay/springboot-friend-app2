package org.pinsoft.interview.utils.validations.serviceValidation.servicesImpl;

import org.pinsoft.interview.domain.dto.logger.LoggerServiceModel;
import org.pinsoft.interview.utils.validations.serviceValidation.services.LoggerValidationService;
import org.springframework.stereotype.Component;

@Component
public class LoggerValidationServiceImpl implements LoggerValidationService {
    @Override
    public boolean isValid(LoggerServiceModel loggerServiceModel) {
        return loggerServiceModel != null;
    }

    @Override
    public boolean isValid(String method, String principal, String tableName, String action) {
        return method != null && principal != null && tableName != null && action != null;
    }

    @Override
    public boolean isValid(String username) {
        return username != null;
    }
}
