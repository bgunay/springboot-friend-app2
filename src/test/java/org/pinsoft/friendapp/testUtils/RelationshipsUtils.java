package org.pinsoft.friendapp.testUtils;

import org.pinsoft.friendapp.domain.dto.relationship.FriendsAllViewModel;
import org.pinsoft.friendapp.domain.dto.relationship.FriendsCandidatesViewModel;
import org.pinsoft.friendapp.domain.dto.relationship.RelationshipServiceModel;
import org.pinsoft.friendapp.domain.repo.entity.Relationship;
import org.pinsoft.friendapp.domain.repo.entity.UserEntity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class RelationshipsUtils {
    public static Relationship createRelationship(UserEntity userOne, UserEntity userTwo, int status, UserEntity actionUser) {
        LocalDateTime time = LocalDateTime.now();

        return new Relationship() {{
            setId("1");
            setUserOne(userOne);
            setUserTwo(userTwo);
            setStatus(status);
            setActionUser(actionUser);
            setTime(time);
        }};
    }

    public static List<Relationship> getRelationshipList(int count, UserEntity userOne, UserEntity userTwo, int status, UserEntity actionUser) {
        LocalDateTime time = LocalDateTime.now();

        return IntStream.range(0, count)
                .mapToObj(index -> new Relationship() {{
                    setId(String.valueOf(index + 1));
                    setUserOne(userOne);
                    setUserTwo(userTwo);
                    setStatus(status);
                    setActionUser(actionUser);
                    setTime(time);
                }})
                .collect(Collectors.toList());
    }

    public static RelationshipServiceModel getRelationshipServiceModel(UserEntity userOne, UserEntity userTwo, int status, UserEntity actionUser) {
        LocalDateTime time = LocalDateTime.now();

        return new RelationshipServiceModel() {{
            setId(String.valueOf(1));
            setUserOne(userOne);
            setUserTwo(userTwo);
            setStatus(status);
            setActionUser(actionUser);
            setTime(time);
        }};
    }

    public static List<FriendsCandidatesViewModel> getFriendsCandidatesViewModel(int count) {
        return IntStream.range(0, count)
                .mapToObj(index -> new FriendsCandidatesViewModel() {{
                    setId(String.valueOf(index + 1));
                    setFirstName("Ali " + index);
                    setLastName("Aliv " + index);
                    setUsername("ali " + index);
                    setStarterOfAction(false);
                    setStatus(2);
                }})
                .collect(Collectors.toList());
    }

    public static List<FriendsAllViewModel> getFriendsAllViewModels(int count) {
        return IntStream.range(0, count)
                .mapToObj(index -> new FriendsAllViewModel() {{
                    setId(String.valueOf(index + 1));
                    setFirstName("Ali " + index);
                    setLastName("Aliv " + index);
                    setUsername("ali " + index);
                }})
                .collect(Collectors.toList());
    }
}
