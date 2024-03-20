package org.pinsoft.friendapp.domain.dto.relationship;

import com.fasterxml.jackson.annotation.*;
import lombok.*;
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
