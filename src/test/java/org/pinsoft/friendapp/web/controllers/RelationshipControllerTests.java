package org.pinsoft.friendapp.web.controllers;

import jakarta.servlet.ServletContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.pinsoft.friendapp.domain.dto.relationship.FriendsCandidatesViewModel;
import org.pinsoft.friendapp.service.RelationshipService;
import org.pinsoft.friendapp.testUtils.RelationshipsUtils;
import org.pinsoft.friendapp.testUtils.TestUtil;
import org.pinsoft.friendapp.utils.responseHandler.exceptions.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockServletContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.pinsoft.friendapp.utils.constants.ResponseMessageConstants.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ContextConfiguration
@WebAppConfiguration
@SpringBootTest
public class RelationshipControllerTests {
    private MockMvc mvc;

    @Autowired
    private WebApplicationContext context;

    @MockBean
    private RelationshipService mockRelationshipService;

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
        assertNotNull(context.getBean("relationshipController"));
    }




    @Test
    public void findAllNotFriends_when2NotFriends_2NotFriends() throws Exception {
        List<FriendsCandidatesViewModel> friendsCandidatesViewModel = RelationshipsUtils.getFriendsCandidatesViewModel(2);

        when(this.mockRelationshipService.findAllFriendCandidates(anyString()))
                .thenReturn(friendsCandidatesViewModel);

        this.mvc
                .perform(get("/relationship/findFriends/{id}", "1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(content().contentTypeCompatibleWith("application/json"))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is("1")))
                .andExpect(jsonPath("$[0].username", is("ali 0")))
                .andExpect(jsonPath("$[0].firstName", is("Ali 0")))
                .andExpect(jsonPath("$[0].lastName", is("Aliv 0")))
                .andExpect(jsonPath("$[0].starterOfAction", is(false)))
                .andExpect(jsonPath("$[0].status", is(2)))
                .andExpect(jsonPath("$[1].id", is("2")))
                .andExpect(jsonPath("$[1].username", is("ali 1")))
                .andExpect(jsonPath("$[1].firstName", is("Ali 1")))
                .andExpect(jsonPath("$[1].lastName", is("Aliv 1")))
                .andExpect(jsonPath("$[0].starterOfAction", is(false)))
                .andExpect(jsonPath("$[0].status", is(2)));

        verify(this.mockRelationshipService, times(1)).findAllFriendCandidates(anyString());
        verifyNoMoreInteractions(this.mockRelationshipService);
    }


    @Test
    public void addFriend_whenCreateRequestForAddingFriendReturnsTrue_addFriend() throws Exception {
        Map<String, Object> body = new HashMap<>();
        body.put("fromUser", "1");
        body.put("toUser", "2");

        when(mockRelationshipService.createRequestForAddingFriend(anyString(), anyString()))
                .thenReturn(true);

        this.mvc
                .perform(post("/relationship/addFriend")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtil.convertObjectToJsonString(body)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestUtil.TEXT_PLAIN_UTF8))
                .andExpect(jsonPath("$.success").value("true"))
                .andExpect(jsonPath("$.message").value(SUCCESSFUL_FRIEND_REQUEST_SUBMISSION_MESSAGE));

        verify(mockRelationshipService, times(1)).createRequestForAddingFriend(anyString(), anyString());
        verifyNoMoreInteractions(mockRelationshipService);
    }

    @Test
    public void addFriend_whenCreateRequestForAddingFriendReturnsFalse_throwException() throws Exception {
        Map<String, Object> body = new HashMap<>();
        body.put("fromUser", "1");
        body.put("toUser", "2");

        when(mockRelationshipService.createRequestForAddingFriend(anyString(), anyString()))
                .thenReturn(false);

        Exception resolvedException = this.mvc
                .perform(post("/relationship/addFriend")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(TestUtil.convertObjectToJsonString(body)))
                .andDo(print())
                .andExpect(status().isInternalServerError())
                .andReturn().getResolvedException();

        assertEquals(SERVER_ERROR_MESSAGE, resolvedException.getMessage());
        assertEquals(CustomException.class, resolvedException.getClass());

        verify(mockRelationshipService, times(1)).createRequestForAddingFriend(anyString(), anyString());
        verifyNoMoreInteractions(mockRelationshipService);
    }

    // removeFriend

    @Test
    public void removeFriend_whenRemoveFriendReturnsTrue_removeFriend() throws Exception {
        Map<String, Object> body = new HashMap<>();
        body.put("fromUser", "1");
        body.put("toUser", "2");

        when(mockRelationshipService.removeFriend(anyString(), anyString()))
                .thenReturn(true);

        this.mvc
                .perform(post("/relationship/removeFriend")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(TestUtil.convertObjectToJsonString(body)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestUtil.TEXT_PLAIN_UTF8))
                .andExpect(jsonPath("$.success").value("true"))
                .andExpect(jsonPath("$.message").value(SUCCESSFUL_FRIEND_REMOVE_MESSAGE));

        verify(mockRelationshipService, times(1)).removeFriend(anyString(), anyString());
        verifyNoMoreInteractions(mockRelationshipService);
    }

    @Test
    public void removeFriend_whenRemoveFriendReturnsFalse_throwException() throws Exception {
        Map<String, Object> body = new HashMap<>();
        body.put("fromUser", "1");
        body.put("toUser", "2");

        when(mockRelationshipService.removeFriend(anyString(), anyString()))
                .thenReturn(false);

        Exception resolvedException = this.mvc
                .perform(post("/relationship/removeFriend")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(TestUtil.convertObjectToJsonString(body)))
                .andDo(print())
                .andExpect(status().isInternalServerError())
                .andReturn().getResolvedException();

        assertEquals(SERVER_ERROR_MESSAGE, resolvedException.getMessage());
        assertEquals(CustomException.class, resolvedException.getClass());

        verify(mockRelationshipService, times(1)).removeFriend(anyString(), anyString());
        verifyNoMoreInteractions(mockRelationshipService);
    }


    @Test
    public void acceptFriend_whenAcceptFriendReturnsTrue_acceptFriend() throws Exception {
        Map<String, Object> body = new HashMap<>();
        body.put("fromUser", "1");
        body.put("toUser", "2");

        when(mockRelationshipService.acceptFriend(anyString(), anyString()))
                .thenReturn(true);

        this.mvc
                .perform(post("/relationship/acceptFriend")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(TestUtil.convertObjectToJsonString(body)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestUtil.TEXT_PLAIN_UTF8))
                .andExpect(jsonPath("$.success").value("true"))
                .andExpect(jsonPath("$.message").value(SUCCESSFUL_ADDED_FRIEND_MESSAGE));

        verify(mockRelationshipService, times(1)).acceptFriend(anyString(), anyString());
        verifyNoMoreInteractions(mockRelationshipService);
    }

    @Test
    public void acceptFriend_whenAcceptFriendReturnsFalse_throwException() throws Exception {
        Map<String, Object> body = new HashMap<>();
        body.put("fromUser", "1");
        body.put("toUser", "2");

        when(mockRelationshipService.acceptFriend(anyString(), anyString()))
                .thenReturn(false);

        Exception resolvedException = this.mvc
                .perform(post("/relationship/acceptFriend")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(TestUtil.convertObjectToJsonString(body)))
                .andDo(print())
                .andExpect(status().isInternalServerError())
                .andReturn().getResolvedException();

        assertEquals(SERVER_ERROR_MESSAGE, resolvedException.getMessage());
        assertEquals(CustomException.class, resolvedException.getClass());

        verify(mockRelationshipService, times(1)).acceptFriend(anyString(), anyString());
        verifyNoMoreInteractions(mockRelationshipService);
    }

    @Test
    public void cancelFriendshipRequest_whenCancelFriendshipRequestReturnsTrue_acceptFriend() throws Exception {
        Map<String, Object> body = new HashMap<>();
        body.put("fromUser", "1");
        body.put("toUser", "2");

        when(mockRelationshipService.cancelFriendshipRequest(anyString(), anyString()))
                .thenReturn(true);

        this.mvc
                .perform(post("/relationship/cancelRequest")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(TestUtil.convertObjectToJsonString(body)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(TestUtil.TEXT_PLAIN_UTF8))
                .andExpect(jsonPath("$.success").value("true"))
                .andExpect(jsonPath("$.message").value(SUCCESSFUL_REJECT_FRIEND_REQUEST_MESSAGE));

        verify(mockRelationshipService, times(1)).cancelFriendshipRequest(anyString(), anyString());
        verifyNoMoreInteractions(mockRelationshipService);
    }

    @Test
    public void cancelFriendshipRequest_whenCancelFriendshipRequestReturnsFalse_throwException() throws Exception {
        Map<String, Object> body = new HashMap<>();
        body.put("fromUser", "1");
        body.put("toUser", "2");

        when(mockRelationshipService.cancelFriendshipRequest(anyString(), anyString()))
                .thenReturn(false);

        Exception resolvedException = this.mvc
                .perform(post("/relationship/cancelRequest")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(TestUtil.convertObjectToJsonString(body)))
                .andDo(print())
                .andExpect(status().isInternalServerError())
                .andReturn().getResolvedException();

        assertEquals(SERVER_ERROR_MESSAGE, resolvedException.getMessage());
        assertEquals(CustomException.class, resolvedException.getClass());

        verify(mockRelationshipService, times(1)).cancelFriendshipRequest(anyString(), anyString());
        verifyNoMoreInteractions(mockRelationshipService);
    }

}
