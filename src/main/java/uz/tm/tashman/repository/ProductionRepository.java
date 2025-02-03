package uz.tm.tashman.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.tm.tashman.entity.Production;

@Repository
public interface ProductionRepository extends JpaRepository<Production,Long> {
}