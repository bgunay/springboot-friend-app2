package org.pinsoft.interview.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.pinsoft.interview.domain.dto.logger.LoggerServiceModel;
import org.pinsoft.interview.domain.repo.LoggerRepository;
import org.pinsoft.interview.domain.repo.entity.Logger;
import org.pinsoft.interview.service.LoggerService;
import org.pinsoft.interview.utils.responseHandler.exceptions.CustomException;
import org.pinsoft.interview.utils.validations.serviceValidation.services.LoggerValidationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import static org.pinsoft.interview.utils.constants.ResponseMessageConstants.*;

@Service
@RequiredArgsConstructor
public class LoggerServiceImpl implements LoggerService {
    private final ModelMapper modelMapper;
    private final LoggerRepository loggerRepository;
    private final LoggerValidationService loggerValidation;

    @Override
    public boolean createLog(String method, String principal, String tableName, String action) {
        if (!loggerValidation.isValid(method, principal, tableName, action)) {
            throw new CustomException(FAILURE_LOGS_SAVING_MESSAGE);
        }

        LoggerServiceModel loggerServiceModel = new LoggerServiceModel();
        loggerServiceModel.setMethod(method);
        loggerServiceModel.setUsername(principal);
        loggerServiceModel.setTableName(tableName);
        loggerServiceModel.setAction(action);
        loggerServiceModel.setTime(LocalDateTime.now());

        if (!loggerValidation.isValid(loggerServiceModel)) {
            throw new CustomException(FAILURE_LOGS_SAVING_MESSAGE);
        }

        Logger logger = this.modelMapper.map(loggerServiceModel, Logger.class);

        this.loggerRepository.save(logger);

        return true;

    }

    @Override
    public List<LoggerServiceModel> getAllLogs() {
        return this.loggerRepository
                .findAllByOrderByTimeDesc()
                .stream()
                .map(x -> this.modelMapper.map(x, LoggerServiceModel.class)).toList();
    }

    @Override
    public List<LoggerServiceModel> getLogsByUsername(String username) {
        if (!loggerValidation.isValid(username)) {
            throw new CustomException(SERVER_ERROR_MESSAGE);
        }

        List<LoggerServiceModel> logsByUsername = this.loggerRepository
                .findAllByUsernameOrderByTimeDesc(username)
                .stream()
                .map(x -> this.modelMapper.map(x, LoggerServiceModel.class)).toList();

        if (logsByUsername.size() == 0) {
            throw new CustomException(FAILURE_LOGS_NOT_FOUND_MESSAGE);
        }

        return logsByUsername;
    }

    @Override
    public boolean deleteAll() {
        try {
            this.loggerRepository.deleteAll();
            return true;
        } catch (Exception e) {
            throw new CustomException(FAILURE_LOGS_CLEARING_ERROR_MESSAGE);
        }
    }

    @Override
    @Transactional
    @Modifying
    public boolean deleteByName(String username) {
        if (!loggerValidation.isValid(username)) {
            throw new CustomException(SERVER_ERROR_MESSAGE);
        }

        List<Logger> loggers = this.loggerRepository.deleteAllByUsername(username);

        if (loggers.size() == 0) {
            throw new CustomException(FAILURE_LOGS_NOT_FOUND_MESSAGE);
        }
        return true;
    }

////    @Scheduled(cron = "* */30 * * * *")
//    @Scheduled(cron = "0 0 2 * * * ")
//    public void testSchedule(){
//        this.deleteAll();
//        System.out.println("Logs deleted successfully!");
//    }
}


