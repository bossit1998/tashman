package uz.tm.tashman.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.tm.tashman.entity.Assortment;

@Repository
public interface AssortmentRepository extends JpaRepository<Assortment,Long> {

}