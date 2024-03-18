package org.pinsoft.interview.utils.validations.serviceValidation.services;

import org.pinsoft.interview.domain.repo.entity.Relationship;

public interface RelationshipValidationService {
    boolean isValid(Relationship relationship);
}
