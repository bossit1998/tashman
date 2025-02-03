package uz.tm.tashman.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.tm.tashman.entity.User;
import uz.tm.tashman.entity.UserAgent;

import java.util.Optional;

@Repository
public interface UserAgentRepository extends JpaRepository<UserAgent, Long> {
    Optional<UserAgent> findByUserAndUserAgent(User user, String userAgent);

    Optional<UserAgent> findByEncodedId(String encodedId);

    Optional<UserAgent> findByEncodedIdAndUser(String encodedId, User user);

    Optional<UserAgent> findByEncodedIdAndUserAndIsDeletedFalse(String encodedId, User user);
}