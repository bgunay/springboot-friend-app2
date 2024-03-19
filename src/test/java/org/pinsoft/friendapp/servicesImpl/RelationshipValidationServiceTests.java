package org.pinsoft.friendapp.servicesImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.pinsoft.friendapp.domain.repo.entity.Relationship;
import org.pinsoft.friendapp.domain.repo.entity.UserEntity;
import org.pinsoft.friendapp.testUtils.RelationshipsUtils;
import org.pinsoft.friendapp.testUtils.UsersUtils;
import org.pinsoft.friendapp.utils.validations.serviceValidation.services.RelationshipValidationService;
import org.pinsoft.friendapp.utils.validations.serviceValidation.servicesImpl.RelationshipValidationServiceImpl;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class RelationshipValidationServiceTests {
    private RelationshipValidationService relationshipValidationService;

    @BeforeEach
    public void setupTest(){
        relationshipValidationService = new RelationshipValidationServiceImpl();
    }

    @Test
    public void isValid_whenValid_true(){
        List<UserEntity> users = UsersUtils.getUsers(2);
        UserEntity userOne = users.get(0);
        UserEntity userTwo = users.get(1);

        Relationship relationship = RelationshipsUtils.createRelationship(userOne, userTwo, 0, userOne);

        boolean result = relationshipValidationService.isValid(relationship);
        assertTrue(result);
    }

    @Test
    public void isValid_whenNull_false(){
        Relationship relationship = null;
        boolean result = relationshipValidationService.isValid(relationship);
        assertFalse(result);
    }
}
