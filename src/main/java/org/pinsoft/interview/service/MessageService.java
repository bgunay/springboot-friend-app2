package org.pinsoft.interview.service;

import org.pinsoft.interview.domain.dto.message.MessageCreateBindingModel;
import org.pinsoft.interview.domain.dto.message.MessageFriendsViewModel;
import org.pinsoft.interview.domain.dto.message.MessageServiceModel;

import java.util.List;

public interface MessageService {

    MessageServiceModel createMessage(MessageCreateBindingModel messageCreateBindingModel, String loggedInUsername) throws Exception;

    List<MessageServiceModel> getAllMessages(String loggedInUsername, String chatUserId);

    List<MessageFriendsViewModel> getAllFriendMessages(String loggedInUsername);
}
