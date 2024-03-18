package org.pinsoft.interview.domain.dto.message;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.pinsoft.interview.domain.repo.entity.Relationship;
import org.pinsoft.interview.domain.repo.entity.User;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class MessageServiceModel {
    private String id;
    private User fromUser;
    private User toUser;
    private Relationship relationship;
    private String subject;
    private String content;
    private int status;
    private LocalDateTime time;
}
