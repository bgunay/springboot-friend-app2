package org.pinsoft.interview.domain.repo;

import org.pinsoft.interview.domain.repo.entity.Logger;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LoggerRepository extends JpaRepository<Logger, String> {
    List<Logger> findAllByOrderByTimeDesc();

    List<Logger> findAllByUsernameOrderByTimeDesc(String username);

    List<Logger> deleteAllByUsername(String username);
}