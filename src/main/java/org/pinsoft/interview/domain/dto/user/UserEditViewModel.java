package org.pinsoft.interview.domain.dto.user;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserEditViewModel {
    private String id;
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private String address;
    private String city;

}
