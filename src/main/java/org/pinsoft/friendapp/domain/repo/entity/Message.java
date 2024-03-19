package org.pinsoft.friendapp.domain.repo.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
@Entity
@Table(name = "messages")
public class Message{

    @Id
    @UuidGenerator
    private String id;

    @ManyToOne(optional = false, targetEntity = UserEntity.class)
    @JoinColumn(name = "from_userid", referencedColumnName = "id")
    private UserEntity fromUser;

    @ManyToOne(optional = false, targetEntity = UserEntity.class)
    @JoinColumn(name = "to_userid", referencedColumnName = "id")
    private UserEntity toUser;

    @ManyToOne(optional = false, targetEntity = Relationship.class)
    @JoinColumn(name = "relationship_id", referencedColumnName = "id")
    private Relationship relationship;

    @Column(name = "subject")
    private String subject;

    @Column(name = "content", nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(name = "status", columnDefinition = "INT")
    @ColumnDefault("0")
    private int status;

    @Column(name = "time", nullable = false)
    private LocalDateTime time;
}
