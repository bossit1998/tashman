package uz.tm.tashman.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.tm.tashman.entity.ProductCategory;

import java.util.Optional;

@Repository
public interface ProductCategoryRepository extends JpaRepository<ProductCategory, Long> {
    Optional<ProductCategory> findBySlugAndIsDeletedFalse(String slug);

//    @Query(value = "select * from product_category pc join product p " +
//            " on pc.id = p.product_category_id where p.id = :productId", nativeQuery = true)
//    Optional<ProductCategory> findProductCategoryByProductId(Long productId);
}