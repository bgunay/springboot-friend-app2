package org.pinsoft.friendapp.utils.validations.serviceValidation.servicesImpl;

import org.pinsoft.friendapp.domain.repo.entity.Relationship;
import org.pinsoft.friendapp.utils.validations.serviceValidation.services.RelationshipValidationService;
import org.springframework.stereotype.Component;

@Component
public class RelationshipValidationServiceImpl implements RelationshipValidationService {
    @Override
    public boolean isValid(Relationship relationship) {
        return relationship != null;
    }
}
