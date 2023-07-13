package uz.tm.tashman.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uz.tm.tashman.models.ProductionRequestModel;
import uz.tm.tashman.services.ProductionService;

@RestController
@RequestMapping("/produce")
public class ProduceController {
    @Autowired
    ProductionService productionService;
    @PostMapping("/save")
    public ResponseEntity<?> save(ProductionRequestModel productionRequestModel){

        return productionService.produce(productionRequestModel);
    }
}
