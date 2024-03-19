package org.pinsoft.friendapp.service;


import org.pinsoft.friendapp.domain.dto.relationship.FriendsCandidatesViewModel;
import org.pinsoft.friendapp.domain.dto.relationship.RelationshipServiceModel;

import java.util.List;

public interface RelationshipService {

    List<RelationshipServiceModel> findAllFriends(String userId) throws Exception;

    List<FriendsCandidatesViewModel> findAllFriendCandidates(String loggedInUserId);

    boolean createRequestForAddingFriend(String loggedInUserId, String friendCandidateId) throws Exception;

    boolean removeFriend(String loggedInUserId, String friendToRemoveId) throws Exception;

    boolean acceptFriend(String loggedInUserId, String friendToAcceptId) throws Exception;

    boolean cancelFriendshipRequest(String loggedInUserId, String friendToRejectId) throws Exception;

    List<RelationshipServiceModel> findPendingRequests(String userId);

}
