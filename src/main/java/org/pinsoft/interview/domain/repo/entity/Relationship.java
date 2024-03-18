package org.pinsoft.interview.domain.repo.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Table(name = "relationship")
public class Relationship extends BaseEntity {

    @Builder
    public Relationship(final User userOne,
                        final User userTwo,
                        final int status, final User actionUser,
                        final LocalDateTime time,
                        final List<Message> messageList) {
        this.userOne = userOne;
        this.userTwo = userTwo;
        this.status = status;
        this.actionUser = actionUser;
        this.time = time;
        this.messageList = messageList;
    }

    @ManyToOne(optional = false, targetEntity = User.class)
    @JoinColumn(name = "user_one_id", referencedColumnName = "id")
    private User userOne;
    @ManyToOne(optional = false, targetEntity = User.class)
    @JoinColumn(name = "user_two_id", referencedColumnName = "id")
    private User userTwo;
    @Column(name = "status", columnDefinition = "TINYINT DEFAULT 0", nullable = false)
    private int status;
    @ManyToOne(optional = false, targetEntity = User.class)
    @JoinColumn(name = "action_user_id", referencedColumnName = "id")
    private User actionUser;
    @Column(name = "time", nullable = false)
    private LocalDateTime time;
    @OneToMany(mappedBy = "relationship", targetEntity = Message.class, cascade = CascadeType.ALL)
    private List<Message> messageList;

}
