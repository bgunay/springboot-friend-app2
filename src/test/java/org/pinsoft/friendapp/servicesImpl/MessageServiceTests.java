package org.pinsoft.friendapp.servicesImpl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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
import org.pinsoft.friendapp.testUtils.MessagesUtils;
import org.pinsoft.friendapp.testUtils.RelationshipsUtils;
import org.pinsoft.friendapp.testUtils.UsersUtils;
import org.pinsoft.friendapp.utils.responseHandler.exceptions.CustomException;
import org.pinsoft.friendapp.utils.validations.serviceValidation.services.MessageValidationService;
import org.pinsoft.friendapp.utils.validations.serviceValidation.services.RelationshipValidationService;
import org.pinsoft.friendapp.utils.validations.serviceValidation.services.UserValidationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.pinsoft.friendapp.utils.constants.ResponseMessageConstants.*;

@SpringBootTest
public class MessageServiceTests {

    @Autowired
    private MessageService messageService;

    @MockBean
    private MessageRepository mockMessageRepository;

    @MockBean
    private MessageValidationService mockMessageValidation;

    @MockBean
    private RelationshipValidationService mockRelationshipValidation;

    @MockBean
    private RelationshipRepository mockRelationshipRepository;

    @MockBean
    private UserRepository mockUserRepository;

    @MockBean
    private UserValidationService mockUserValidationService;


    private List<Message> messageList;

    @BeforeEach
    public void setUpTest() {
        messageList = new ArrayList<>();
    }

    @Test
    public void getAllMessages_when2Messages_2Messages() {
        // Arrange
        List<UserEntity> users = UsersUtils.getUsers(2);
        UserEntity firstUser = users.get(0);
        UserEntity secondUser = users.get(1);
        Relationship relationship = RelationshipsUtils.createRelationship(firstUser, secondUser, 1, firstUser);
        List<Message> messages = MessagesUtils.getMessages(2, firstUser, secondUser, relationship);

        messageList.addAll(messages);

        when(mockUserRepository.findByUsername(anyString()))
                .thenReturn(java.util.Optional.ofNullable(firstUser));

        when(mockUserRepository.findById(anyString()))
                .thenReturn(java.util.Optional.ofNullable(secondUser));


        when(mockUserValidationService.isValid(any(UserEntity.class)))
                .thenReturn(true);

        when(mockMessageRepository.findAllMessagesBetweenTwoUsers(anyString(), anyString()))
                .thenReturn(messageList);


        // Act
        List<MessageServiceModel> allMessages = messageService.getAllMessages("username", "userId");

        // Assert
        Message expected = messageList.get(0);
        MessageServiceModel actual = allMessages.get(0);

        assertEquals(2, allMessages.size());
        assertEquals(expected.getContent(), actual.getContent());
        assertEquals(expected.getFromUser().getId(), actual.getFromUser().getId());
        assertEquals(expected.getToUser().getId(), actual.getToUser().getId());
        assertEquals(expected.getStatus(), actual.getStatus());
        assertEquals(expected.getSubject(), actual.getSubject());

        verify(mockMessageRepository).findAllMessagesBetweenTwoUsers(anyString(), anyString());
        verify(mockMessageRepository, times(1)).findAllMessagesBetweenTwoUsers(anyString(), anyString());
    }

    @Test
    public void getAllMessages_whenZeroMessages_returnEmptyCollection() {
        // Arrange
        messageList.clear();

        List<UserEntity> users = UsersUtils.getUsers(2);
        UserEntity firstUser = users.get(0);
        UserEntity secondUser = users.get(1);

        when(mockUserRepository.findByUsername(anyString()))
                .thenReturn(java.util.Optional.ofNullable(firstUser));

        when(mockUserRepository.findById(anyString()))
                .thenReturn(java.util.Optional.ofNullable(secondUser));

        when(mockUserValidationService.isValid(any(UserEntity.class)))
                .thenReturn(true);

        when(mockMessageRepository.findAllMessagesBetweenTwoUsers(anyString(), anyString()))
                .thenReturn(messageList);

        // Act
        List<MessageServiceModel> allMessages = messageService.getAllMessages("username", "userId");

        // Assert
        assertTrue(allMessages.isEmpty());
        verify(mockMessageRepository).findAllMessagesBetweenTwoUsers(anyString(), anyString());
        verify(mockMessageRepository, times(1)).findAllMessagesBetweenTwoUsers(anyString(), anyString());
    }

    @Test
    public void getAllMessages_whenFromUserIsNotValid_throwException() {
        // Arrange
        List<UserEntity> users = UsersUtils.getUsers(2);
        UserEntity firstUser = users.get(0);
        UserEntity secondUser = users.get(1);
        Relationship relationship = RelationshipsUtils.createRelationship(firstUser, secondUser, 1, firstUser);
        List<Message> messages = MessagesUtils.getMessages(2, firstUser, secondUser, relationship);

        messageList.addAll(messages);

        when(mockUserRepository.findByUsername(anyString()))
                .thenReturn(java.util.Optional.ofNullable(firstUser));

        when(mockUserRepository.findById(anyString()))
                .thenReturn(java.util.Optional.ofNullable(secondUser));

        when(mockUserValidationService.isValid(firstUser))
                .thenReturn(false);

        when(mockUserValidationService.isValid(secondUser))
                .thenReturn(false);

        when(mockMessageRepository.findAllMessagesBetweenTwoUsers(anyString(), anyString()))
                .thenReturn(messageList);


        CustomException customException = Assertions.assertThrows(CustomException.class, () -> {
            messageService.getAllMessages("username", "userId");
        });
        assertTrue(customException.getMessage().contains(USER_NOT_FOUND_ERROR_MESSAGE));

    }

    @Test
    public void getAllMessages_whenToUserIsNotValid_throwException() {
        // Arrange
        List<UserEntity> users = UsersUtils.getUsers(2);
        UserEntity firstUser = users.get(0);
        UserEntity secondUser = users.get(1);
        Relationship relationship = RelationshipsUtils.createRelationship(firstUser, secondUser, 1, firstUser);
        List<Message> messages = MessagesUtils.getMessages(2, firstUser, secondUser, relationship);

        messageList.addAll(messages);

        when(mockUserRepository.findByUsername(anyString()))
                .thenReturn(java.util.Optional.ofNullable(firstUser));

        when(mockUserRepository.findById(anyString()))
                .thenReturn(java.util.Optional.ofNullable(secondUser));

        when(mockUserValidationService.isValid(firstUser))
                .thenReturn(true);

        when(mockUserValidationService.isValid(secondUser))
                .thenReturn(false);

        when(mockMessageRepository.findAllMessagesBetweenTwoUsers(anyString(), anyString()))
                .thenReturn(messageList);

        CustomException customException = Assertions.assertThrows(CustomException.class, () -> {
            List<MessageServiceModel> allMessages = messageService.getAllMessages("username", "userId");
        });
        assertTrue(  customException.getMessage().contains("User can't found with"));

    }

    //    getAllFriendMessages
    @Test
    public void getAllFriendMessages_when2Messages_2Messages() {
        // Arrange
        List<UserEntity> users = UsersUtils.getUsers(2);
        UserEntity firstUser = users.get(0);
        UserEntity secondUser = users.get(1);
        UserEntity thirdUser = UsersUtils.createUser();
        thirdUser.setId("5");
        UserEntity fourthUser = UsersUtils.createUser();
        thirdUser.setId("6");

        Relationship relationship = RelationshipsUtils.createRelationship(firstUser, secondUser, 1, firstUser);
        List<Message> messages = MessagesUtils.getMessages(2, firstUser, secondUser, relationship);
        Message message3 = MessagesUtils.createMessage(thirdUser, firstUser, relationship);
        Message message4 = MessagesUtils.createMessage(fourthUser, thirdUser, relationship);

        messageList.addAll(messages);
        messageList.add(message3);
        messageList.add(message4);

        List<MessageFriendsViewModel> messageFriendsViewModels = MessagesUtils.getMessageFriendsViewModels(4);
        List<Object[]> countOfUnreadMessagesObjectArr = MessagesUtils.getCountOfUnreadMessagesObjectArr(2);

        when(mockUserRepository.findById(anyString()))
                .thenReturn(java.util.Optional.ofNullable(firstUser));

        when(mockUserValidationService.isValid(any(UserEntity.class)))
                .thenReturn(true);

        when(mockMessageRepository.getAllUnreadMessages(anyString()))
                .thenReturn(messageList);

        when(mockMessageRepository.getCountOfUnreadMessagesByFromUser(anyString()))
                .thenReturn(countOfUnreadMessagesObjectArr);

        // Act
        List<MessageFriendsViewModel> allFriendMessages = messageService.getAllFriendMessages("userId");

        // Assert
        MessageFriendsViewModel expected = messageFriendsViewModels.get(0);
        MessageFriendsViewModel actual = allFriendMessages.get(0);

        assertEquals(4, allFriendMessages.size());
        assertEquals(expected.getContent(), actual.getContent());
        assertEquals(expected.getFromUserId(), actual.getFromUserId());
        assertEquals(1, actual.getCount());

        verify(mockMessageRepository).getAllUnreadMessages(anyString());
        verify(mockMessageRepository, times(1)).getAllUnreadMessages(anyString());
    }

    @Test
    public void getAllFriendMessages_whenZeroFriendsMessages_returnEmptyCollection() {
        // Arrange
        List<UserEntity> users = UsersUtils.getUsers(2);
        UserEntity firstUser = users.get(0);
        UserEntity secondUser = users.get(1);
        UserEntity thirdUser = UsersUtils.createUser();
        thirdUser.setId("5");
        UserEntity fourthUser = UsersUtils.createUser();
        thirdUser.setId("6");

        Relationship relationship = RelationshipsUtils.createRelationship(firstUser, secondUser, 0, firstUser);
        List<Message> messages = MessagesUtils.getMessages(2, firstUser, secondUser, relationship);
        Message message3 = MessagesUtils.createMessage(thirdUser, firstUser, relationship);
        Message message4 = MessagesUtils.createMessage(fourthUser, thirdUser, relationship);

        messageList.addAll(messages);
        messageList.add(message3);
        messageList.add(message4);

        List<MessageFriendsViewModel> messageFriendsViewModels = MessagesUtils.getMessageFriendsViewModels(4);
        List<Object[]> countOfUnreadMessagesObjectArr = MessagesUtils.getCountOfUnreadMessagesObjectArr(2);

        when(mockUserRepository.findById(anyString()))
                .thenReturn(java.util.Optional.ofNullable(firstUser));

        when(mockUserValidationService.isValid(any(UserEntity.class)))
                .thenReturn(true);

        when(mockMessageRepository.getAllUnreadMessages(anyString()))
                .thenReturn(messageList);

        when(mockMessageRepository.getCountOfUnreadMessagesByFromUser(anyString()))
                .thenReturn(countOfUnreadMessagesObjectArr);

        // Act
        List<MessageFriendsViewModel> allFriendMessages = messageService.getAllFriendMessages("userId");

        // Assert
        assertTrue(allFriendMessages.isEmpty());

        verify(mockMessageRepository).getAllUnreadMessages(anyString());
        verify(mockMessageRepository, times(1)).getAllUnreadMessages(anyString());
    }

    @Test
    public void getAllFriendMessages_whenZeroMessages_returnEmptyCollection() {
        // Arrange
        messageList.clear();

        List<UserEntity> users = UsersUtils.getUsers(2);
        UserEntity firstUser = users.get(0);

        List<Object[]> countOfUnreadMessagesObjectArr = MessagesUtils.getCountOfUnreadMessagesObjectArr(2);

        when(mockUserRepository.findById(anyString()))
                .thenReturn(java.util.Optional.ofNullable(firstUser));

        when(mockUserValidationService.isValid(any(UserEntity.class)))
                .thenReturn(true);

        when(mockMessageRepository.getAllUnreadMessages(anyString()))
                .thenReturn(messageList);

        when(mockMessageRepository.getCountOfUnreadMessagesByFromUser(anyString()))
                .thenReturn(countOfUnreadMessagesObjectArr);

        // Act
        List<MessageFriendsViewModel> allFriendMessages = messageService.getAllFriendMessages("username");

        // Assert
        assertTrue(allFriendMessages.isEmpty());

        verify(mockMessageRepository).getAllUnreadMessages(anyString());
        verify(mockMessageRepository, times(1)).getAllUnreadMessages(anyString());
    }

    @Test
    public void getAllFriendMessages_whenUserIsNotValid_throwException() {
        // Arrange
        when(mockUserRepository.findByUsername(anyString()))
                .thenReturn(java.util.Optional.of(new UserEntity()));

        when(mockUserValidationService.isValid(any(UserEntity.class)))
                .thenReturn(false);

        CustomException customException = Assertions.assertThrows(CustomException.class, () -> {
            messageService.getAllFriendMessages("userId");
        });
        assertEquals(SERVER_ERROR_MESSAGE, customException.getMessage());

    }

    // createMessage

    @Test
    public void createMessage_whenAllInputsAreValid_createMessage() throws Exception {
        // Arrange
        List<UserEntity> users = UsersUtils.getUsers(2);
        Relationship relationship = RelationshipsUtils.createRelationship(users.get(0), users.get(1), 1, users.get(0));
        MessageCreateBindingModel messageCreateBindingModel = MessagesUtils.getMessageCreateBindingModel();
        Message message = MessagesUtils.createMessage(users.get(0), users.get(1), relationship);

        when(mockMessageValidation.isValid(any(MessageCreateBindingModel.class)))
                .thenReturn(true);

        when(mockUserRepository.findByUsername(any()))
                .thenReturn(java.util.Optional.ofNullable(users.get(0)));

        when(mockUserRepository.findByUsername(any()))
                .thenReturn(java.util.Optional.of(users.get(1)));


        when(mockUserValidationService.isValid(users.get(0)))
                .thenReturn(true);

        when(mockUserValidationService.isValid(users.get(1)))
                .thenReturn(true);

        when(mockRelationshipRepository.findRelationshipWithFriendWithStatus(anyString(), anyString(), anyInt()))
                .thenReturn(relationship);

        when(mockRelationshipValidation.isValid(any(Relationship.class)))
                .thenReturn(true);

        when(mockMessageRepository.save(any())).thenReturn(message);
        // Act
        messageService.createMessage(messageCreateBindingModel, "username");

        // Assert
        verify(mockMessageRepository).save(any());
        verifyNoMoreInteractions(mockMessageRepository);
    }

    @Test
    public void createMessage_whenMessageCreateBindingModelIsNotValid_throwException() throws Exception {
        // Arrange
        MessageCreateBindingModel messageCreateBindingModel = MessagesUtils.getMessageCreateBindingModel();

        when(mockMessageValidation.isValid(any(MessageCreateBindingModel.class)))
                .thenReturn(false);

        CustomException customException = Assertions.assertThrows(CustomException.class, () -> {
            messageService.createMessage(messageCreateBindingModel, "username");
        });
        assertEquals(INVALID_MESSAGE_FORMAT, customException.getMessage());
    }

    @Test
    public void createMessage_whenFromUserIsNotValid_throwException() throws Exception {
        // Arrange
        List<UserEntity> users = UsersUtils.getUsers(2);
        MessageCreateBindingModel messageCreateBindingModel = MessagesUtils.getMessageCreateBindingModel();

        when(mockMessageValidation.isValid(any(MessageCreateBindingModel.class)))
                .thenReturn(true);

        when(mockUserRepository.findByUsername(any()))
                .thenReturn(java.util.Optional.ofNullable(users.get(0)));

        when(mockUserRepository.findByUsername(any()))
                .thenReturn(java.util.Optional.of(users.get(1)));


        when(mockUserValidationService.isValid(users.get(0)))
                .thenReturn(false);

        CustomException customException = Assertions.assertThrows(CustomException.class, () -> {
            messageService.createMessage(messageCreateBindingModel, "username");
        });
        assertEquals(MESSAGE_FROM_USER_INVALID, customException.getMessage());
    }

    @Test
    public void createMessage_whenToUserIsNotValid_throwException() throws Exception {
        // Arrange
        List<UserEntity> users = UsersUtils.getUsers(2);
        MessageCreateBindingModel messageCreateBindingModel = MessagesUtils.getMessageCreateBindingModel();

        when(mockMessageValidation.isValid(any(MessageCreateBindingModel.class)))
                .thenReturn(true);

        when(mockUserRepository.findByUsername(any()))
                .thenReturn(java.util.Optional.ofNullable(users.get(0)));

        when(mockUserRepository.findByUsername(any()))
                .thenReturn(java.util.Optional.of(users.get(1)));


        when(mockUserValidationService.isValid(users.get(0)))
                .thenReturn(true);

        when(mockUserValidationService.isValid(users.get(1)))
                .thenReturn(false);

        CustomException customException = Assertions.assertThrows(CustomException.class, () -> {
            messageService.createMessage(messageCreateBindingModel, "username");
        });
        assertEquals(MESSAGE_FROM_USER_INVALID, customException.getMessage());


    }

    @Test
    public void createMessage_whenRelationshipIsNotValid_throwException() throws Exception {
        // Arrange
        List<UserEntity> users = UsersUtils.getUsers(2);
        Relationship relationship = RelationshipsUtils.createRelationship(users.get(0), users.get(1), 1, users.get(0));
        MessageCreateBindingModel messageCreateBindingModel = MessagesUtils.getMessageCreateBindingModel();

        when(mockMessageValidation.isValid(any(MessageCreateBindingModel.class)))
                .thenReturn(true);

        when(mockUserRepository.findByUsername(any()))
                .thenReturn(java.util.Optional.ofNullable(users.get(0)));

        when(mockUserRepository.findByUsername(any()))
                .thenReturn(java.util.Optional.of(users.get(1)));


        when(mockUserValidationService.isValid(users.get(0)))
                .thenReturn(true);

        when(mockUserValidationService.isValid(users.get(1)))
                .thenReturn(true);

        when(mockRelationshipRepository.findRelationshipWithFriendWithStatus(anyString(), anyString(), anyInt()))
                .thenReturn(relationship);

        when(mockRelationshipValidation.isValid(any(Relationship.class)))
                .thenReturn(false);

        CustomException customException = Assertions.assertThrows(CustomException.class, () -> {
            messageService.createMessage(messageCreateBindingModel, "username");
        });
        assertEquals(RELATIONSHIP_INVALID_MESSAGE, customException.getMessage());
    }

    @Test
    public void createMessage_whenSaveMessageReturnsNull_throwException() throws Exception {
        // Arrange
        List<UserEntity> users = UsersUtils.getUsers(2);
        Relationship relationship = RelationshipsUtils.createRelationship(users.get(0), users.get(1), 1, users.get(0));
        MessageCreateBindingModel messageCreateBindingModel = MessagesUtils.getMessageCreateBindingModel();
        Message message = MessagesUtils.createMessage(users.get(0), users.get(1), relationship);

        when(mockMessageValidation.isValid(any(MessageCreateBindingModel.class)))
                .thenReturn(true);

        when(mockUserRepository.findByUsername(any()))
                .thenReturn(java.util.Optional.ofNullable(users.get(0)));

        when(mockUserRepository.findByUsername(any()))
                .thenReturn(java.util.Optional.of(users.get(1)));


        when(mockUserValidationService.isValid(users.get(0)))
                .thenReturn(true);

        when(mockUserValidationService.isValid(users.get(1)))
                .thenReturn(true);

        when(mockRelationshipRepository.findRelationshipWithFriendWithStatus(anyString(), anyString(), anyInt()))
                .thenReturn(relationship);

        when(mockRelationshipValidation.isValid(any(Relationship.class)))
                .thenReturn(true);

        when(mockMessageRepository.save(any())).thenReturn(new Message());

        CustomException customException = Assertions.assertThrows(CustomException.class, () -> {
            messageService.createMessage(messageCreateBindingModel, "username");
        });
        assertEquals(MESSAGE_SAVE_FAILURE_MESSAGE, customException.getMessage());

    }
}
