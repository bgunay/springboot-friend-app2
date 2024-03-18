package org.pinsoft.interview.web.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.modelmapper.ModelMapper;
import org.pinsoft.interview.domain.dto.relationship.FriendsAllViewModel;
import org.pinsoft.interview.domain.dto.relationship.FriendsCandidatesViewModel;
import org.pinsoft.interview.domain.dto.relationship.RelationshipServiceModel;
import org.pinsoft.interview.service.RelationshipService;
import org.pinsoft.interview.utils.responseHandler.exceptions.CustomException;
import org.pinsoft.interview.utils.responseHandler.successResponse.SuccessResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.pinsoft.interview.utils.constants.ResponseMessageConstants.*;

@RestController
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

    @GetMapping(value = "/friends/{id}")
    public List<FriendsAllViewModel> findAllFriends(@PathVariable String id) throws Exception {
        List<RelationshipServiceModel> allFriends = this.relationshipService.findAllUserRelationshipsWithStatus(id);

        return allFriends.stream().map(relationshipServiceModel -> {
            if (!relationshipServiceModel.getUserOne().getId().equals(id)) {
                return this.modelMapper.map(relationshipServiceModel.getUserOne(), FriendsAllViewModel.class);
            }

            return this.modelMapper.map(relationshipServiceModel.getUserTwo(), FriendsAllViewModel.class);
        }).collect(Collectors.toList());
    }

    @PostMapping(value = "/addFriend")
    public ResponseEntity<String> addFriend(@RequestBody Map<String, Object> body) throws Exception {
        String loggedInUserId = (String) body.get("loggedInUserId");
        String friendCandidateId = (String) body.get("friendCandidateId");

        boolean result = this.relationshipService.createRequestForAddingFriend(loggedInUserId, friendCandidateId);

        if (result) {
            SuccessResponse successResponse = new SuccessResponse(LocalDateTime.now(), SUCCESSFUL_FRIEND_REQUEST_SUBMISSION_MESSAGE, "", true);

            return new ResponseEntity<>(this.objectMapper.writeValueAsString(successResponse), HttpStatus.OK);
        }

        throw new CustomException(SERVER_ERROR_MESSAGE);
    }

    @PostMapping(value = "/removeFriend")
    public ResponseEntity<String> removeFriend(@RequestBody Map<String, Object> body) throws Exception {
        String loggedInUserId = (String) body.get("loggedInUserId");
        String friendToRemoveId = (String) body.get("friendToRemoveId");

        boolean result = this.relationshipService.removeFriend(loggedInUserId, friendToRemoveId);

        if (result) {
            SuccessResponse successResponse = new SuccessResponse(LocalDateTime.now(), SUCCESSFUL_FRIEND_REMOVE_MESSAGE, "", true);
            return new ResponseEntity<>(this.objectMapper.writeValueAsString(successResponse), HttpStatus.OK);
        }

        throw new CustomException(SERVER_ERROR_MESSAGE);
    }

    @PostMapping(value = "/acceptFriend")
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

    @PostMapping(value = "/search", produces = "application/json")
    public List<FriendsCandidatesViewModel> searchUsers(@RequestBody Map<String, Object> body) {
        String loggedInUserId = (String) body.get("loggedInUserId");
        String search = (String) body.get("search");

        return this.relationshipService.searchUsers(loggedInUserId, search);
    }

    @GetMapping(value = "/findFriends/{id}", produces = "application/json")
    public List<FriendsCandidatesViewModel> findAllNotFriends(@PathVariable String id) {
        return this.relationshipService.findAllFriendCandidates(id);
    }
}

