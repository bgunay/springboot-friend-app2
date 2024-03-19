package org.pinsoft.friendapp.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.modelmapper.ModelMapper;
import org.pinsoft.friendapp.domain.dto.relationship.FriendsAllViewModel;
import org.pinsoft.friendapp.domain.dto.relationship.FriendsCandidatesViewModel;
import org.pinsoft.friendapp.domain.dto.relationship.RelationshipServiceModel;
import org.pinsoft.friendapp.service.RelationshipService;
import org.pinsoft.friendapp.utils.responseHandler.exceptions.CustomException;
import org.pinsoft.friendapp.utils.responseHandler.successResponse.SuccessResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.pinsoft.friendapp.utils.constants.ResponseMessageConstants.*;

@RestController
@Tag(name = "relationship", description = "relationship")
@RequestMapping(value = "/relationship")
public class RelationshipController {

    private final RelationshipService relationshipService;
    private final ModelMapper modelMapper;
    private final ObjectMapper objectMapper;

    @Autowired
    public RelationshipController(RelationshipService relationshipService, ModelMapper modelMapper, ObjectMapper objectMapper) {
        this.relationshipService = relationshipService;
        this.modelMapper = modelMapper;
        this.objectMapper = objectMapper;
    }

    @Operation(
            summary = "find all friends of user with UserId",
            description = "find all friends of user with UserId."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "ok"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping(value = "/allfriends/{userId}")
    public List<FriendsAllViewModel> findAllFriends(@PathVariable String userId) throws Exception {
        List<RelationshipServiceModel> allFriends = this.relationshipService.findAllFriends(userId);

        return allFriends.stream().map(relationshipServiceModel -> {
            if (!relationshipServiceModel.getUserOne().getId().equals(userId)) {
                return this.modelMapper.map(relationshipServiceModel.getUserOne(), FriendsAllViewModel.class);
            }

            return this.modelMapper.map(relationshipServiceModel.getUserTwo(), FriendsAllViewModel.class);
        }).collect(Collectors.toList());
    }

    @PostMapping(value = "/addFriend")
    @Operation(
            summary = "add a friend",
            description = "add a friend."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "ok"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<String> addFriend(@RequestBody Map<String, Object> body) throws Exception {
        String fromUser = (String) body.get("fromUser");
        String toUser = (String) body.get("toUser");

        boolean result = this.relationshipService.createRequestForAddingFriend(fromUser, toUser);

        if (result) {
            SuccessResponse successResponse = new SuccessResponse(LocalDateTime.now(), SUCCESSFUL_FRIEND_REQUEST_SUBMISSION_MESSAGE, "", true);

            return new ResponseEntity<>(this.objectMapper.writeValueAsString(successResponse), HttpStatus.OK);
        }

        throw new CustomException(SERVER_ERROR_MESSAGE);
    }

    @PostMapping(value = "/removeFriend")
    @Operation(
            summary = "remove a friend",
            description = "remove a friend."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "ok"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<String> removeFriend(@RequestBody Map<String, Object> body) throws Exception {
        String loggedInUserId = (String) body.get("fromUser");
        String friendToRemoveId = (String) body.get("toUser");

        boolean result = this.relationshipService.removeFriend(loggedInUserId, friendToRemoveId);

        if (result) {
            SuccessResponse successResponse = new SuccessResponse(LocalDateTime.now(), SUCCESSFUL_FRIEND_REMOVE_MESSAGE, "", true);
            return new ResponseEntity<>(this.objectMapper.writeValueAsString(successResponse), HttpStatus.OK);
        }

        throw new CustomException(SERVER_ERROR_MESSAGE);
    }

    @PostMapping(value = "/acceptFriend")
    @Operation(
            summary = "accept friend request",
            description = "accept friend request."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "ok"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<String> acceptFriend(@RequestBody Map<String, Object> body) throws Exception {
        String loggedInUserId = (String) body.get("loggedInUserId");
        String friendToAcceptId = (String) body.get("friendToAcceptId");

        boolean result = this.relationshipService.acceptFriend(loggedInUserId, friendToAcceptId);

        if (result) {
            SuccessResponse successResponse = new SuccessResponse(LocalDateTime.now(), SUCCESSFUL_ADDED_FRIEND_MESSAGE, "", true);
            return new ResponseEntity<>(this.objectMapper.writeValueAsString(successResponse), HttpStatus.OK);
        }
        throw new CustomException(SERVER_ERROR_MESSAGE);
    }

    @PostMapping(value = "/cancelRequest")
    @Operation(
            summary = "cancel friend request",
            description = "cancel friend request."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "ok"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<String> cancelFriendshipRequest(@RequestBody Map<String, Object> body) throws Exception {
        String loggedInUserId = (String) body.get("loggedInUserId");
        String friendToRejectId = (String) body.get("friendToRejectId");

        boolean result = this.relationshipService.cancelFriendshipRequest(loggedInUserId, friendToRejectId);

        if (result) {
            SuccessResponse successResponse = new SuccessResponse(LocalDateTime.now(), SUCCESSFUL_REJECT_FRIEND_REQUEST_MESSAGE, "", true);
            return new ResponseEntity<>(this.objectMapper.writeValueAsString(successResponse), HttpStatus.OK);
        }

        throw new CustomException(SERVER_ERROR_MESSAGE);
    }

    @GetMapping(value = "/findAllFriendCandidates/{id}", produces = "application/json")
    @Operation(
            summary = "findPendingRequests for user",
            description = "findPendingRequests for user."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "ok"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public List<FriendsCandidatesViewModel> findAllFriendCandidates(@PathVariable String id) {
        return this.relationshipService.findAllFriendCandidates(id);
    }

    @GetMapping(value = "/findMyPendingRequests/{id}", produces = "application/json")
    @Operation(
            summary = "findMyPendingRequests for user",
            description = "findMyPendingRequests for user."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "ok"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public List<RelationshipServiceModel> findMyPendingRequests(@PathVariable String id) {
        return this.relationshipService.findPendingRequests(id);
    }
}

