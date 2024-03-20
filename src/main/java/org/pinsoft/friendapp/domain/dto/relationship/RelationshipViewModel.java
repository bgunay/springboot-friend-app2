package org.pinsoft.friendapp.domain.dto.relationship;

import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RelationshipViewModel implements Serializable {
    private String id;
    private String userOne;
    private String userTwo;
    private int status;
    private String actionUser;
    private LocalDateTime time;

}
