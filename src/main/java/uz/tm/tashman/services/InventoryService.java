package uz.tm.tashman.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import uz.tm.tashman.entity.Assortment;
import uz.tm.tashman.entity.Inventory;
import uz.tm.tashman.entity.Product;
import uz.tm.tashman.enums.StatusCodes;
import uz.tm.tashman.repository.InventoryRepository;
import uz.tm.tashman.util.HTTPUtil;

import java.util.Optional;

@Service
public class InventoryService extends HTTPUtil {

    @Autowired
    InventoryRepository inventoryRepository;

    public ResponseEntity<?> updateSelling(Product product, Assortment assortment, Double amount) {

        Optional<Inventory> inventoryOptional = inventoryRepository.findByProduct(product);

        if (!inventoryOptional.isPresent()) {
            Inventory inventory = new Inventory();
            inventory.setProduct(product);
            inventory.setQuantity(amount);
            inventory.setQuantity_unit(product.getProductPacking().getUnit());
            inventory.setAssortmentId(assortment.getId());
            inventory.setPackingId(product.getProductPacking().getId());
            inventoryRepository.save(inventory);
        } else {
            Inventory existingInventory = inventoryOptional.get();
            existingInventory.setQuantity(existingInventory.getQuantity() - amount);
            inventoryRepository.save(existingInventory);

        }
        return OkResponse(StatusCodes.SUCCESSFULLY_ADDED);
    }
}