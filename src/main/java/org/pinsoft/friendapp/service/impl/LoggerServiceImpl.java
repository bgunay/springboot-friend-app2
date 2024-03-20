package org.pinsoft.friendapp.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.pinsoft.friendapp.domain.dto.logger.LoggerServiceModel;
import org.pinsoft.friendapp.domain.repo.LoggerRepository;
import org.pinsoft.friendapp.domain.repo.entity.Logger;
import org.pinsoft.friendapp.service.LoggerService;
import org.pinsoft.friendapp.utils.responseHandler.exceptions.CustomException;
import org.pinsoft.friendapp.utils.validations.serviceValidation.services.LoggerValidationService;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.pinsoft.friendapp.utils.constants.ResponseMessageConstants.*;

@Service
@Transactional
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

        Logger saved = loggerRepository.save(logger);

        if (saved.getId() != null) {
            return true;
        } else {
            throw new CustomException(FAILURE_LOGS_SAVING_MESSAGE);
        }
    }

    @Override
    public List<LoggerServiceModel> getAllLogs() {
        return loggerRepository
                .findAllByOrderByTimeDesc()
                .stream()
                .map(x -> this.modelMapper.map(x, LoggerServiceModel.class)).collect(Collectors.toList());
    }

    @Override
    public List<LoggerServiceModel> getLogsByUsername(String username) {
        if (!loggerValidation.isValid(username)) {
            throw new CustomException(SERVER_ERROR_MESSAGE);
        }

        List<LoggerServiceModel> logsByUsername = loggerRepository
                .findAllByUsernameOrderByTimeDesc(username)
                .stream()
                .map(x -> this.modelMapper.map(x, LoggerServiceModel.class)).collect(Collectors.toList());

        if (logsByUsername.size() == 0) {
            throw new CustomException(FAILURE_LOGS_NOT_FOUND_MESSAGE);
        }

        return logsByUsername;
    }

    @Override
    public boolean deleteAll() {
        try {
            loggerRepository.deleteAll();
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

        List<Logger> loggers = loggerRepository.deleteAllByUsername(username);

        if (loggers.size() == 0) {
            throw new CustomException(FAILURE_LOGS_NOT_FOUND_MESSAGE);
        }
        return true;
    }

}


