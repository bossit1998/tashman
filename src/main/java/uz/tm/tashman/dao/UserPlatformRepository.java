package uz.tm.tashman.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.tm.tashman.entity.UserPlatform;

import java.util.List;

@Repository
public interface UserPlatformRepository extends JpaRepository<UserPlatform, Long> {
    List<UserPlatform> findByUserId(Long id);

    List<UserPlatform> findByUserIdIn(List<Long> id);

    List<UserPlatform> findByFcmTokenOrVoipToken(String fcmToken, String voipToken);
}
