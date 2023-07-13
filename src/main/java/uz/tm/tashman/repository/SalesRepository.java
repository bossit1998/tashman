package uz.tm.tashman.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.tm.tashman.entity.Sales;

public interface SalesRepository extends JpaRepository<Sales,Long> {
}
