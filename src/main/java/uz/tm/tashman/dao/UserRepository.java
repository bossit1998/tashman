package uz.tm.tashman.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.tm.tashman.entity.Role;
import uz.tm.tashman.entity.User;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    Boolean existsByUsername(String username);

    Page<User> findByRoles(Role role, Pageable pageable);

//	@Query(value = "select * from users where users.id in (select ug.user_id from user_agent ug " +
//			"where ug.user_agent=:userAgent and (current_date - CAST(cast(ug.token_date as TIMESTAMP) AS DATE) <= 7)) " +
//			"and users.username=:phoneNumber", nativeQuery = true)
//	User getByUserAgentEquals(@Param(value = "phoneNumber") String phoneNumber,
//			@Param(value = "userAgent") String userAgent);

}