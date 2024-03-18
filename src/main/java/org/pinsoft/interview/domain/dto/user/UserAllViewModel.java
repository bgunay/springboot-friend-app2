package org.pinsoft.interview.domain.dto.user;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserAllViewModel {
    private String id;
    private String username;
    private String role;
}
