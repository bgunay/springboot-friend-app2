package org.pinsoft.interview.domain.dto.user;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
public class UserServiceModel implements Serializable {
    private String id;
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private String address;
    private String city;
    private String username;
    private String profilePicUrl;
    private String backgroundImageUrl;
    private boolean isOnline;
    private boolean isEnabled;

    private Boolean isDeleted = false;
}
