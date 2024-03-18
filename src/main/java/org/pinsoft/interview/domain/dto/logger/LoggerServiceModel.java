package org.pinsoft.interview.domain.dto.logger;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class LoggerServiceModel {
    private String id;
    private String username;
    private String method;
    private String tableName;
    private String action;
    private LocalDateTime time;

}
