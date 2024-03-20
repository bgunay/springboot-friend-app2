package org.pinsoft.friendapp.domain.dto.message;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class MessageAllViewModel {
    private String id;
    private String fromUserId;
    private String fromUserUsername;
    private String toUserUsername;
    private String fromUserFirstName;
    private String fromUserLastName;
    private String content;
    private LocalDateTime time;

}
