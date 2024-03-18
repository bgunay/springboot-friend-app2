package org.pinsoft.interview.domain.dto.message;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class MessageFriendsViewModel {
    private String id;
    private String fromUserId;
    private String fromUserProfilePicUrl;
    private String fromUserFirstName;
    private String fromUserLastName;
    private String content;
    private LocalDateTime time;
    private int count;

}
