package uz.tm.tashman.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import uz.tm.tashman.entity.Assortment;
import uz.tm.tashman.entity.Product;
import uz.tm.tashman.enums.StatusCodes;
import uz.tm.tashman.models.AssortmentResponseModel;
import uz.tm.tashman.models.ResponseModel;
import uz.tm.tashman.repository.AssortmentRepository;
import uz.tm.tashman.repository.ProductRepository;
import uz.tm.tashman.util.HTTPUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
public class AssortmentService extends HTTPUtil {
    final
    AssortmentRepository assortmentRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    LogService logService;


    public AssortmentService(AssortmentRepository assortmentRepository) {
        this.assortmentRepository = assortmentRepository;
    }


    public ResponseEntity<?> editAssortmentList(List<String> list, Long productId) {

        try{
        Optional<Product> optionalProduct = productRepository.findById(productId);
        if(optionalProduct.isPresent()){
            Product product = optionalProduct.get();

            List<Assortment> assortmentList = new ArrayList<>();
            for(String assortmentName: list){
                Assortment assortment = new Assortment();
                assortment.setName(assortmentName);
                assortment.setProduct(product);
                assortmentList.add(assortment);
            }
            product.setAssortments(assortmentList);
            productRepository.save(product);

            return OkResponse(StatusCodes.SUCCESSFULLY_EDITED);
        }
        }catch (Exception e){
            logService.saveToLog(exceptionAsString(e));
            return InternalServerErrorResponse(e);
        }
        return null;
    }
}
