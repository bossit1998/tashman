package uz.tm.tashman.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.tm.tashman.entity.ProductMeta;

@Repository
public interface ProductMetaRepository extends JpaRepository<ProductMeta, Long> {
}