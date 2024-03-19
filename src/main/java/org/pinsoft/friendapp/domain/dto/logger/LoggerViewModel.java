package org.pinsoft.friendapp.domain.dto.logger;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoggerViewModel {
    private String id;
    private String username;
    private String method;
    private String tableName;
    private String action;
    private String time;

}
