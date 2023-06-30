package uz.tm.tashman.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.tm.tashman.entity.ProductPacking;

@Repository
public interface ProductPackingRepository extends JpaRepository<ProductPacking,Long> {
}
