package org.pinsoft.friendapp.utils.validations.serviceValidation.services;


import org.pinsoft.friendapp.domain.dto.message.MessageCreateBindingModel;

public interface MessageValidationService {
    boolean isValid(MessageCreateBindingModel messageCreateBindingModel);
}
