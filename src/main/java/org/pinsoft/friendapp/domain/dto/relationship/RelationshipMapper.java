package org.pinsoft.friendapp.domain.dto.relationship;

import org.pinsoft.friendapp.domain.repo.entity.Relationship;
import org.springframework.stereotype.Component;

@Component
public class RelationshipMapper {

    public RelationshipViewModel convert(Relationship relationship){
        return RelationshipViewModel.builder()
                .id(relationship.getId())
                .status(relationship.getStatus())
                .userOne(relationship.getUserOne().getId() + " " + relationship.getUserOne().getUsername() + " " + relationship.getUserOne().getEmail())
                .userTwo(relationship.getUserTwo().getId() + " " + relationship.getUserTwo().getUsername() + " " + relationship.getUserOne().getEmail())
                .actionUser(relationship.getActionUser().getId() + " " + relationship.getActionUser().getUsername())
                .time(relationship.getTime())
                .build();
    }
}
