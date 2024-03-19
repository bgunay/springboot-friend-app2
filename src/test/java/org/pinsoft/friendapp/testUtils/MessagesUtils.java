package org.pinsoft.friendapp.testUtils;

import org.pinsoft.friendapp.domain.dto.message.MessageAllViewModel;
import org.pinsoft.friendapp.domain.dto.message.MessageCreateBindingModel;
import org.pinsoft.friendapp.domain.dto.message.MessageFriendsViewModel;
import org.pinsoft.friendapp.domain.dto.message.MessageServiceModel;
import org.pinsoft.friendapp.domain.repo.entity.Message;
import org.pinsoft.friendapp.domain.repo.entity.Relationship;
import org.pinsoft.friendapp.domain.repo.entity.UserEntity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class MessagesUtils {
    public static Message createMessage(UserEntity fromUser, UserEntity toUser, Relationship relationship) {
        LocalDateTime time = LocalDateTime.now();
        return new Message() {{
            setId("1");
            setFromUser(fromUser);
            setToUser(toUser);
            setRelationship(relationship);
            setContent("content message");
            setSubject("subject message");
            setStatus(0);
            setTime(time);
        }};
    }

    public static List<Message> getMessages(int count, UserEntity fromUser, UserEntity toUser, Relationship relationship) {
        LocalDateTime time = LocalDateTime.now();

        return IntStream.range(0, count)
                .mapToObj(index -> new Message() {{
                    setId(String.valueOf(index + 1));
                    setFromUser(fromUser);
                    setToUser(toUser);
                    setRelationship(relationship);
                    setContent("message " + index + " content");
                    setSubject("message " + index + " subject");
                    setStatus(0);
                    setTime(time);
                }})
                .collect(Collectors.toList());
    }

    public static List<MessageServiceModel> getMessageServiceModels(int count, UserEntity fromUser, UserEntity toUser, Relationship relationship) {
        LocalDateTime time = LocalDateTime.now();

        return IntStream.range(0, count)
                .mapToObj(index -> new MessageServiceModel() {{
                    setId(String.valueOf(index + 1));
                    setFromUser(fromUser);
                    setToUser(toUser);
                    setRelationship(relationship);
                    setContent("message " + index + " content");
                    setSubject("message " + index + " subject");
                    setStatus(0);
                    setTime(time);
                }})
                .collect(Collectors.toList());
    }

    public static List<MessageAllViewModel> getMessageAllViewModels(int count) {
        LocalDateTime time = LocalDateTime.now();

        return IntStream.range(0, count)
                .mapToObj(index -> new MessageAllViewModel() {{
                    setId(String.valueOf(index + 1));
                    setFromUserId(String.valueOf(index + 1));
                    setContent("message " + index + " content");
                    setTime(time);
                }})
                .collect(Collectors.toList());
    }

    public static List<MessageFriendsViewModel> getMessageFriendsViewModels(int count) {
        LocalDateTime time = LocalDateTime.now();

        return IntStream.range(0, count)
                .mapToObj(index -> new MessageFriendsViewModel() {{
                    setId(String.valueOf(index + 1));
                    setFromUserId(String.valueOf(index + 1));
                    setFromUserFirstName("firstName " + index);
                    setFromUserLastName("laststName " + index);
                    setContent("message " + index + " content");
                    setTime(time);
                    setCount(1);
                }})
                .collect(Collectors.toList());
    }

    public static MessageCreateBindingModel getMessageCreateBindingModel() {
        return new MessageCreateBindingModel() {{
            setContent("message content");
            setToUserId("toUserId");
        }};
    }

    public static List<Object[]> getCountOfUnreadMessagesObjectArr(int count) {
        return IntStream.range(0, count)
                .mapToObj(index -> new Object[]{
                        String.valueOf(index + 1),
                        String.valueOf(1),
                })
                .collect(Collectors.toList());
    }
}
