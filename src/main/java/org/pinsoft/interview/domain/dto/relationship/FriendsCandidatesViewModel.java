package org.pinsoft.interview.domain.dto.relationship;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class FriendsCandidatesViewModel {
    private String id;
    private String firstName;
    private String lastName;
    private String username;
    private String profilePicUrl;
    private String backgroundImageUrl;
    private Boolean starterOfAction = false;
    private Integer status;

}
