package org.pinsoft.interview.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.pinsoft.interview.domain.dto.message.MessageCreateBindingModel;
import org.pinsoft.interview.domain.dto.message.MessageFriendsViewModel;
import org.pinsoft.interview.domain.dto.message.MessageServiceModel;
import org.pinsoft.interview.domain.repo.MessageRepository;
import org.pinsoft.interview.domain.repo.RelationshipRepository;
import org.pinsoft.interview.domain.repo.UserRepository;
import org.pinsoft.interview.domain.repo.entity.Message;
import org.pinsoft.interview.domain.repo.entity.Relationship;
import org.pinsoft.interview.domain.repo.entity.User;
import org.pinsoft.interview.service.MessageService;
import org.pinsoft.interview.utils.responseHandler.exceptions.CustomException;
import org.pinsoft.interview.utils.validations.serviceValidation.services.MessageValidationService;
import org.pinsoft.interview.utils.validations.serviceValidation.services.RelationshipValidationService;
import org.pinsoft.interview.utils.validations.serviceValidation.services.UserValidationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static org.pinsoft.interview.utils.constants.ResponseMessageConstants.SERVER_ERROR_MESSAGE;


@Service
@Transactional
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {
    private final MessageRepository messageRepository;
    private final RelationshipRepository relationshipRepository;
    private final UserRepository userRepository;
    private final UserValidationService userValidation;
    private final MessageValidationService messageValidation;
    private final RelationshipValidationService relationshipValidation;
    private final ModelMapper modelMapper;

    @Override
    public MessageServiceModel createMessage(MessageCreateBindingModel messageCreateBindingModel, String loggedInUsername) throws Exception {
        if (!messageValidation.isValid(messageCreateBindingModel)) {
            throw new CustomException(SERVER_ERROR_MESSAGE);
        }

        User fromUser = userRepository
                .findByUsername(loggedInUsername)
                .filter(userValidation::isValid)
                .orElseThrow(() -> new CustomException(SERVER_ERROR_MESSAGE));

        User toUser = userRepository
                .findById(messageCreateBindingModel.getToUserId())
                .filter(userValidation::isValid)
                .orElseThrow(() -> new CustomException(SERVER_ERROR_MESSAGE));


        Relationship relationship = relationshipRepository
                .findRelationshipWithFriendWithStatus(fromUser.getId(), toUser.getId(), 1);

        if (!relationshipValidation.isValid(relationship)) {
            throw new CustomException(SERVER_ERROR_MESSAGE);
        }

        MessageServiceModel messageServiceModel = new MessageServiceModel();
        messageServiceModel.setContent(messageCreateBindingModel.getContent());
        messageServiceModel.setFromUser(fromUser);
        messageServiceModel.setToUser(toUser);
        messageServiceModel.setRelationship(relationship);
        messageServiceModel.setTime(LocalDateTime.now());

        Message message = this.modelMapper.map(messageServiceModel, Message.class);

        Message savedMessage = messageRepository.save(message);

        return  this.modelMapper.map(savedMessage, MessageServiceModel.class);

    }

    @Override
    public List<MessageServiceModel> getAllMessages(String loggedInUsername, String chatUserId) {
        User loggedInUser = userRepository
                .findByUsername(loggedInUsername)
                .filter(userValidation::isValid)
                .orElseThrow(() -> new CustomException(SERVER_ERROR_MESSAGE));

        User chatUser = userRepository
                .findById(chatUserId)
                .filter(userValidation::isValid)
                .orElseThrow(() -> new CustomException(SERVER_ERROR_MESSAGE));

        List<Message> allMessagesBetweenTwoUsers = this.messageRepository
                .findAllMessagesBetweenTwoUsers(loggedInUser.getId(), chatUser.getId());

        this.updateMessageStatus(loggedInUser.getId(), chatUserId);

        return allMessagesBetweenTwoUsers
                .stream().map(message -> modelMapper.map(message, MessageServiceModel.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<MessageFriendsViewModel> getAllFriendMessages(String loggedInUsername) {
        User loggedInUser = userRepository
                .findByUsername(loggedInUsername)
                .filter(userValidation::isValid)
                .orElseThrow(() -> new CustomException(SERVER_ERROR_MESSAGE));

        List<Message> allUnreadMessages = this.messageRepository.getAllUnreadMessages(loggedInUser.getId());

        List<MessageServiceModel> messageServiceModels = allUnreadMessages.stream()
                .map(message -> modelMapper.map(message, MessageServiceModel.class)).toList();

        List<MessageServiceModel> allFriendsMessages =
                messageServiceModels.stream()
                        .filter(message -> message.getRelationship().getStatus() == 1)
                        .collect(Collectors.toList());

        return mapToMessageFriendsViewModel(loggedInUser.getId(), allFriendsMessages);
    }

    private List<MessageFriendsViewModel> mapToMessageFriendsViewModel(String loggedInUserId, List<MessageServiceModel> allUnreadMessages) {
        List<Object[]> countOfUnreadMessagesByFromUser = this.messageRepository.getCountOfUnreadMessagesByFromUser(loggedInUserId);

        return allUnreadMessages.stream()
                .map(messageServiceModel -> {
                    MessageFriendsViewModel unreadViewModel = modelMapper.map(messageServiceModel, MessageFriendsViewModel.class);
                    Object[] objects = countOfUnreadMessagesByFromUser.stream()
                            .filter(element -> element[0].equals(unreadViewModel.getFromUserId()))
                            .findFirst().orElse(null);

                    if(objects != null){
                        unreadViewModel.setCount(Integer.parseInt(objects[1].toString()));
                    }else{
                        unreadViewModel.setCount(0);
                    }

                    return unreadViewModel;
                })
                .collect(Collectors.toList());
    }

    @Transactional
    @Modifying
    private void updateMessageStatus(String loggedInUserId, String chatUserId) {
        this.messageRepository.updateStatusFromReadMessages(loggedInUserId, chatUserId);
    }
}
