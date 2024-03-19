package org.pinsoft.friendapp.web.controllers;

import jakarta.servlet.ServletContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.pinsoft.friendapp.domain.dto.message.MessageAllViewModel;
import org.pinsoft.friendapp.domain.dto.message.MessageFriendsViewModel;
import org.pinsoft.friendapp.domain.dto.message.MessageServiceModel;
import org.pinsoft.friendapp.domain.repo.entity.Relationship;
import org.pinsoft.friendapp.domain.repo.entity.UserEntity;
import org.pinsoft.friendapp.controller.MessageController;
import org.pinsoft.friendapp.service.MessageService;
import org.pinsoft.friendapp.testUtils.MessagesUtils;
import org.pinsoft.friendapp.testUtils.RelationshipsUtils;
import org.pinsoft.friendapp.testUtils.UsersUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockServletContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = MessageController.class)
//@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class MessageControllerTests {
    private MockMvc mvc;

    @Autowired
    private WebApplicationContext context;

    @MockBean
    private MessageService mockMessageService;

    @BeforeEach
    public void setup() {
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .alwaysDo(print())
                .build();
    }

    @Test
    public void givenWac_whenServletContext_thenItProvidesPostController() {
        ServletContext servletContext = context.getServletContext();

        assertNotNull(servletContext);
        assertTrue(servletContext instanceof MockServletContext);
        assertNotNull(context.getBean("messageController"));
    }

//    getAllMessages

    @Test
    public void getAllMessages_whenUnAuthorized_403Forbidden() throws Exception {
        this.mvc
                .perform(get("/message/all/{id}", "1"))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    public void getAllMessages_when2Messages_2Messages() throws Exception {
        List<UserEntity> users = UsersUtils.getUsers(2);
        UserEntity firstUser = users.get(0);
        UserEntity secondUser = users.get(1);

        Relationship relationship = RelationshipsUtils.createRelationship(firstUser, secondUser, 1, firstUser);

        List<MessageServiceModel> messageServiceModels = MessagesUtils.getMessageServiceModels(2, firstUser, secondUser, relationship);

        List<MessageAllViewModel> messageAllViewModels = MessagesUtils.getMessageAllViewModels(2);

        when(this.mockMessageService.getAllMessages(anyString(), anyString()))
                .thenReturn(messageServiceModels);

        this.mvc
                .perform(get("/message/all/{id}", "1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(content().contentTypeCompatibleWith("application/json"))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(messageAllViewModels.get(0).getId())))
                .andExpect(jsonPath("$[0].fromUserId", is(messageAllViewModels.get(0).getFromUserId())))
                .andExpect(jsonPath("$[0].content", is(messageAllViewModels.get(0).getContent())));

        verify(this.mockMessageService, times(1)).getAllMessages(anyString(), anyString());
        verifyNoMoreInteractions(this.mockMessageService);
    }

    @Test
    public void getAllMessages_when0Messages_EmptyCollection() throws Exception {
        // Arrange
        when(this.mockMessageService.getAllMessages(anyString(), anyString()))
                .thenReturn(new ArrayList<>());

        // Act
        this.mvc
                .perform(get("/message/all/{id}", "1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().contentTypeCompatibleWith("application/json"))
                .andExpect(jsonPath("$", hasSize(0)));

        verify(this.mockMessageService, times(1)).getAllMessages(anyString(), anyString());
        verifyNoMoreInteractions(this.mockMessageService);
    }

//    getAllFriendMessages

    @Test()
    public void getAllFriendMessages_whenUnAuthorized_403Forbidden() throws Exception {
        this.mvc
                .perform(get("/message/friend"))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    public void getAllFriendMessages_when2Messages_2Messages() throws Exception {
        List<MessageFriendsViewModel> messageFriendsViewModels = MessagesUtils.getMessageFriendsViewModels(2);

        when(this.mockMessageService.getAllFriendMessages(anyString()))
                .thenReturn(messageFriendsViewModels);

        this.mvc
                .perform(get("/message/friend"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().contentTypeCompatibleWith("application/json"))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(messageFriendsViewModels.get(0).getId())))
                .andExpect(jsonPath("$[0].fromUserId", is(messageFriendsViewModels.get(0).getFromUserId())))
                .andExpect(jsonPath("$[0].fromUserFirstName", is(messageFriendsViewModels.get(0).getFromUserFirstName())))
                .andExpect(jsonPath("$[0].fromUserLastName", is(messageFriendsViewModels.get(0).getFromUserLastName())))
                .andExpect(jsonPath("$[0].content", is(messageFriendsViewModels.get(0).getContent())))
                .andExpect(jsonPath("$[0].count", is(messageFriendsViewModels.get(0).getCount())));

        verify(this.mockMessageService, times(1)).getAllFriendMessages(anyString());
        verifyNoMoreInteractions(this.mockMessageService);
    }

    @Test
    public void getAllFriendMessages_when0Messages_EmptyCollection() throws Exception {
        when(this.mockMessageService.getAllFriendMessages(anyString()))
                .thenReturn(new ArrayList<>());

        this.mvc
                .perform(get("/message/friend"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(content().contentTypeCompatibleWith("application/json"))
                .andExpect(jsonPath("$", hasSize(0)));

        verify(this.mockMessageService, times(1)).getAllFriendMessages(anyString());
        verifyNoMoreInteractions(this.mockMessageService);
    }

}
