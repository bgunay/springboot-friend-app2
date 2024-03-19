package org.pinsoft.friendapp.utils.validations.serviceValidation.servicesImpl;

import org.pinsoft.friendapp.domain.dto.message.MessageCreateBindingModel;
import org.pinsoft.friendapp.utils.validations.serviceValidation.services.MessageValidationService;
import org.springframework.stereotype.Component;

@Component
public class MessageValidationServiceImpl implements MessageValidationService {

    @Override
    public boolean isValid(MessageCreateBindingModel messageCreateBindingModel) {
        return messageCreateBindingModel != null;
    }
}
