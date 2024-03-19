package org.pinsoft.friendapp.domain.dto.message;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.pinsoft.friendapp.domain.repo.entity.Relationship;
import org.pinsoft.friendapp.domain.repo.entity.UserEntity;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class MessageServiceModel {
    private String id;
    private UserEntity fromUser;
    private UserEntity toUser;
    private Relationship relationship;
    private String subject;
    private String content;
    private int status;
    private LocalDateTime time;
}
