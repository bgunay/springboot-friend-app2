package org.pinsoft.interview.domain.repo.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "logs")
public class Logger extends BaseEntity{

    @Builder
    public Logger(String username, String method, String tableName, String action, LocalDateTime time) {
        this.username = username;
        this.method = method;
        this.tableName = tableName;
        this.action = action;
        this.time = time;
    }

    private String username;
    private String method;
    private String tableName;
    private String action;
    private LocalDateTime time;
}
