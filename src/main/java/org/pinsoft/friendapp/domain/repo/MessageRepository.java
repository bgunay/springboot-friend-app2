package org.pinsoft.friendapp.domain.repo;

import org.pinsoft.friendapp.domain.repo.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, String> {
    @Query(value = "" +
            "SELECT m FROM Message AS m " +
            "WHERE ((m.fromUser.id = :firstUserId AND m.toUser.id = :secondUserId) " +
            "OR  (m.fromUser.id = :secondUserId AND m.toUser.id = :firstUserId)) " +
            "ORDER BY m.time")
    List<Message> findAllMessagesBetweenTwoUsers(@Param("firstUserId") String firstUserId, @Param("secondUserId") String secondUserId);

    @Query(value = "UPDATE Message as m " +
            "SET m.status = 1 " +
            "WHERE m.toUser.id =:toUserId AND m.fromUser.id =:fromUserId " +
            "    AND m.status = 0")
    void updateStatusFromReadMessages(@Param("toUserId") String toUserId, @Param("fromUserId") String fromUserId);

    @Query(value = "select * " +
            "from messages AS m " +
            "where m.to_userid =:userId and m.status=0 " +
            "ORDER BY m.time DESC;", nativeQuery = true)
    List<Message> getAllUnreadMessages(@Param("userId") String loggedInUserId);

    @Query(value = "select m.from_userid, count(*)as count " +
            "from messages AS m " +
            "where m.status = 0 " +
            "  and m.to_userid =:userId " +
            "GROUP BY m.from_userid, m.time " +
            "ORDER BY m.time DESC;", nativeQuery = true)
    List<Object[]> getCountOfUnreadMessagesByFromUser(@Param("userId") String loggedInUserId);
}
