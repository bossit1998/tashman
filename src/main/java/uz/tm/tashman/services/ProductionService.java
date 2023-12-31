package uz.tm.tashman.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import uz.tm.tashman.entity.*;
import uz.tm.tashman.enums.StatusCodes;
import uz.tm.tashman.models.ProductionRequestModel;
import uz.tm.tashman.repository.*;
import uz.tm.tashman.util.HTTPUtil;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class ProductionService extends HTTPUtil {

        @Autowired
        ProductRepository productRepository;
        @Autowired
        ProductionRepository productionRepository;
        @Autowired
        InventoryRepository inventoryRepository;

        @Autowired
        AssortmentRepository assortmentRepository;

        @Autowired
        ProductPackingRepository productPackingRepository;



        public ResponseEntity<?> produce(ProductionRequestModel productionRequestModel){

                Production production = new Production();

                Optional<Product> optionalProduct = productRepository.findById(productionRequestModel.getProductId());
                if(!optionalProduct.isPresent()){
                        return OkResponse(StatusCodes.PRODUCT_NOT_FOUND);
                }
                Product product = optionalProduct.get();
                production.setProduct(product);

                Optional<Assortment> optionalAssortment = assortmentRepository.findById(productionRequestModel.getAssortmentId());
                if(!optionalAssortment.isPresent()) {
                        return OkResponse(StatusCodes.ASSORTMENT_NOT_FOUND);
                }
                Assortment assortment = optionalAssortment.get();
                production.setAssortment(assortment);
                production.setDate(LocalDateTime.now());

                production.setVolumeUnit(productionRequestModel.getVolumeUnit());

                production.setQuantity(productionRequestModel.getQuantity());
                ProductPacking productPacking = product.getProductPacking();

                Double numberOfPieces = production.getQuantity()/productPacking.getVolume();

                Double numberOfBoxes = numberOfPieces/productPacking.getBoxQuantity();

                production.setBoxCount(numberOfBoxes);

                productionRepository.save(production);


                Optional<Inventory> inventoryOptional = inventoryRepository.findByProduct(product);
                if(!inventoryOptional.isPresent()){
                        Inventory inventory = new Inventory();
                        inventory.setProduct(product);
                        inventory.setQuantity(numberOfBoxes);
                        inventory.setQuantity_unit(productPacking.getVolumeUnit());
                        inventory.setAssortmentId(assortment.getId());
                        inventory.setPackingId(productPacking.getId());
                        inventoryRepository.save(inventory);
                }else {
                        Inventory existingInventory = inventoryOptional.get();
                        existingInventory.setQuantity(existingInventory.getQuantity() + numberOfBoxes);
                        inventoryRepository.save(existingInventory);
                }


                return OkResponse(StatusCodes.SUCCESSFULLY_ADDED);
        }

}
