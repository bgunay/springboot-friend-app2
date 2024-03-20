package org.pinsoft.friendapp.domain.dto.relationship;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.pinsoft.friendapp.domain.repo.entity.UserEntity;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RelationshipServiceModel implements Serializable {
    private String id;
    private UserEntity userOne;
    private UserEntity userTwo;
    private int status;
    private UserEntity actionUser;
    private LocalDateTime time;

}
