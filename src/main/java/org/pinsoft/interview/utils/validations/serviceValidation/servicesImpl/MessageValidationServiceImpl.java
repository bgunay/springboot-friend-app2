package org.pinsoft.interview.utils.validations.serviceValidation.servicesImpl;

import org.pinsoft.interview.domain.dto.message.MessageCreateBindingModel;
import org.pinsoft.interview.utils.validations.serviceValidation.services.MessageValidationService;
import org.springframework.stereotype.Component;

@Component
public class MessageValidationServiceImpl implements MessageValidationService {

    @Override
    public boolean isValid(MessageCreateBindingModel messageCreateBindingModel) {
        return messageCreateBindingModel != null;
    }
}
