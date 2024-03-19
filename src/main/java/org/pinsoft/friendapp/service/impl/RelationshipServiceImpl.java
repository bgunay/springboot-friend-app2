package org.pinsoft.friendapp.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.pinsoft.friendapp.domain.dto.relationship.FriendsCandidatesViewModel;
import org.pinsoft.friendapp.domain.dto.relationship.RelationshipServiceModel;
import org.pinsoft.friendapp.domain.repo.RelationshipRepository;
import org.pinsoft.friendapp.domain.repo.UserRepository;
import org.pinsoft.friendapp.domain.repo.entity.Relationship;
import org.pinsoft.friendapp.domain.repo.entity.UserEntity;
import org.pinsoft.friendapp.service.RelationshipService;
import org.pinsoft.friendapp.utils.validations.serviceValidation.services.RelationshipValidationService;
import org.pinsoft.friendapp.utils.validations.serviceValidation.services.UserValidationService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class RelationshipServiceImpl implements RelationshipService {
    private final RelationshipRepository relationshipRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final UserValidationService userValidation;
    private final RelationshipValidationService relationshipValidation;


    @Override
    public List<RelationshipServiceModel> findAllFriends(String userId) {
        List<Relationship> relationshipList = this.relationshipRepository
                .findRelationshipByUserIdAndStatus(userId, 1);

        return relationshipList
                .stream()
                .map(relationship -> this.modelMapper
                        .map(relationship, RelationshipServiceModel.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<RelationshipServiceModel> findPendingRequests(String userId) {
        List<Relationship> relationshipList = this.relationshipRepository
                .findRelationshipByUserOneIdAndStatus(userId, 0);

        return relationshipList
                .stream()
                .map(relationship -> this.modelMapper
                        .map(relationship, RelationshipServiceModel.class))
                .collect(Collectors.toList());
    }


    @Override
    public List<FriendsCandidatesViewModel> findAllFriendCandidates(String loggedInUserId) {
        List<UserEntity> userList = this.userRepository.findAll();

        List<Relationship> notCandidatesForFriends = this.relationshipRepository.findAllNotCandidatesForFriends(loggedInUserId);
        List<Relationship> relationshipWithStatusZero = this.relationshipRepository.findRelationshipByUserIdAndStatus(loggedInUserId, 0);

        List<UserEntity> usersWithRelationship = new ArrayList<>();

        notCandidatesForFriends.forEach(relationship -> {
            if (!relationship.getUserOne().getId().equals(loggedInUserId)) {
                usersWithRelationship.add(relationship.getUserOne());
            } else {
                usersWithRelationship.add(relationship.getUserTwo());
            }
        });

        List<FriendsCandidatesViewModel> notFriendsUserList = userList.stream()
                .filter(user -> !usersWithRelationship.contains(user) && !user.getId().equals(loggedInUserId))
                .map(user -> this.modelMapper.map(user, FriendsCandidatesViewModel.class)).collect(Collectors.toList());


        return notFriendsUserList.stream()
                .map(user -> mapUser(user, relationshipWithStatusZero))
                .collect(Collectors.toList());
    }

    @Override
    public boolean createRequestForAddingFriend(String loggedInUserId, String friendCandidateId) throws Exception {
        UserEntity loggedInUser = this.userRepository.findById(loggedInUserId)
                .filter(userValidation::isValid)
                .orElseThrow(Exception::new);

        UserEntity friendCandidateUser = this.userRepository.findById(friendCandidateId)
                .filter(userValidation::isValid)
                .orElseThrow(Exception::new);

        Relationship relationshipFromDb = this.relationshipRepository.findRelationshipByUserOneIdAndUserTwoId(loggedInUserId, friendCandidateId);

        if (relationshipFromDb == null) {
            Relationship relationship = new Relationship();
            relationship.setActionUser(loggedInUser);
            relationship.setUserOne(loggedInUser);
            relationship.setUserTwo(friendCandidateUser);
            relationship.setStatus(0);
            relationship.setTime(LocalDateTime.now());

            this.relationshipRepository.save(relationship);
            return true;
        } else {
            relationshipFromDb.setActionUser(loggedInUser);
            relationshipFromDb.setStatus(0);
            relationshipFromDb.setTime(LocalDateTime.now());
            this.relationshipRepository.save(relationshipFromDb);
            return true;
        }
    }

    @Override
    public boolean removeFriend(String loggedInUserId, String friendToRemoveId) throws Exception {
        return this.changeStatusAndSave(loggedInUserId, friendToRemoveId, 1, 2);
    }

    @Override
    public boolean acceptFriend(String loggedInUserId, String friendToAcceptId) throws Exception {
        return this.changeStatusAndSave(loggedInUserId, friendToAcceptId, 0, 1);
    }

    @Override
    public boolean cancelFriendshipRequest(String loggedInUserId, String friendToRejectId) throws Exception {
        return this.changeStatusAndSave(loggedInUserId, friendToRejectId, 0, 2);
    }

    private FriendsCandidatesViewModel mapUser(FriendsCandidatesViewModel user, List<Relationship> relationshipList) {
        Relationship relationshipWithCurrentUser = relationshipList.stream()
                .filter(relationship ->
                        relationship.getUserOne().getId().equals(user.getId()) ||
                                relationship.getUserTwo().getId().equals(user.getId()))
                .findFirst().orElse(null);

        if (relationshipWithCurrentUser != null) {
            user.setStatus(relationshipWithCurrentUser.getStatus());
            user.setStarterOfAction(relationshipWithCurrentUser.getActionUser().getId().equals(user.getId()));
        }

        return user;
    }

    private boolean changeStatusAndSave(String loggedInUserId, String friendId, int fromStatus, int toStatus) throws Exception {
        UserEntity loggedInUser = this.userRepository.findById(loggedInUserId)
                .filter(userValidation::isValid)
                .orElseThrow(Exception::new);

        UserEntity friend = this.userRepository.findById(friendId)
                .filter(userValidation::isValid)
                .orElseThrow(Exception::new);

        Relationship relationship = this.relationshipRepository
                .findRelationshipWithFriendWithStatus(
                        loggedInUserId, friendId, fromStatus);

        if (!relationshipValidation.isValid(relationship)) {
            throw new Exception();
        }

        relationship.setActionUser(loggedInUser);
        relationship.setStatus(toStatus);
        relationship.setTime(LocalDateTime.now());
        this.relationshipRepository.save(relationship);
        return true;
    }
}
