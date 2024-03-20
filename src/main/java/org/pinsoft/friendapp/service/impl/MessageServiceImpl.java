package org.pinsoft.friendapp.service.impl;

import io.micrometer.common.util.StringUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.pinsoft.friendapp.domain.dto.message.MessageCreateBindingModel;
import org.pinsoft.friendapp.domain.dto.message.MessageFriendsViewModel;
import org.pinsoft.friendapp.domain.dto.message.MessageServiceModel;
import org.pinsoft.friendapp.domain.repo.MessageRepository;
import org.pinsoft.friendapp.domain.repo.RelationshipRepository;
import org.pinsoft.friendapp.domain.repo.UserRepository;
import org.pinsoft.friendapp.domain.repo.entity.Message;
import org.pinsoft.friendapp.domain.repo.entity.Relationship;
import org.pinsoft.friendapp.domain.repo.entity.UserEntity;
import org.pinsoft.friendapp.service.MessageService;
import org.pinsoft.friendapp.utils.responseHandler.exceptions.CustomException;
import org.pinsoft.friendapp.utils.validations.serviceValidation.services.MessageValidationService;
import org.pinsoft.friendapp.utils.validations.serviceValidation.services.RelationshipValidationService;
import org.pinsoft.friendapp.utils.validations.serviceValidation.services.UserValidationService;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static org.pinsoft.friendapp.utils.constants.ResponseMessageConstants.*;
import static org.pinsoft.friendapp.web.websocket.WebSocketEventName.CHAT_LOGS;


@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class MessageServiceImpl implements MessageService {
    private final MessageRepository messageRepository;
    private final RelationshipRepository relationshipRepository;
    private final UserRepository userRepository;
    private final UserValidationService userValidation;
    private final MessageValidationService messageValidation;
    private final RelationshipValidationService relationshipValidation;
    private final ModelMapper modelMapper;
    private final SimpMessagingTemplate template;

    @Override
    public MessageServiceModel createMessage(MessageCreateBindingModel messageCreateBindingModel, String fromUserName) {
        if (!messageValidation.isValid(messageCreateBindingModel)) {
            throw new CustomException(INVALID_MESSAGE_FORMAT);
        }

        if(StringUtils.isBlank(fromUserName)){
            fromUserName = messageCreateBindingModel.getToUserId();
        }

        UserEntity fromUser = userRepository
                .findByUsername(fromUserName)
                .filter(userValidation::isValid)
                .orElseThrow(() -> new CustomException(MESSAGE_FROM_USER_INVALID));

        UserEntity toUser = userRepository
                .findByUsername(messageCreateBindingModel.getToUserId())
                .filter(userValidation::isValid)
                .orElseThrow(() -> new CustomException(MESSAGE_TO_USER_INVALID));


        Relationship relationship = relationshipRepository
                .findRelationshipWithFriendWithStatus(fromUser.getId(), toUser.getId(), 1);

        if (!relationshipValidation.isValid(relationship)) {
            template.convertAndSend(CHAT_LOGS.getDestination(), RELATIONSHIP_INVALID_MESSAGE);
            throw new CustomException(RELATIONSHIP_INVALID_MESSAGE);
        }

        MessageServiceModel messageServiceModel = new MessageServiceModel();
        messageServiceModel.setContent(messageCreateBindingModel.getContent());
        messageServiceModel.setFromUser(fromUser);
        messageServiceModel.setToUser(toUser);
        messageServiceModel.setRelationship(relationship);
        messageServiceModel.setTime(LocalDateTime.now());

        Message message = this.modelMapper.map(messageServiceModel, Message.class);

        Message savedMessage = messageRepository.save(message);

        if(savedMessage.getId() != null){
            return  this.modelMapper.map(savedMessage, MessageServiceModel.class);
        } else {
            throw new CustomException(MESSAGE_SAVE_FAILURE_MESSAGE);
        }

    }

    @Override
    public List<MessageServiceModel> getAllMessages(String loggedInUsername, String chatUserId) {
        UserEntity loggedInUser = userRepository
                .findById(loggedInUsername)
                .filter(userValidation::isValid)
                .orElseThrow(() -> new CustomException(USER_NOT_FOUND_ERROR_MESSAGE + " with ID" + loggedInUsername));

        UserEntity chatUser = userRepository
                .findById(chatUserId)
                .filter(userValidation::isValid)
                .orElseThrow(() -> new CustomException(USER_NOT_FOUND_ERROR_MESSAGE + " with ID" + loggedInUsername));

        List<Message> allMessagesBetweenTwoUsers = this.messageRepository
                .findAllMessagesBetweenTwoUsers(loggedInUser.getId(), chatUser.getId());


        if (allMessagesBetweenTwoUsers.size() > 0) {
            for (Message msg : allMessagesBetweenTwoUsers) {
                log.info("Message read" + msg.getSubject());
                this.updateMessageStatus(loggedInUsername, chatUserId);
            }
        }


        return allMessagesBetweenTwoUsers
                .stream().map(message -> modelMapper.map(message, MessageServiceModel.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<MessageFriendsViewModel> getAllFriendMessages(String userId) {
        UserEntity loggedInUser = userRepository
                .findById(userId)
                .filter(userValidation::isValid)
                .orElseThrow(() -> new CustomException(SERVER_ERROR_MESSAGE));

        List<Message> allUnreadMessages = this.messageRepository.getAllUnreadMessages(loggedInUser.getId());

        List<MessageServiceModel> messageServiceModels = allUnreadMessages.stream()
                .map(message -> modelMapper.map(message, MessageServiceModel.class))
                .collect(Collectors.toList());

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

                    if (objects != null) {
                        unreadViewModel.setCount(Integer.parseInt(objects[1].toString()));
                    } else {
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
