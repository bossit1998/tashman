package uz.tm.tashman.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.tm.tashman.entity.Logs;

@Repository
public interface LogsRepository extends JpaRepository<Logs, Long> {
}