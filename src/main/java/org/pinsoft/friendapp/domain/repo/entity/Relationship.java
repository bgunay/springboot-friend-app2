package org.pinsoft.friendapp.domain.repo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
@Table(name = "relationship")
@Entity
public class Relationship {

    @Id
    @UuidGenerator
    private String id;

    @ManyToOne(optional = false, targetEntity = UserEntity.class)
    @JoinColumn(name = "user_one_id", referencedColumnName = "id")
    private UserEntity userOne;

    @ManyToOne(optional = false, targetEntity = UserEntity.class)
    @JoinColumn(name = "user_two_id", referencedColumnName = "id")
    private UserEntity userTwo;

    @Column(name = "status", columnDefinition = "INT", nullable = false)
    @ColumnDefault("0")
    private int status;

    @ManyToOne(optional = false, targetEntity = UserEntity.class)
    @JoinColumn(name = "action_user_id", referencedColumnName = "id")
    private UserEntity actionUser;

    @Column(name = "time", nullable = false)
    private LocalDateTime time;

    @OneToMany(mappedBy = Message_.RELATIONSHIP, targetEntity = Message.class, cascade = CascadeType.ALL)
    private List<Message> messageList;

    @Override
    public String toString() {
        return "Relationship{" +
                "id='" + id + '\'' +
                ", status=" + status +
                ", time=" + time +
                ", messageList=" + messageList +
                '}';
    }

}
