package org.pinsoft.interview.domain.dto.relationship;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class FriendsAllViewModel {
    private String id;
    private String firstName;
    private String lastName;
    private String username;
    private String profilePicUrl;
    private String backgroundImageUrl;
    private boolean isOnline;

}
