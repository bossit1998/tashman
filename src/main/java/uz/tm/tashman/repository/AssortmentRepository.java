package uz.tm.tashman.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.tm.tashman.entity.Assortment;
import uz.tm.tashman.entity.Product;
import uz.tm.tashman.entity.ProductImage;

import java.util.List;

@Repository
public interface AssortmentRepository extends JpaRepository<Assortment,Long> {
    List<Assortment> findAllByProductId(Long id);

    List<Assortment> findAllByProduct(Product product);

    void deleteByProductId(Long id);

}
