package uz.tm.tashman.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import uz.tm.tashman.entity.*;
import uz.tm.tashman.enums.ERole;
import uz.tm.tashman.enums.StatusCodes;
import uz.tm.tashman.models.SalesRequestModel;
import uz.tm.tashman.repository.ProductRepository;
import uz.tm.tashman.repository.SalesRepository;
import uz.tm.tashman.repository.UserRepository;
import uz.tm.tashman.util.HTTPUtil;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

@Service
public class SalesService extends HTTPUtil {

    @Autowired
    SalesRepository salesRepository;
    @Autowired
    ProductRepository productRepository;
    @Autowired
    InventoryService inventoryService;
    @Autowired
    UserRepository userRepository;
    @Autowired
    UserService userService;

    public ResponseEntity<?> sell(SalesRequestModel sellingRequestModel) {
        Sales sale = new Sales();

        Optional<User> userOptional = userRepository.findById(sellingRequestModel.getClientId());
        if(userOptional.isPresent()){
            User user = userOptional.get();
            if(user.hasRole(ERole.ROLE_CLIENT)){
                Client client = user.getClient();
                sale.setClient(client);
            }else{
                return OkResponse(StatusCodes.USER_IS_NOT_CLIENT);
            }
        }else {
            return OkResponse(StatusCodes.USER_NOT_FOUND);
        }
        Optional<Product> optionalProduct = productRepository.findById(sellingRequestModel.getProductId());

        if (optionalProduct.isPresent()){
            Product product = optionalProduct.get();
            for(Assortment assortment: product.getAssortments()){
                if(Objects.equals(assortment.getId(), sellingRequestModel.getAssortmentId())){
                    sale.setAssortment(assortment);
                    sale.setAmount(sellingRequestModel.getAmount());
                    sale.setSoldDate(LocalDateTime.now());
                    salesRepository.save(sale);
                    inventoryService.updateSelling(product,assortment,sellingRequestModel.getAmount());
                }
            }
        }
        return OkResponse(StatusCodes.SUCCESSFULLY_ADDED);
    }
}