package org.pinsoft.friendapp.domain.repo;

import jakarta.transaction.Transactional;
import org.pinsoft.friendapp.domain.repo.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public interface UserRepository extends JpaRepository<UserEntity, String> {
    UserEntity findByEmail(String email);

    UserEntity findByEmailAndPassword(String email, String password);

    UserEntity findAllByFirstName(String firstName);

    Optional<UserEntity> findByUsername(String username);

    @Query(value = "" +
            "SELECT u FROM UserEntity AS u " +
            "WHERE u.id <> :id AND " +
            "(LOWER(u.firstName) LIKE CONCAT('%', :searchSymbols, '%') OR " +
            "LOWER(u.lastName) LIKE CONCAT('%', :searchSymbols, '%'))  ")
    List<UserEntity> findAllUsersLike(@Param(value = "id") String id,
                                      @Param(value = "searchSymbols") String searchSymbols);

    @Transactional
    @Modifying
    @Query(value = "Update UserEntity as u " +
            "SET u.isOnline = false " +
            "WHERE u.isOnline = true ")
    void setIsOnlineToFalse();
}
