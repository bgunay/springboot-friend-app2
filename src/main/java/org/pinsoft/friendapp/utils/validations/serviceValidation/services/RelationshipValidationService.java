package org.pinsoft.friendapp.utils.validations.serviceValidation.services;

import org.pinsoft.friendapp.domain.repo.entity.Relationship;

public interface RelationshipValidationService {
    boolean isValid(Relationship relationship);
}
