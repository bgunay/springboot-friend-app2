package org.pinsoft.interview.domain.repo.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Table(name = "messages")
public class Message extends BaseEntity{

    @Builder
    public Message(User fromUser, User toUser, Relationship relationship, String subject, String content, int status, LocalDateTime time) {
        this.fromUser = fromUser;
        this.toUser = toUser;
        this.relationship = relationship;
        this.subject = subject;
        this.content = content;
        this.status = status;
        this.time = time;
    }

    @ManyToOne(optional = false, targetEntity = User.class)
    @JoinColumn(name = "from_user_id", referencedColumnName = "id")
    private User fromUser;
    @ManyToOne(optional = false, targetEntity = User.class)
    @JoinColumn(name = "to_user_id", referencedColumnName = "id")
    private User toUser;
    @ManyToOne(optional = false, targetEntity = Relationship.class)
    @JoinColumn(name = "relationship_id", referencedColumnName = "id")
    private Relationship relationship;
    @Column(name = "subject")
    private String subject;
    @Column(name = "content", nullable = false, columnDefinition = "TEXT")
    private String content;
    @Column(name = "status", columnDefinition = "TINYINT DEFAULT 0")
    private int status;
    @Column(name = "time", nullable = false)
    private LocalDateTime time;
}
