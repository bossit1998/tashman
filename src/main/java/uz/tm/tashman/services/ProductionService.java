package uz.tm.tashman.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import uz.tm.tashman.entity.Assortment;
import uz.tm.tashman.entity.Product;
import uz.tm.tashman.entity.ProductPacking;
import uz.tm.tashman.entity.Production;
import uz.tm.tashman.models.ProductionRequestModel;
import uz.tm.tashman.repository.AssortmentRepository;
import uz.tm.tashman.repository.ProductPackingRepository;
import uz.tm.tashman.repository.ProductRepository;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class ProductionService {

        @Autowired
        ProductRepository productRepository;

        @Autowired
        AssortmentRepository assortmentRepository;

        @Autowired
        ProductPackingRepository productPackingRepository;



        public ResponseEntity<?> produce(ProductionRequestModel productionRequestModel){

                Production production = new Production();

                Optional<Product> optionalProduct = productRepository.findById(productionRequestModel.getProductId());
                if(optionalProduct.isPresent()){
                        Product product = optionalProduct.get();
                        production.setProduct(product);
                }
                Optional<Assortment> optionalAssortment = assortmentRepository.findById(productionRequestModel.getAssortmentId());
                if(optionalAssortment.isPresent()) {
                        Assortment assortment = optionalAssortment.get();
                        production.setAssortment(assortment);
                }
                production.setDate(LocalDateTime.now());





                production.setQuantity(productionRequestModel.getQuantity());
                ProductPacking productPacking = new ProductPacking();








                return null;
        }

}
