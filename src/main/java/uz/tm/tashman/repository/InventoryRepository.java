package uz.tm.tashman.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.tm.tashman.entity.Inventory;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory,Long> {

}
