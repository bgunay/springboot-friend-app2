package org.pinsoft.friendapp.domain.repo.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
@Entity
@Table(name = "logs")
public class Logger{

    @Id
    @UuidGenerator
    private String id;
    @Column(name = "username", nullable = false)
    private String username;
    @Column(name = "method", nullable = false)
    private String method;
    @Column(name = "tableName", nullable = false)
    private String tableName;
    @Column(name = "action", nullable = false)
    private String action;
    @Column(name = "time", nullable = false)
    private LocalDateTime time;
}
