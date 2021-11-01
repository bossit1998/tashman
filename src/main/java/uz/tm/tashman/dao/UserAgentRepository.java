package uz.tm.tashman.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.tm.tashman.entity.User;
import uz.tm.tashman.entity.UserAgent;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserAgentRepository extends JpaRepository<UserAgent, Long> {
    Optional<UserAgent> findByUserAndUserAgent(User user, String userAgent);

    List<UserAgent> findAllByUserAndVerifiedTrueAndDeletedFalse(User user);

}