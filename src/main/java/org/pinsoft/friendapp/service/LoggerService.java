package org.pinsoft.friendapp.service;

import org.pinsoft.friendapp.domain.dto.logger.LoggerServiceModel;

import java.util.List;

public interface LoggerService {

    List<LoggerServiceModel> getAllLogs();

    List<LoggerServiceModel> getLogsByUsername(String username);

    boolean deleteAll();

    boolean deleteByName(String username);

    boolean createLog(String method, String principal, String tableName, String action);
}
