package org.pinsoft.interview.domain.dto.relationship;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.pinsoft.interview.domain.repo.entity.User;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class RelationshipServiceModel implements Serializable {
    private String id;
    private User userOne;
    private User userTwo;
    private int status;
    private User actionUser;
    private LocalDateTime time;

}
