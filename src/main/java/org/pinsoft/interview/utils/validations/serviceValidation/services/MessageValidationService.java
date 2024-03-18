package org.pinsoft.interview.utils.validations.serviceValidation.services;


import org.pinsoft.interview.domain.dto.message.MessageCreateBindingModel;

public interface MessageValidationService {
    boolean isValid(MessageCreateBindingModel messageCreateBindingModel);
}
